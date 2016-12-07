package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by mackenzie on 12/1/16.
 */
public class RouteCreatorFragment extends Fragment
{
    private Button mSaveButton;
    private Button mResetButton;
    private CheckBox mFreezeCheckBox;
    private PolylineOptions routeToSave; //The final route to save all the waypoints to
    private ArrayList<LatLng> routeToMake; //a middle collection class to record waypoints. Editable.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        routeToSave = new PolylineOptions();
        routeToMake = new ArrayList<LatLng>();
        if (container == null)
            Log.d("onCreateView Route: ", "attaching fragment. Container is: null");
        else
            Log.d("onCreateView Route: ", "attaching fragment. Container is: there");
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_route_generation, container, false);
        mSaveButton = (Button)ll.findViewById(R.id.saveButton);
        mResetButton = (Button)ll.findViewById(R.id.resetButton);
        mFreezeCheckBox = (CheckBox)ll.findViewById(R.id.freezeCheckBox);
        return ll;
    }

    public void initRouteClickListeners(GoogleMap map)
    {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                routeToMake.add(latLng);
                routeToSave.add(latLng);
                ((MainMapsActivity)getActivity()).getGoogleMap().addPolyline(routeToSave);
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override //click listener will remove LatLng listeners from the ArrayList. Allows user to correct for mistakes
            public void onClick(View v)
            {
                Log.d("MACKENZIE: ", "Reset Button. Removing the following items from the ArrayList");
                if(routeToMake.size() > 0)
                {
                    Log.d("MACKENZIE: ", ((LatLng)routeToMake.remove(routeToMake.size()-1)).toString());
                }
                else
                    Log.d("MACKENZIE: ", "ArrayList is empty");
                ((MainMapsActivity)getActivity()).getGoogleMap().addPolyline(routeToSave);
            }
        });

        mFreezeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                    ((MainMapsActivity)getActivity()).lockMap();
                else
                    ((MainMapsActivity)getActivity()).unlockMap();
            }
        });
    }
}
