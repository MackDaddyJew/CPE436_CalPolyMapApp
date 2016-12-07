package edu.calpoly.mjew.cpe436_calpolymapapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mackenzie on 11/29/16.
 */
public class Building
{
    private String mBuildingName;
    private String mBuildingDescription;
    private LatLng mBuildingCenter;

    private ArrayList<ClassRoom> mAllClassRooms;
    private ArrayList<Route> mAllRoutes;
    private ArrayList<String> mAllBuildingPhotos;           // contains String Paths

    public Building(String bName, String bDescription, LatLng bCenter)
    {
        mBuildingName = bName;
        mBuildingDescription = bDescription;
        mBuildingCenter = bCenter;

        mAllClassRooms = new ArrayList<>();
        mAllBuildingPhotos = new ArrayList<>();
        mAllRoutes = new ArrayList<>();
    }

    // basic information return functions
    public String getFullBuildingName() { return mBuildingName; }
    public String getBuildingDescription(){ return mBuildingDescription; }
    public LatLng getBuildingCenter(){ return mBuildingCenter; }

    // mBuildingName is in always in format '[string] - [string]'
    //      the first string contains the 'Building number'
    //      the second string contains the 'Building name'
    //  The following two functions grab each respective part
    public String getBuildingNumber()
    {
        return mBuildingName.split(" - ")[0];
    }

    public String getBuildingName()
    {
        return mBuildingName.split(" - ")[1];
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
    public ArrayList<String> getAllBuildingPhotos()
    {
        return mAllBuildingPhotos;
    }

    //public String getBuildingPhoto

}
