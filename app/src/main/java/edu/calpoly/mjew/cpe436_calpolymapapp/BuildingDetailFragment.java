package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;


/**
 * Created by mackenzie on 11/24/16.
 */
public class BuildingDetailFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTextView;
    private int buildingIndex;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_building_detail, container, false);
        if (container == null)
            Log.d("onCreateView: ", "attaching fragment. Container is: null");
        else
            Log.d("onCreateView: ", "attaching fragment. Container is: there");
        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.fragment_building_detail, container, false);

        if(ll.findViewById(R.id.buildingDetailLayout) == null)
            Log.d("MACKENZIE: " , "Not using the Landscape mode file for fragment_building_detail");
        else
            Log.d("MACKENZIE: " , "Using the Landscape mode file for fragment_building_detail");

        // Firebase initialization
        initializeFirebase();

        // widget initialization
        mImageView = (ImageView) ll.findViewById(R.id.buildingDetailImage);
        mTextView = (TextView) ll.findViewById(R.id.buildingDetailText);

        buildingIndex = getArguments().getInt("BuildingIndex");

        // grab building info from Firebase
        final String buildingIndexStr = Integer.toString(buildingIndex);
        final Building buildingInst = new Building();
        buildingInst.setBFbId(buildingIndex);

        mDatabaseRef.child("building").child(buildingIndexStr)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // TODO

                        GenericTypeIndicator<ArrayList<String>> phType = new GenericTypeIndicator<ArrayList<String>>() {};
                        GenericTypeIndicator<ArrayList<ClassRoom>> crType = new GenericTypeIndicator<ArrayList<ClassRoom>>() {};
                        GenericTypeIndicator<ArrayList<Route>> rtType = new GenericTypeIndicator<ArrayList<Route>>() {};


                        buildingInst.setBName(dataSnapshot.child("mBuildingName").getValue().toString());
                        buildingInst.setBNum(dataSnapshot.child("mBuildingNumber").getValue().toString());
                        //buildingInst.setBDescription(dataSnapshot.child("mBuildingDescription").getValue().toString());

                        // need way to store empty array in firebase

                        // grab list of photos
                        buildingInst.setBPhotoList(dataSnapshot.child("mAllBuildingPhotos").getValue(phType));
                        // grab list of classrooms
                        buildingInst.setBClassroomList(dataSnapshot.child("mAllClassRooms").getValue(crType));
                        int loopStop =  buildingInst.getNumberOfClassRooms();

                        for(int i = 0; i < loopStop; i++)
                        {
                            // initialize list of classes with data from firebase
                            String classNum = dataSnapshot.child("mAllClassRooms").child(Integer.toString(i))
                                    .child("mRoomNumber").getValue().toString();

                            String classDescrip = dataSnapshot.child("mAllClassRooms").child(Integer.toString(i))
                                    .child("mRoomDescription").getValue().toString();

                            buildingInst.getAllClassRooms().get(i).setCRoomNumber(classNum);
                            buildingInst.getAllClassRooms().get(i).setCRoomDescrip(classDescrip);

                        }




                        // grab list of routes
                        //buildingInst.setBRouteList(dataSnapshot.child("mAllRoutes").getValue(rtType));


                        String emptyImage = "";

                        // TODO: Have this be if there is more than one photo in array
                        StorageReference imageRef;;



                        if(buildingInst.getAllBuildingPhotos().size() == 1)
                        {
                            // first photo is a placeholder image
                            imageRef = mStorageRef.child(buildingInst.getAllBuildingPhotos().get(0));
                            emptyImage = "Be the first to add a picture for this building!";
                        }
                        else {
                            imageRef = mStorageRef.child(buildingInst.getAllBuildingPhotos().get(1));
                        }

                        // BLESS YOU GLIDE
                        Glide.with(getContext())
                                .using(new FirebaseImageLoader())
                                .load(imageRef)
                                .into(mImageView);

                        mTextView.setText(buildingInst.getBuildingName() + " ("
                                + buildingInst.getBuildingNumber() + ")" + " \n"
                                + " \n" + emptyImage);

                        // set the current selected building
                        selectedBuilding = buildingInst;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        // assign FAB and associated function
        FloatingActionButton fab = (FloatingActionButton) ll.findViewById(R.id.buildingDetailFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabSelect = new Intent(getActivity().getApplicationContext(), fabObtions.class);
                startActivity(fabSelect);
            }
        });


        return ll;
    }

    private void initializeFirebase(){
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
