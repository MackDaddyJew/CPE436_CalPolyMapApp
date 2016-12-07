package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.ClipData;
import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mackenzie on 11/29/16.
 */
public class Building
{
    private String mBuildingNumber;
    private String mBuildingName;
    private String mBuildingDescription;
    private transient LatLng mBuildingCenter;           // TODO change from transient as they are implemented

    private transient ArrayList<ClassRoom> mAllClassRooms;
    private transient ArrayList<Route> mAllRoutes;
    private ArrayList<String> mAllBuildingPhotos;           // contains String Paths
    // if we add an official photo, don't want to have to search for it everytime


    public Building()
    {
        mBuildingNumber = "";
        mBuildingName = "";
        mBuildingDescription = "";
        mBuildingCenter = null; //bCenter;

        mAllClassRooms = new ArrayList<>();
        mAllBuildingPhotos = new ArrayList<>();
        mAllRoutes = new ArrayList<>();
    }

    public Building(String bNumber, String bName, String bDescription) //, LatLng bCenter) TODO: add this param later
    {
        mBuildingNumber = bNumber;
        mBuildingName = bName;
        mBuildingDescription = bDescription;
        mBuildingCenter = null; //bCenter;

        mAllClassRooms = new ArrayList<>();
        mAllBuildingPhotos = new ArrayList<>();
        mAllRoutes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return("Building Number: " + this.mBuildingNumber + ", "
                + "Building Name: " + this.mBuildingName + ", "
                + "Description: " + this.mBuildingDescription + ", ");

        // TODO add rest of fields to toString
    }

    // initialize database with buildings
    //      only ever need to call this once
    // TODO: maybe move into a BuildingHandler class?
    static public void InitializeAllBuildings(String[] allBuildingNames)
    {
        ArrayList<Building> allBuildings = new ArrayList<>();
        Building newBuild;
        String bName, bNumber, bDescription;

        int i = 0;  // start at 1 because 0 is a filler in spinner object

        // FIREBASE AND JSON
        Gson gson = new Gson();
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map;


        for(String s : allBuildingNames)
        {
            // skip first entry
            if(i != 0) {
                bNumber = s.split(" - ")[0];
                bName = s.split(" - ")[1];
                bDescription = "TO FILL IN";        // TODO - access another resource page and pass that in as another string array

                newBuild = new Building(bNumber, bName, bDescription);
                //allBuildings.add(newBuild);

                // initialize empty arrays in Firebase
                String imageName = "buildings/building_006.jpeg";   // use image name and put into mStorageRef.child(imageName);
                newBuild.mAllBuildingPhotos.add(0, imageName);

                String myJson = gson.toJson(newBuild);

                map = new Gson().fromJson(myJson,
                        new TypeToken<HashMap<String, Object>>() {
                        }.getType());

                mDatabaseRef.child("building").child(Integer.toString(i)).setValue(map);
            }
            i++;
        }
    }

    // basic information setters
    public void setBNum(String bNum)
    {
        mBuildingNumber = bNum;
    }
    public void setBName(String bName)
    {
        mBuildingName = bName;
    }
    public void setBDescription(String bDescription)
    {
        mBuildingDescription = bDescription;
    }
    public void setBPhotoList(ArrayList<String> bPhotoList) { mAllBuildingPhotos = bPhotoList; }

    // basic information return functions
    public String getBuildingNumber() { return mBuildingNumber; }
    public String getBuildingName() { return mBuildingName; }
    public String getBuildingDescription(){ return mBuildingDescription; }
    public LatLng getBuildingCenter(){ return mBuildingCenter; }
    public ArrayList<String> getAllBuildingPhotos()
    {
        return mAllBuildingPhotos;
    }



 // CLASSROOM RELATED FUNCTIONS

    //Searches all classrooms that have been added to the ArrayList for a matching room number to the given "roomNumber" parameter
    //will return null if the room number is not found;
    public ClassRoom getClassRoom(int roomNumber)
    {
        int size = mAllClassRooms.size();
        for(int i = 0; i < size; i++)
            if(mAllClassRooms.get(i).getRoomNumber() == roomNumber)
                return mAllClassRooms.get(i);
        return null;
    }

    // for use with Grid function
    public ArrayList<ClassRoom> getAllClassRooms()
    {
        return mAllClassRooms;
    }

    public void addClassRoom(int roomNumber, String cDescription)
    {
        ClassRoom createdClassRoom = new ClassRoom(roomNumber, cDescription);
        mAllClassRooms.add(createdClassRoom);
        Collections.sort(mAllClassRooms);
    }

 // ROUTE RELATED FUNCTIONS


 // PHOTO RELATED FUNCTIONS


    //public String getBuildingPhoto

}
