package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by mackenzie on 12/9/16.
 */
public class RouteAdapter extends BaseAdapter
{
    private ArrayList<Route> data;

    public RouteAdapter(ArrayList<Route> d)
    {
        super();
        data = d;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Context c = parent.getContext();
        final int pos = position;
        if(convertView != null)
            ((TextView)convertView.findViewById(R.id.routeText)).setText(data.get(position).getCreatorName());
        else
        {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.route_icon, parent,false);
            ((TextView)convertView.findViewById(R.id.routeText)).setText(data.get(position).getCreatorName());
        }

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(c, MainMapsActivity.class);
                i.putExtra("Route", data.get(pos));
                i.putExtra("CONFIG", MainMapsActivity.CONFIG2);
                c.startActivity(i);
            }
        });
        Log.d("MACKENZIE: ", "RouteAdapter getView() method called");
        return convertView;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public Route getItem(int position)
    {
        return data.get(position);
    }
}
