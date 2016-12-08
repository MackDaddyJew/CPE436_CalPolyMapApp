package edu.calpoly.mjew.cpe436_calpolymapapp;

import java.util.ArrayList;

/**
 * Created by mackenzie on 11/29/16.
 */
public class ClassRoom implements Comparable
{
    private String mRoomNumber;
    private String mRoomDescription; //the string description. We may want in the future to change this to multiple strings.

    private transient ArrayList<String> mAllClassPhotos;

    public ClassRoom(){
        mRoomNumber = "";
        mRoomDescription = "";
    }

    public ClassRoom(String cNumber, String cDescription) {
        mRoomNumber = cNumber;
        mRoomDescription = cDescription;
    }

    public String getCRoomNumber() { return mRoomNumber; }
    public String getCRoomDescrip() { return mRoomDescription; }

    public void setCRoomNumber(String cNum) { mRoomNumber = cNum; }        // TODO: room numbers need to be strings for 282B cases
    public void setCRoomDescrip(String cRoomDescrip) { mRoomDescription = cRoomDescrip; }


    // used to sort list of ClassRooms in Building class
    @Override
    public int compareTo(Object cCompare)
    {
        ClassRoom cCom = (ClassRoom) cCompare;
        return  this.mRoomNumber.compareTo(cCom.getCRoomNumber());
    }

    @Override
    public String toString()
    {
        return("Room Number: " + this.mRoomNumber + ", "
                + "Description: " + this.mRoomDescription);
    }
}
