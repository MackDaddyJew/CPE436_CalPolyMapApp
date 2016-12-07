package edu.calpoly.mjew.cpe436_calpolymapapp;

import java.util.ArrayList;

/**
 * Created by mackenzie on 11/29/16.
 */
public class ClassRoom implements Comparable
{
    private int mRoomNumber;
    public String mDescription; //the string description. We may want in the future to change this to multiple strings.

    private ArrayList<String> mAllClassPhotos;

    public ClassRoom(int cNumber, String cDescription) {
        mRoomNumber = cNumber;
        mDescription = cDescription;
    }

    public int getRoomNumber() { return mRoomNumber; }


    // used to sort list of ClassRooms in Building class
    @Override
    public int compareTo(Object cCompare)
    {
        int compareRoom = ((ClassRoom)cCompare).getRoomNumber();
        return this.mRoomNumber - compareRoom;
    }

    @Override
    public String toString()
    {
        return("Room Number: " + this.mRoomNumber + ", "
                + "Description: " + this.mDescription);
    }
}
