package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


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
        //((TextView)ll.findViewById(R.id.buildingDetailText)).setText("Is it working?");

        mImageView = (ImageView) ll.findViewById(R.id.buildingDetailImage);
        mTextView = (TextView) ll.findViewById(R.id.buildingDetailText);

        buildingIndex = getArguments().getInt("BuildingIndex");

        // grab building info from Firebase
        String buildingIndexStr = Integer.toString(buildingIndex);
        final Building buildingInst = new Building();

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.child("building").child(buildingIndexStr)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // TODO

                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};

                        buildingInst.setBName(dataSnapshot.child("mBuildingName").getValue().toString());
                        buildingInst.setBNum(dataSnapshot.child("mBuildingNumber").getValue().toString());
                        buildingInst.setBDescription(dataSnapshot.child("mBuildingDescription").getValue().toString());
                        buildingInst.setBPhotoList(dataSnapshot.child("mAllBuildingPhotos").getValue(t));

                        mTextView.setText(buildingInst.getBuildingName() + "(" + buildingInst.getBuildingNumber() + ")");



                        // need to format array list of strings for photos
                        //      just load the whole ArrayList, then .get(0) for first element

                        //ArrayList<String> importList = new ArrayList<>();

                        // TODO: need part here to set values of 'importList'

                        //buildingInst.setBPhotoList(importList);


                        if(!buildingInst.getAllBuildingPhotos().isEmpty())
                        {
                            //String imgName =
                            StorageReference imageRef = mStorageRef.child(buildingInst.getAllBuildingPhotos().get(0));
                            Log.v("LOOOOOOOOOOK", imageRef.toString());
                            //Uri imageUri = Uri.fromFile(new File(imageRef.toString()));

                            File localFile = null;
                            try {
                                localFile = File.createTempFile("images", ".jpeg");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            imageRef.getFile(localFile);
                            Uri imageUri = Uri.fromFile(localFile);

                            mImageView.setImageURI(imageUri);
                        }
                        else {
                            // put some place holder text
                            //  should say something like - "be the first to post a picture of this building!"
                        }



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
                fabSelect.putExtra("BuildingIndex", buildingIndex);
                startActivity(fabSelect);
            }
        });


        return ll;
    }
}
