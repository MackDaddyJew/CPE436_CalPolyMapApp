package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class ClassroomSelect extends AppCompatActivity {

    String buildingName;
    private DatabaseReference mDatabaseRef;

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

        final Random rand = new Random();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addClassroom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if user is authenticated, allow classroom addition
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {

                    // write test - FIREBASE AND JSON
                    /*Gson gson = new Gson();
                    ClassRoom testClass = new ClassRoom(56, "this is a test class");
                    String myJson = gson.toJson(testClass);

                    Map<String, Object> map = new Gson().fromJson(myJson,
                            new TypeToken<HashMap<String, Object>>() {}.getType());
                    mDatabaseRef.child("classroom").child("0").setValue(map);*/

                    // write list text - FIREBASE AND JSON
                    /*int randNum = rand.nextInt(10) + 1;
                    final String randString = Integer.toString(randNum);
                    str_list.add(randString);

                    // comment out for read list test
                    //mDatabaseRef.child("strings").setValue(str_list);

                    // read list test - FIREBASE AND JSON
                    mDatabaseRef.child("strings").child(randString)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    updateTitle(randString + "->" + dataSnapshot.getValue().toString());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });*/


                    // initialize database with buildings
                    Resources res = getResources();
                    String[] allBuildings = res.getStringArray(R.array.buildings);

                    Building.InitializeAllBuildings(allBuildings);


                    // read test - FIREBASE AND JSON
                    /*mDatabaseRef.child("classroom").child("0").child("mDescription")
                           .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                updateTitle(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                           });*/
                }
                else
                {
                    // change to an option to log in with google?
                    Snackbar.make(view, "Must be logged in to add Classrooms", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
    }

    public void updateTitle(String newTitle)
    {
        setTitle(newTitle);
    }

}
