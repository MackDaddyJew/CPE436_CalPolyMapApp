package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by mackenzie on 11/22/16.
 * this class is meant to be the object representation of a route. It should contain all waypoints that
 * make up the route and any meta data associated with it.
 */
public class Route implements Parcelable
{
    private String mCreatorName = "Sample Name";
    private ArrayList<LatLng> mWaypoints;
    private transient ArrayList<Instruction> mInstructions;
    //need some collection for images? or perhaps create a new instructions class that contains a
    // single image and a string, and mInstructions can be a collection of those.

    public static Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>()
    {
        @Override
        public Route[] newArray(int size)
        {
            return new Route[0];
        }

        @Override
        public Route createFromParcel(Parcel source)
        {
            Route rtn = new Route();
            int[] intRead = new int[2];
            source.readIntArray(intRead);
            double[] doubleRead = new double[intRead[0]];
            source.readDoubleArray(doubleRead);
            String[] stringRead = new String[intRead[1]];
            source.readStringArray(stringRead);

            for(int i = 0; i < doubleRead.length; i++)
                rtn.addWaypoint(new LatLng(doubleRead[i*2], doubleRead[i*2 + 1]));
            for(int i = 0; i < stringRead.length; i++)
                rtn.addInstruction(new Instruction(stringRead[i], null));
            return rtn;
        }
    };

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

    public String getCreatorName() {return mCreatorName;}

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeIntArray(new int[] {mWaypoints.size()*2, mInstructions.size()});
        double doubleWrite[] = new double[mWaypoints.size()*2];
        for(int i = 0; i < mWaypoints.size(); i++)
        {
            doubleWrite[i*2] = mWaypoints.get(i).latitude;
            doubleWrite[i*2 + 1] = mWaypoints.get(i).longitude;
        }
        dest.writeDoubleArray(doubleWrite);
        String stringWrite[] = new String[mInstructions.size()];
        for(int i = 0; i < mInstructions.size(); i++)
        {
            stringWrite[i] = mInstructions.get(i).getText();
        }
        dest.writeStringArray(stringWrite);
    }
}
