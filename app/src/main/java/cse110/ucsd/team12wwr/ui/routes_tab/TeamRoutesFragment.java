package cse110.ucsd.team12wwr.ui.routes_tab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.RouteDetailsPage;
import cse110.ucsd.team12wwr.TeamRouteListAdapter;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class TeamRoutesFragment extends Fragment {

    private static final String TAG = "TeamRoutesFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences emailPref;
    String userEmail;
    ListView listView;

    private PageViewModel pageViewModel;

    public static TeamRoutesFragment newInstance(int index) {
        TeamRoutesFragment fragment = new TeamRoutesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Entered Team Routes Tab!");

        Log.i(TAG, "onCreate: Getting current user's email from SharedPreferences");
        emailPref = this.getActivity().getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = emailPref.getString("EMAIL_ID", null);

        Log.d(TAG, "onCreate: Email for current user: " + userEmail);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_routes, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: Loading team routes...");
        initializeUpdateListener(view);
    }

    private void initializeUpdateListener(View view) {
        RouteDao dao = DaoFactory.getRouteDao();
        dao.listenForChanges((newChatSnapshot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }

            if (newChatSnapshot != null && !newChatSnapshot.isEmpty()) {
                renderRoutesList(view);
            }
        });
    }

    public void launchRoutesDetailsPage(String routeName) {
        Log.d(TAG, "launchRouteDetailsPage: launching the route details page");
        Intent intent = new Intent(getActivity(), RouteDetailsPage.class);
        intent.putExtra("name", routeName);
        intent.putExtra("fromTeam", true);
        startActivity(intent);
    }

    private void renderRoutesList(View view) {
        Log.d(TAG, "renderRoutesList: Now rendering list of routes for the team");
        UserDao dao = DaoFactory.getUserDao();
        dao.findUserByID(userEmail,task -> {
           if (task.isSuccessful()) {
               User u = null;
               for (QueryDocumentSnapshot document : task.getResult()) {
                   u = document.toObject(User.class);
                   Log.i(TAG, "renderRoutesList: Found user");
               }

               if (u != null) {
                   dao.findUsersByTeam(u.teamID, task1 -> {
                       if ( task1.isSuccessful() ) {
                           Log.d(TAG, "renderRoutesList: Getting all the team members");
                           List<User> userList = new ArrayList<>();
                           for (QueryDocumentSnapshot document : task1.getResult()) {
                               userList.add(document.toObject(User.class));
                           }

                           Log.i(TAG, "renderRoutesList: Now retrieving all routes");
                           RouteDao routeDao = DaoFactory.getRouteDao();
                           routeDao.retrieveAllRoutes(task2 -> {
                               if ( task2.isSuccessful()) {
                                   List<Route> allRoutes = new ArrayList<>();
                                   for (QueryDocumentSnapshot document : task2.getResult()) {
                                       allRoutes.add(document.toObject(Route.class));
                                   }

                                   List<Route>routeList = new ArrayList<>();
                                   Log.i(TAG, "renderRoutesList: Looking through all the routes to locate team routes");
                                   for (User user : userList ) {
                                       String userID = user.userID;
                                       for ( Route route : allRoutes ) {
                                           if ( route.userID != null ) {
                                               if (route.userID.equals(userID) && !route.userID.equals(userEmail)) {
                                                   routeList.add(route);
                                               }
                                           }
                                       }
                                   }

                                   listView = view.findViewById(R.id.teams_routes_list);
                                   TeamRouteListAdapter teamRouteListAdapter = new TeamRouteListAdapter(getActivity(), R.layout.route_adapter_view_layout, (ArrayList<Route>) routeList);

                                   Log.i(TAG, "renderRoutesList: Displaying all the team routes");
                                   listView.setAdapter(teamRouteListAdapter);
                                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                       @Override
                                       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                           String routeName = routeList.get(i).name;
                                           launchRoutesDetailsPage(routeName);
                                       }
                                   });
                               }
                           });
                       }
                   });
               }
           }
        });
    }
}
