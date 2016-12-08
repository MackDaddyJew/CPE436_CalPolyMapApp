package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.List;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class PhotoSelect extends AppCompatActivity {

    private String buildingName;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get and set info from selectedBuilding object
        buildingName = selectedBuilding.getBuildingNumber() + " - "
                + selectedBuilding.getBuildingName();
        setTitle(buildingName + ": Photos");

        List<String> listPics = selectedBuilding.getAllBuildingPhotos();
        String[] arrPics = listPics.toArray(new String[listPics.size()]);
        Log.v("LOOOOOOOOOK","number of items: " + listPics.size());


        // set up adapter for gridView
        GridView gridView = (GridView) findViewById(R.id.photoGrid);
        gridView.setAdapter(new ImageListAdapter(PhotoSelect.this, arrPics));


        // initialize Firebase Storage reference
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addPhoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Intent fabAdd = new Intent(getApplicationContext(), PhotoAdd.class);
                    fabAdd.putExtra("BuildingName", buildingName);
                    startActivity(fabAdd);
                }
                else
                {
                    // change to an option to log in with google?
                    Snackbar.make(view, "Must be logged in to add Photos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            }
        });
    }

    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private String[] imageList;

        public ImageListAdapter(Context context, String[] imageList){
            super(context, R.layout.grid_item_photo, imageList);

            this.context = context;
            this.imageList = imageList;

            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = inflater.inflate(R.layout.grid_item_photo, parent, false);
            }

            StorageReference imageRef = mStorageRef.child(imageList[position]);

            ImageView userPosted  = (ImageView) convertView.findViewById(R.id.userPostPhoto);
            TextView photoCred = (TextView) convertView.findViewById(R.id.photoCred);

            // BLESS YOU GLIDE
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(userPosted);

            photoCred.setText("Anonymous");

            return convertView;
        }
    }

}
