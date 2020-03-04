package cse110.ucsd.team12wwr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.WWRDatabase;

public class RouteListAdapter extends ArrayAdapter<Route> {

    private static final String TAG = "ListAdapter";
    private Context context;
    int resource;
    public boolean bool = true;

    public RouteListAdapter(Context context, int resource, ArrayList<Route> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String routeName = getItem(position).name;

        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource, parent, false);

        TextView textViewRouteName = (TextView) convertView.findViewById(R.id.route_name);
        TextView textViewPrevWalked = (TextView) convertView.findViewById(R.id.previously_walked);

        textViewRouteName.setText(routeName);
        if (!bool) {
            textViewPrevWalked.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

}
