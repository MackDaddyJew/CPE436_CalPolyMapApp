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

        // Firebase initialization
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // widget initialization
        mImageView = (ImageView) ll.findViewById(R.id.buildingDetailImage);
        mTextView = (TextView) ll.findViewById(R.id.buildingDetailText);

        buildingIndex = getArguments().getInt("BuildingIndex");

        // grab building info from Firebase
        String buildingIndexStr = Integer.toString(buildingIndex);
        final Building buildingInst = new Building();

        mDatabaseRef.child("building").child(buildingIndexStr)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // TODO

                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};

                        buildingInst.setBName(dataSnapshot.child("mBuildingName").getValue().toString());
                        buildingInst.setBNum(dataSnapshot.child("mBuildingNumber").getValue().toString());
                        buildingInst.setBDescription(dataSnapshot.child("mBuildingDescription").getValue().toString());

                        // need a way to store an empty array in firebase
                        buildingInst.setBPhotoList(dataSnapshot.child("mAllBuildingPhotos").getValue(t));

                        String emptyImage = "";

                        // TODO: Have this be if there is more than one photo in array
                        StorageReference imageRef = mStorageRef.child(buildingInst.getAllBuildingPhotos().get(0));  // TODO: change to .get(1)

                        // BLESS YOU GLIDE
                        Glide.with(getContext())
                                .using(new FirebaseImageLoader())
                                .load(imageRef)
                                .into(mImageView);

                        if(buildingInst.getAllBuildingPhotos().size() > 1)
                        {

                        }
                        else {
                            // TODO: have this be if there is only one photo in array
                            //       have that first photo be a placeholder image

                            // put some place holder text
                            //  should say something like - "be the first to post a picture of this building!"
                            emptyImage = "Be the first to add a picture for this building!";
                        }

                        mTextView.setText(buildingInst.getBuildingName() + " ("
                                + buildingInst.getBuildingNumber() + ")" + " \n"
                                + " \n" + emptyImage);

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
                //fabSelect.putExtra("BuildingIndex", buildingIndex);

                //fabSelect.putExtras()
                startActivity(fabSelect);
            }
        });


        return ll;
    }
}
