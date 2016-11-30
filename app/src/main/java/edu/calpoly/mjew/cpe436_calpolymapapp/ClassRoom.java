package edu.calpoly.mjew.cpe436_calpolymapapp;

/**
 * Created by mackenzie on 11/29/16.
 */
public class ClassRoom
{
    private int mRoomNumber;
    public String mDescription; //the string description. We may want in the future to change this to multiple strings.

    public ClassRoom(int number) {mRoomNumber = number; }

    public int getRoomNumber() { return mRoomNumber; }
}
