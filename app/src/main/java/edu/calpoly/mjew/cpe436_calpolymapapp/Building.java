package edu.calpoly.mjew.cpe436_calpolymapapp;

import java.util.ArrayList;

/**
 * Created by mackenzie on 11/29/16.
 */
public class Building
{
    private int mBuildingNumber;
    private ArrayList<ClassRoom> mAllClassRooms;

    public Building(int number)
    {
        mBuildingNumber = number;
        mAllClassRooms = new ArrayList<ClassRoom>();
    }

    public int getBuildingNumber() { return mBuildingNumber; }

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
}
