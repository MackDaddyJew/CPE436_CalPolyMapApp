package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.appContentResolver;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.appContext;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

/**
 * Created by mackenzie on 11/22/16.
 * this class is meant to be the object representation of a route. It should contain all waypoints that
 * make up the route and any meta data associated with it.
 */
public class Route
{
    private String mCreatorName = "Sample Name";
    private ArrayList<LatLng> mWaypoints;
    private String mMapSnapshot;

    public Route()
    {
        mWaypoints = new ArrayList<>();
    }

    public ArrayList<LatLng> getWaypoints() { return mWaypoints; }

    public void addWaypoint(LatLng wp)
    {
        mWaypoints.add(wp);
    }

    public void replaceWaypoints(ArrayList<LatLng> newWp)
    {
        mWaypoints = newWp;
    }

    public String getCreatorName() {return mCreatorName;}

    public void setCreateName(String creatorName) { mCreatorName = creatorName; }

    public void setSnapshotPath(String snapshotPath) {
        mMapSnapshot = snapshotPath;
        //uploadMapSnapshot(snapshotPath, appContentResolver, appContext);
    }

    // upload photo passed in as a String path
    public void uploadMapSnapshot(String currentPhotoPath, final Context context){


        // get components of file name to upload to Firebase Storage
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String buildingNumber = selectedBuilding.getBuildingNumber();


        String pictureName = buildingNumber + "_"
                + userName + "_"
                + dateFormat.format(date) + ".jpeg";

        // set up Firebase variables
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        StorageReference filePath = mStorageRef.child("routes").child(pictureName);

        // update Firebase building value
        this.mMapSnapshot = "buildings/" + pictureName;
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        String routeIndex = Integer.toString(selectedBuilding.
                getAllClassRooms().indexOf(this));

        mDatabaseRef.child("building").child(Integer.toString(selectedBuilding.getBFbId()))
                .child("mAllRoutes").child(routeIndex).setValue(map);

        // upload photo to Firebase Storage
        Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Photo saved to Firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
