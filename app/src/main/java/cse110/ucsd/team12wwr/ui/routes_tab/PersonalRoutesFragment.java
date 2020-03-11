package cse110.ucsd.team12wwr.ui.routes_tab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.RouteDetailsPage;
import cse110.ucsd.team12wwr.RouteInfoActivity;
import cse110.ucsd.team12wwr.RouteListAdapter;
import cse110.ucsd.team12wwr.TeamIndividRoutes;
import cse110.ucsd.team12wwr.TeamRouteListAdapter;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalRoutesFragment extends Fragment {

    private static final String TAG = "PersonalRoutesFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    ArrayList<Route> routeListParam;
    ListView listView;
    String routeName;
    SharedPreferences emailPref;
    String userEmail;

    public static PersonalRoutesFragment newInstance(int index) {
        PersonalRoutesFragment fragment = new PersonalRoutesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Entered Personal Routes Tab!");

        emailPref = this.getActivity().getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = emailPref.getString("EMAIL_ID", null);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }

    public void launchRouteInfoActivity() {
        Log.d(TAG, "launchRouteInfoActivity: launching the route information page");
        Intent intent = new Intent(getActivity(), RouteInfoActivity.class);

        startActivity(intent);
    }

    public void launchRoutesDetailsPage() {
        Log.d(TAG, "launchRouteDetailsPage: launching the route details page");
        Intent intent = new Intent(getActivity(), RouteDetailsPage.class);
        intent.putExtra("name", routeName);
        startActivity(intent);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_individ_routes, container, false);

        FloatingActionButton button = root.findViewById(R.id.add_fab2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRouteInfoActivity();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private void renderRoutesList(View view) {
        RouteDao routeDao = DaoFactory.getRouteDao();
        Log.d(TAG, "renderRoutesList: Loading all routes from database");
        routeDao.retrieveAllRoutes(task -> {
            if (task.isSuccessful()) {
                List<Route> routeList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    routeList.add(document.toObject(Route.class));
                }

                routeListParam = new ArrayList<>();
                for ( Route route : routeList ) {
                    if ( route.userID != null  && route.userID.equals(userEmail) ) {
                        routeListParam.add(route);
                    }
                }

                Log.i(TAG, "renderRoutesList: Extracting personal routes...");

                listView = view.findViewById(R.id.individ_routes_list);
                RouteListAdapter routeListAdapter = new RouteListAdapter(getActivity(),
                        R.layout.route_adapter_view_layout, routeListParam);

                listView.setAdapter(routeListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        routeName = routeList.get(position).name;
                        launchRoutesDetailsPage();
                    }
                });
            }
        });
    }
}