package cse110.ucsd.team12wwr;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.firebase.Walk;
import cse110.ucsd.team12wwr.firebase.WalkDao;
import cse110.ucsd.team12wwr.ui.routes_tab.PersonalRoutesFragment;

import static android.content.Context.MODE_PRIVATE;

public class TeamRouteListAdapter extends ArrayAdapter<Route> {

    private static final String TAG = "RouteListAdapter";
    private Context context;
    int resource;
    SharedPreferences emailPrefs;
    String email;
    TextView textViewPrevWalked;

    public TeamRouteListAdapter(Context context, int resource, ArrayList<Route> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

        emailPrefs = context.getSharedPreferences("USER_ID", MODE_PRIVATE);
        email = emailPrefs.getString("EMAIL_ID", null);
        Log.d(TAG, "RouteListAdapter: Now adapting each team route to list item format");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String routeName = getItem(position).name;

        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource, parent, false);

        TextView textViewRouteName = (TextView) convertView.findViewById(R.id.route_name);
        textViewPrevWalked = (TextView) convertView.findViewById(R.id.previously_walked);

        textViewRouteName.setText(routeName);
        Log.d(TAG, "getView: Setting the route name to : " + routeName);
        ColorGenerator generator = ColorGenerator.MATERIAL;

        TextDrawable.IBuilder builder = TextDrawable.builder()
                                                    .beginConfig()
                                                    .bold()
                                                    .toUpperCase()
                                    //                        .withBorder(4)
                                                    .endConfig()
                                                    .round();

//        WalkDao dao = DaoFactory.getWalkDao();
//        View finalConvertView = convertView;
        Log.d(TAG, "getView: Finding the route by the name");
        WalkDao dao = DaoFactory.getWalkDao();
        View finalConvertView = convertView;
        dao.findByRouteName(routeName, task -> {
            if (task.isSuccessful()) {
                boolean hasWalk = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    hasWalk = true;
                }

                if (hasWalk) {
                    textViewPrevWalked.setVisibility(View.VISIBLE);
                }

                Log.i(TAG, "getView: Walk for " + routeName + " has been walked on before: " + hasWalk);

//        dao.findByRouteName(routeName, task -> {
//            if (task.isSuccessful()) {
//                boolean hasWalk = false;
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Walk walk = document.toObject(Walk.class);
////                    if (walk.userID != null && walk.userID.equals(email)) {
//                        hasWalk = true;
////                    }
//                }
//                Log.e("Limit", "Rendering route " + routeName + ", hasWalk is " + hasWalk);
//
//                if (hasWalk) {
//                    textViewPrevWalked.setVisibility(View.VISIBLE);
//                }

                RouteDao routeDao = DaoFactory.getRouteDao();
                routeDao.findName(routeName, task3 -> {
                    Log.d(TAG, "getView: Loading the route under name: " + routeName);
                    if ( task3.isSuccessful() ) {
                        Route route = null;
                        for ( QueryDocumentSnapshot document : task3.getResult() ) {
                            route = document.toObject(Route.class);
                        }

                        UserDao userDao = DaoFactory.getUserDao();
                        userDao.findUserByID(route.userID, task1 -> {
                            if ( task1.isSuccessful()) {
                                User user = null;
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    user = document.toObject(User.class);
                                    Log.d(TAG, "getView: UserID for route [" + routeName + "] is: " + user);
                                }
                                String memberName = user.firstName + " " + user.lastName;
                                Log.d(TAG, "getView: name of user is " + memberName);
                                int color = generator.getColor(memberName);

                                String initials = String.valueOf(user.firstName.charAt(0)) +
                                        String.valueOf(user.lastName.charAt(0));
                                Log.d(TAG, "getView: Initials are : " + initials);

                                TextDrawable iconDrawable = builder.build(initials, color);

                                ImageView icon = finalConvertView.findViewById(R.id.route_icon);
                                icon.setImageDrawable(iconDrawable);
                                Log.d(TAG, "getView: icon for route: " + routeName + " is now set");

                            }
                        });
                    }
                });
            }
        });

        return convertView;
    }
}
