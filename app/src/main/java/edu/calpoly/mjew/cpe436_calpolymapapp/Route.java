package edu.calpoly.mjew.cpe436_calpolymapapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by mackenzie on 11/22/16.
 * this class is meant to be the object representation of a route. It should contain all waypoints that
 * make up the route and any meta data associated with it.
 */
public class Route
{
    private ArrayList<LatLng> mWaypoints;
    private transient ArrayList<Instruction> mInstructions;
    //need some collection for images? or perhaps create a new instructions class that contains a
    // single image and a string, and mInstructions can be a collection of those.

    public Route()
    {
        mWaypoints = new ArrayList<LatLng>();
        mInstructions = new ArrayList<Instruction>();
    }

    public ArrayList<LatLng> getWaypoints() { return mWaypoints; }

    public ArrayList<Instruction> getInstructions() { return mInstructions; }

    public void addWaypoint(LatLng wp)
    {
        mWaypoints.add(wp);
    }

    public void addInstruction(Instruction s)
    {
        mInstructions.add(s);
    }

    public void replaceWaypoints(ArrayList<LatLng> newWp)
    {
        mWaypoints = newWp;
    }

    public void replaceInstructions(ArrayList<Instruction> newIns)
    {
        mInstructions = newIns;
    }


}
