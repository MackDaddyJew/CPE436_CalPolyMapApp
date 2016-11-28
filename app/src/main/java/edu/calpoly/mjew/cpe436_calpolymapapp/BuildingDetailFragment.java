package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.icu.util.TimeUnit;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by mackenzie on 11/24/16.
 */
public class BuildingDetailFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_building_detail, container, false);
        if (container == null)
            Log.d("onCreateView: ", "attaching fragment. Container is: null");
        Log.d("onCreateView: ", "attaching fragment. Container is: there");
        FrameLayout fl = (FrameLayout) getActivity().findViewById(R.id.layout_2);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_building_detail, container, false);
        //((TextView)ll.findViewById(R.id.buildingDetailText)).setText("Is it working?");

        mImageView = (ImageView) ll.findViewById(R.id.buildingDetailImage);
        mTextView = (TextView) ll.findViewById(R.id.buildingDetailText);

        // Wait for downloading the image
        try {
            java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Uri mUri = Uri.parse(getArguments().getString("imageUri"));
        String s = getArguments().getString("imageString");
        mImageView.setImageURI(mUri);
        mTextView.setText(s);


        return ll;
    }
}
