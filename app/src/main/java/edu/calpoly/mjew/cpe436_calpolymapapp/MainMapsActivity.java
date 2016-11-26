package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainMapsActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private Fragment f1;
    private Fragment f2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);

        //Initializing toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(R.string.title_activity_main_maps);
        setSupportActionBar(null);
        setSupportActionBar(tb);

        //Initializing Spinner
        Spinner buildingList = (Spinner) findViewById(R.id.buildingSelection);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.buildings, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingList.setAdapter(spinAdapter);
        buildingList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                if(pos > 0)
                {
                    Log.d("onItemSelected: ", "Creating a new BuildingDetailFragment");
                    BuildingDetailFragment bdf = new BuildingDetailFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.layout_2, bdf);
                    findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
                    ft.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //Initializing the Google map fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        LatLng calpoly = new LatLng(35.300095, -120.659001);
        //mMap.addMarker(new MarkerOptions().position(calpoly).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calpoly));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        PolylineOptions plo = new PolylineOptions();
        plo.add(new LatLng(35.301000, -120.659900), new LatLng(35.300295, -120.66000),
                new LatLng(35.30095, -120.659000), new LatLng(35.30000, -120.659000));
        plo.color(0xFFEE0000);
        mMap.addPolyline(plo);
    }
}
