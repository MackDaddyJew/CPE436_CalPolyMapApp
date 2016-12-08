package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class ClassRoomAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_add);

        final EditText classNumView = (EditText) findViewById(R.id.editClassroomNumber);
        final EditText classDescripView = (EditText) findViewById(R.id.editClassroomDescrip);

        Button addClassPhoto = (Button) findViewById(R.id.addClassroomPicButton);

        Button confirmChange = (Button) findViewById(R.id.confirmAddButton);
        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cNum = classNumView.getText().toString();
                String cDescrip = classDescripView.getText().toString();

                if(cNum.trim().matches(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a Classroom number.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    selectedBuilding.addNewClassRoom(cNum, cDescrip);
                    finish();
                }
            }
        });



        Button cancelChange = (Button) findViewById(R.id.cancelAddButton);
        cancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
