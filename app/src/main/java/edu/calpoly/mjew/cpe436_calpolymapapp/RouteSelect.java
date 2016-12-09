package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class RouteSelect extends AppCompatActivity {

    String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buildingName = selectedBuilding.getBuildingNumber() + " - "
                + selectedBuilding.getBuildingName();

        setTitle(buildingName + ": Routes");

        Log.d("MACKENZIE: ", "size " + MainMapsActivity.selectedBuilding.getAllBuildingRoutes().size());
        ((GridView)findViewById(R.id.RouteGrid)).setAdapter(new RouteAdapter(MainMapsActivity.selectedBuilding.getAllBuildingRoutes()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addRoute);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
