package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class ClassroomSelect extends AppCompatActivity {

    String buildingName;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buildingName = selectedBuilding.getBuildingNumber() + " - "
                + selectedBuilding.getBuildingName();
        setTitle(buildingName + ": Classrooms");


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<String> str_list = new ArrayList<>();

        reloadPage();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addClassroom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if user is authenticated, allow classroom addition
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    // initialize database with buildings
                    if(false) {
                        Resources res = getResources();
                        String[] allBuildings = res.getStringArray(R.array.buildings);

                        Building.InitializeAllBuildings(allBuildings);
                    }
                    if(true) {
                        Intent addClass = new Intent(getApplicationContext(), ClassRoomAdd.class);
                        //addClass.putExtra("className", buildingName);
                        startActivity(addClass);
                    }
                }
                else
                {
                    // change to an option to log in with google?
                    Snackbar.make(view, "Must be logged in to add Classrooms", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });*/
    }

    public void reloadPage(){
        List<String> listClasses = selectedBuilding.getAllClassRoomsNames();
        String[] arrClass = listClasses.toArray(new String[listClasses.size()]);

        gridView = (GridView) findViewById(R.id.ClassroomGrid);
        gridView.setAdapter(new ImageListAdapter(ClassroomSelect.this, arrClass));

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
    }

    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private String[] classList;
        private String[] classPhotoList;

        public ImageListAdapter(Context context, String[] classList){
            super(context, R.layout.grid_item_photo, classList);

            this.context = context;
            this.classList = classList;

            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(null == convertView){
                convertView = inflater.inflate(R.layout.grid_item_photo, parent, false);
            }

            ClassRoom cr = selectedBuilding.getAllClassRooms().get(position);
            ImageView userPosted  = (ImageView) convertView.findViewById(R.id.userPostPhoto);
            TextView photoCred = (TextView) convertView.findViewById(R.id.photoCred);

            int index = (cr.getCAllRoomPhotos().size() > 1 ? 1 : 0);

            String imagePath = cr.getCRoomPhotoByIndex(index);
            StorageReference imageRef = mStorageRef.child(imagePath);

            // BLESS YOU GLIDE
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(userPosted);

            String userName;
            if(position > 0){
                userName = classList[position];
            } else {
                userName = "Add New Classroom";
            }

            photoCred.setText(userName);
            userPosted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == 0){
                        // if user is authenticated, allow classroom addition
                        if(FirebaseAuth.getInstance().getCurrentUser() != null)
                        {
                            // initialize database with buildings
                            if(false) {
                                Resources res = getResources();
                                String[] allBuildings = res.getStringArray(R.array.buildings);

                                Building.InitializeAllBuildings(allBuildings);
                            }
                            if(true) {
                                Intent addClass = new Intent(getApplicationContext(), ClassRoomAdd.class);
                                //addClass.putExtra("className", buildingName);
                                startActivity(addClass);
                            }
                        }
                        else
                        {
                            // change to an option to log in with google?
                            Snackbar.make(view, "Must be logged in to add Classrooms", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
            });

            return convertView;
        }
    }
}
