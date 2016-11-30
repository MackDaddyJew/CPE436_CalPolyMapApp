package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class fabObtions extends AppCompatActivity {

    String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_obtions);

        Intent in = getIntent();
        buildingName = in.getStringExtra("BuildingName");

        //setTitle(buildingName + " - Options");
        TextView title = (TextView) findViewById(R.id.buildingTitle);
        title.setText(buildingName + " - Options");

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


    }
}
