package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_WORLD_READABLE;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.appContentResolver;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.appContext;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

/**
 * Created by mackenzie on 12/1/16.
 */
public class RouteCreatorFragment extends Fragment
{
    private Button mSaveButton;
    private Button mResetButton;
    private Button mCancelButton;
    private CheckBox mFreezeCheckBox;
    private Polyline shownRoute;
    private PolylineOptions routeToSave; //The final route to save all the waypoints to
    private ArrayList<LatLng> routeToMake; //a middle collection class to record waypoints. Editable.
    //private ArrayList<Polyline> oldRoutes;

    private GoogleMap mMapObject;
    private String mCurrentPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        routeToSave = new PolylineOptions();
        routeToMake = new ArrayList<LatLng>();
        shownRoute = null;
        if (container == null)
            Log.d("onCreateView Route: ", "attaching fragment. Container is: null");
        else
            Log.d("onCreateView Route: ", "attaching fragment. Container is: there");
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_route_generation, container, false);
        mSaveButton = (Button)ll.findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Route newRoute = new Route();
                newRoute.setCreateName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                newRoute.setSnapshotPath("");
                newRoute.replaceWaypoints(routeToMake);

                selectedBuilding.addNewRoute(newRoute);
                int lastPos = selectedBuilding.
                        getAllBuildingRoutes().indexOf(newRoute);

                Log.v("SIZZZZZZZZEEE", "size = " + lastPos);

                // grab specified rout
                Route bR = selectedBuilding.getAllBuildingRoutes().get(lastPos);
                //bR.replaceWaypoints(routeToMake);

                // create temp file for snapshot
                //File storDir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                //File image_file = new File(storDir, "test_image.jpg");
                //mCurrentPath = image_file.getAbsolutePath();

                // take and save map snapshot
                //getMapSnapshot();
                //bR.uploadMapSnapshot(mCurrentPath, appContext);

                getActivity().onBackPressed();
            }
        });

        mCancelButton = (Button) ll.findViewById(R.id.cancelRouteCreate);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mResetButton = (Button)ll.findViewById(R.id.resetButton);
        mFreezeCheckBox = (CheckBox)ll.findViewById(R.id.freezeCheckBox);
        return ll;
    }

    public void initRouteClickListeners(GoogleMap map)
    {

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                routeToMake.add(latLng);
                routeToSave.add(latLng);
                if(shownRoute != null)
                    shownRoute.remove();
                routeToSave = new PolylineOptions();
                routeToSave.addAll(routeToMake);
                shownRoute = ((MainMapsActivity) getActivity()).getGoogleMap().addPolyline(routeToSave);
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override //click listener will remove LatLng listeners from the ArrayList. Allows user to correct for mistakes
            public void onClick(View v)
            {
                Log.d("MACKENZIE: ", "Reset Button. Removing the following items from the ArrayList");
                if(routeToMake.size() > 0)
                {
                    Log.d("MACKENZIE: ", ((LatLng)routeToMake.remove(routeToMake.size()-1)).toString());
                    Log.d("MACKENZIE: ", "All items in the list");
                    for(int i = 0; i < routeToMake.size(); i++)
                    {
                        Log.d("MACKENZIE: ", routeToMake.get(i).toString());
                    }
                }
                else
                    Log.d("MACKENZIE: ", "ArrayList is empty");
                if(shownRoute != null)
                    shownRoute.remove();
                routeToSave = new PolylineOptions();
                routeToSave.addAll(routeToMake);
                shownRoute = ((MainMapsActivity)getActivity()).getGoogleMap().addPolyline(routeToSave);
            }
        });

        mFreezeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                    ((MainMapsActivity)getActivity()).lockMap();
                else
                    ((MainMapsActivity)getActivity()).unlockMap();
            }
        });

        mMapObject = map;
    }

    private void getMapSnapshot(){

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {

            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                // TODO Auto-generated method stub
                Bitmap bitmap= snapshot;

                OutputStream fout = null;

                try
                {
                    //fout = appContext.openFileOutput(mCurrentPath, MODE_APPEND);

                    fout = new FileOutputStream(new File(mCurrentPath.toString()), true);

                    Log.v("INNNNNSSIDE", "here");
                    Log.v("SNAPSHOTPATH", "path = " + mCurrentPath);


                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();

                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    mCurrentPath = "";
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    mCurrentPath = "";
                }
            }
        };

        mMapObject.snapshot(callback);
    }



}
