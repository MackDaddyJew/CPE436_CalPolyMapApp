package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class fabObtions extends AppCompatActivity {

    String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fab_obtions);

        buildingName = selectedBuilding.getBuildingNumber() + " - "
                + selectedBuilding.getBuildingName() + " \n - Options";

        //setTitle(buildingName + " - Options");
        TextView title = (TextView) findViewById(R.id.buildingTitle);
        title.setText(buildingName);

        Button navigate_button = (Button) findViewById(R.id.navButton);
        navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navIntent = new Intent(getApplicationContext(), RouteSelect.class);
                navIntent.putExtra("BuildingName", buildingName);
                startActivity(navIntent);
            }
        });

        Button photo_button = (Button) findViewById(R.id.picButton);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picPageIntent = new Intent(getApplicationContext(), PhotoSelect.class);
                picPageIntent.putExtra("BuildingName", buildingName);
                startActivity(picPageIntent);
            }
        });

        Button classroom_button = (Button) findViewById(R.id.classButton);
        classroom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classroomIntent = new Intent(getApplicationContext(), ClassroomSelect.class);
                classroomIntent.putExtra("BuildingName", buildingName);
                startActivity(classroomIntent);
            }
        });

        final Button route_button = (Button) findViewById(R.id.createRouteButton);
        route_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Intent routeCreatorIntent = new Intent(getApplicationContext(), MainMapsActivity.class);
                    routeCreatorIntent.putExtra("CONFIG", MainMapsActivity.CONFIG1);
                    startActivity(routeCreatorIntent);
                }
                else
                {
                    // change to an option to log in with google?
                    Snackbar.make(route_button, "Must be logged in to add Routes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });
    }
}
