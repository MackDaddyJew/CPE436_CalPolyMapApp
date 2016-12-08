package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class PhotoSelect extends AppCompatActivity {

    String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: update to read in string
        buildingName = selectedBuilding.getBuildingNumber() + " - "
                + selectedBuilding.getBuildingName();

        setTitle(buildingName + ": Photos");

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

}
