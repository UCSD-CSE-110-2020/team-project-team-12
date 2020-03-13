package cse110.ucsd.team12wwr.teamlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.api.LogDescriptor;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.teamlist.TeamScreenRowItem;

public class TeamListAdapter extends BaseAdapter {

    private static final String TAG = "TeamListAdapter";
    Context context;
    List<TeamScreenRowItem> rowItems;
    TextDrawable.IBuilder builder;
    TextDrawable iconDrawable;
    String teamID;

    public TeamListAdapter(Context c, List<TeamScreenRowItem> list, String passedTeam) {
        this.context = c;
        this.rowItems = list;
        this.teamID = passedTeam;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rowItems.indexOf(rowItems.get(i));
    }

    public void updateItems(List<TeamScreenRowItem> newList) {
        rowItems = newList;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView initials;
        TextView member_name;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        LayoutInflater m = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if ( convertView == null ) {
            convertView = m.inflate(R.layout.team_list_item, null);
            holder = new ViewHolder();
            holder.initials = convertView.findViewById(R.id.member_icon);
            holder.member_name = convertView.findViewById(R.id.member_name);

            TeamScreenRowItem item = rowItems.get(i);

            Log.d(TAG, "getView: Retrieved row item and now setting icon and name into list");
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int pendingColor = Color.LTGRAY;
            
            // generate color based on a key (same key returns the same color), useful for list/grid views
            int color = generator.getColor(item.getMemberName());

            builder = TextDrawable.builder()
                    .beginConfig()
                        .bold()
                        .toUpperCase()
                    .endConfig()
                    .round();

            if ( !item.getTeamID().equals("") ) {
                Log.d(TAG, "getView: Team members: " + holder.member_name);
                Log.d(TAG, "getView: Color: " + color);
                iconDrawable = builder.build(item.getInitials(), color);
            } else {
                Log.d(TAG, "getView: Team member is pending");
                iconDrawable = builder.build(item.getInitials(), pendingColor);
            }

            holder.initials.setImageDrawable(iconDrawable);
            holder.member_name.setText(item.getMemberName());



            if (!item.getTeamID().equals(teamID) ) {
                holder.member_name.setTypeface(holder.member_name.getTypeface(), Typeface.ITALIC);
                holder.member_name.setTextColor(Color.parseColor("#808080"));
            }

            Log.d(TAG, "getView: Team List item is now set");

        }
        return convertView;
    }
}