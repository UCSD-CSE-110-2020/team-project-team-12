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

import cse110.ucsd.team12wwr.firebase.FirebaseRouteDao;
import cse110.ucsd.team12wwr.firebase.FirebaseUserDao;
import cse110.ucsd.team12wwr.firebase.FirebaseWalkDao;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.ui.routes_tab.PersonalRoutesFragment;

import static android.content.Context.MODE_PRIVATE;

public class RouteListAdapter extends ArrayAdapter<Route> {

    private static final String TAG = "RouteListAdapter";
    private Context context;
    int resource;
    SharedPreferences emailPrefs;
    String email;

    public RouteListAdapter(Context context, int resource, ArrayList<Route> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        emailPrefs = context.getSharedPreferences("USER_ID", MODE_PRIVATE);
        email = emailPrefs.getString("EMAIL_ID", null);
        email = "jane@gmail.com";
        Log.d(TAG, "RouteListAdapter: Now adapting each team route to list item format");
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
        Log.d(TAG, "getView: Setting the route name to : " + routeName);
        ColorGenerator generator = ColorGenerator.MATERIAL;

        TextDrawable.IBuilder builder = TextDrawable.builder()
                                                    .beginConfig()
                                                    .bold()
                                                    .toUpperCase()
                                    //                        .withBorder(4)
                                                    .endConfig()
                                                    .round();

        FirebaseWalkDao dao = new FirebaseWalkDao();
        View finalConvertView = convertView;
        Log.d(TAG, "getView: Finding the route by the name");
        dao.findByRouteName(routeName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean hasWalk = false;
                Log.d(TAG, "getView: Walk has been walked on: " + hasWalk);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    hasWalk = true;
                }

                if (!hasWalk) {
                    textViewPrevWalked.setVisibility(View.INVISIBLE);
                }

                List<Route> foundRoute = new ArrayList<>();
                FirebaseRouteDao routeDao = new FirebaseRouteDao();
                routeDao.findName(routeName).addOnCompleteListener(task3 -> {
                    Log.d(TAG, "getView: Loading the route under name: " + routeName);
                    if ( task3.isSuccessful() ) {
                        for ( QueryDocumentSnapshot document : task3.getResult() ) {
                            foundRoute.add(document.toObject(Route.class));
                        }

                        List<User> userList = new ArrayList<>();
                        FirebaseUserDao userDao = new FirebaseUserDao();
                        userDao.findUserByID(foundRoute.get(0).userID).addOnCompleteListener(task1 -> {
                            if ( task1.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    userList.add(document.toObject(User.class));
                                    Log.d(TAG, "getView: UserID for route [" + routeName + "] is: " + userList.get(0));
                                }
                                String memberName = userList.get(0).firstName + " " + userList.get(0).lastName;
                                Log.d(TAG, "getView: name of user is " + memberName);
                                int color = generator.getColor(memberName);

                                String initials = String.valueOf(userList.get(0).firstName.charAt(0)) +
                                        String.valueOf(userList.get(0).lastName.charAt(0));
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
