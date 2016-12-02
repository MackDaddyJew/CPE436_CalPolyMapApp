package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by mackenzie on 12/1/16.
 */
public class RouteCreatorFragment extends Fragment
{
    private Button mSaveButton;
    private Button mResetButton;
    private CheckBox mFreezeCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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
}
