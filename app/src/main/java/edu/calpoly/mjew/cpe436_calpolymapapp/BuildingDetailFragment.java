package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


/**
 * Created by mackenzie on 11/24/16.
 */
public class BuildingDetailFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTextView;
    String buildingName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_building_detail, container, false);
        if (container == null)
            Log.d("onCreateView: ", "attaching fragment. Container is: null");
        else
            Log.d("onCreateView: ", "attaching fragment. Container is: there");
        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.fragment_building_detail, container, false);
        //((TextView)ll.findViewById(R.id.buildingDetailText)).setText("Is it working?");

        buildingName = getArguments().getString("BuildingName");

        FloatingActionButton fab = (FloatingActionButton) ll.findViewById(R.id.buildingDetailFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabSelect = new Intent(getActivity().getApplicationContext(), fabObtions.class);
                fabSelect.putExtra("BuildingName", buildingName);
                startActivity(fabSelect);
            }
        });


        mImageView = (ImageView) ll.findViewById(R.id.buildingDetailImage);
        mTextView = (TextView) ll.findViewById(R.id.buildingDetailText);

        // Wait for downloading the image
        try {
            java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Uri mUri = Uri.parse(getArguments().getString("imageUri"));

        mImageView.setImageURI(mUri);
        mTextView.setText(buildingName);

        return ll;
    }
}
