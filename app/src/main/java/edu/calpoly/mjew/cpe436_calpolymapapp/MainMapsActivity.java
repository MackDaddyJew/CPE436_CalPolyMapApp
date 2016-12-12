package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * Meant to be Activity #1. Utilizes 2 fragments.
 */
public class MainMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int CONFIG1 = 1; //signifies Route Creator configuration

    public static Building selectedBuilding;
    public static Context appContext;
    public static ContentResolver appContentResolver;

    private GoogleMap mMap;
    private Marker mCurrentMarker;
    RouteCreatorFragment mRCF;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);



        Intent thisIntent = getIntent();
        if(thisIntent.getIntExtra("CONFIG", 0) == CONFIG1)
        {
            Log.d("MACKENZIE: ", "Launching MainMapsActivity with route creator");
            initToolbar();
            initGoogleMap();
            initRouteMaker();
        }

        else
        {
            //Main configuration for map
            resetLayoutWeight();
            initToolbar();
            initSpinner();
            initGoogleMap();
        }

        appContext = getApplicationContext();
        appContentResolver = getContentResolver();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng calpoly = new LatLng(35.300972, -120.659001); //35.300972
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calpoly, 15f));
        Log.d("onMapReady: ", mMap.getCameraPosition().toString());
        if(findViewById(R.id.routeGeneration) != null)
            mRCF.initRouteClickListeners(mMap);

        // add a marker to the map
        mCurrentMarker = mMap.addMarker(new MarkerOptions()
        .position(calpoly)
        .title("Center of Campus"));
    }

    public GoogleMap getGoogleMap() {return mMap; }

    public void resetMarker(LatLng buildingLoc, String buildingName) {

        // remove previous marker
        mCurrentMarker.remove();

        // add a marker to the map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buildingLoc, 17.5f));
        mCurrentMarker = mMap.addMarker(new MarkerOptions()
                .position(buildingLoc)
                .title(buildingName));
    }

    //Prevents the map from scrolling around or zooming.
    public void lockMap()
    {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    public void unlockMap()
    {
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    public void displayRoute(PolylineOptions plo) { mMap.addPolyline(plo); }

    //Initializing Toolbar
    private void initToolbar()
    {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(R.string.title_activity_main_maps);
        setSupportActionBar(null);
        setSupportActionBar(tb);
    }

    //Initializing the Google map fragment
    private void initGoogleMap()
    {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //resets R.id.layout_2 weight back to original value of 0.5
    private void resetLayoutWeight()
    {
        FrameLayout fl = (FrameLayout)findViewById(R.id.layout_2);
        if(this.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            fl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.5f));
        else if(this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            fl.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f));
    }

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.layout_2).setVisibility(View.GONE);
        }
        else
            super.onBackPressed();
    }

    //Initializing Spinner
    private void initSpinner()
    {
        Spinner buildingList = (Spinner) findViewById(R.id.buildingSelection);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.buildings, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingList.setAdapter(spinAdapter);
        buildingList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 0) {
                    FragmentTransaction temp = getSupportFragmentManager().beginTransaction();
                    if (getSupportFragmentManager().findFragmentByTag("ListFragment") != null)
                        temp.remove(getSupportFragmentManager().findFragmentByTag("ListFragment"));
                    if (getSupportFragmentManager().findFragmentByTag("BuildingDetailFragment") != null)
                        temp.remove(getSupportFragmentManager().findFragmentByTag("BuildingDetailFragment"));
                    temp.commit();
                    findViewById(R.id.layout_2).setVisibility(View.GONE);
                }
                else if (pos > 0) {

                    // get the number of the building
                    String buildingName = parent.getSelectedItem().toString();

                    // access picture from database
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("BuildingIndex", pos);

                    Log.d("onItemSelected: ", "Creating a new BuildingDetailFragment");
                    BuildingDetailFragment bdf = new BuildingDetailFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.layout_2, bdf, "BuildingDetailFragment");
                    ft.addToBackStack("BuildingDetailFragment");
                    findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
                    bdf.setArguments(mBundle);
                    ft.commit();
                    bdf = null;
                    ft = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void initRouteMaker()
    {
        mRCF = new RouteCreatorFragment();
        FragmentTransaction temp = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag("ListFragment") != null)
            temp.remove(getSupportFragmentManager().findFragmentByTag("ListFragment"));
        if (getSupportFragmentManager().findFragmentByTag("BuildingDetailFragment") != null)
            temp.remove(getSupportFragmentManager().findFragmentByTag("BuildingDetailFragment"));
        temp.add(R.id.layout_2, mRCF);
        FrameLayout fl = (FrameLayout)findViewById(R.id.layout_2);
        fl.setVisibility(View.VISIBLE);

        if(this.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            fl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.1f));
        else if(this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            fl.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.1f));
        temp.commit();
        Log.d("MACKENZIE: ", "Commited Route creator");
    }
}
