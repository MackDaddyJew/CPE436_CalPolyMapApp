package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ClassroomSelect extends AppCompatActivity {

    String buildingName;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        buildingName = in.getStringExtra("BuildingName");

        setTitle(buildingName + ":\nClassrooms");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addClassroom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Gson gson = new Gson();
                    ClassRoom testClass = new ClassRoom(56, "this is a test class");
                    //mDatabaseRef.child("classrooms").child(0)
                            //.child().setValue(gson.toJson(testClass));
                    String myJson = gson.toJson(testClass);
                    Map<String, Object> map = new Gson().fromJson(myJson,
                            new TypeToken<HashMap<String, Object>>() {}.getType());
                    mDatabaseRef.child("classroom").child("0").setValue(map);
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
