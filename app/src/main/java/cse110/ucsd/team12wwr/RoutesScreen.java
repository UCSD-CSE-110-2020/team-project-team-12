package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.firebase.FirebaseRouteDao;
import cse110.ucsd.team12wwr.firebase.Route;

public class RoutesScreen extends AppCompatActivity {

    private static final String TAG = "RoutesScreen";

    ListView listView;
    List<Route> routeList;
    ArrayList<Route> routeListParam;
    String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_screen);

        initializeUpdateListener();

        Button back = findViewById(R.id.back_button);
        Button add = findViewById(R.id.add_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRouteInfoActivity();
            }
        });
    }

//     @Override
//     protected void onResume() {
//         super.onResume();

//         WWRDatabase db = WWRDatabase.getInstance(this);
//         routeList = db.routeDao().retrieveAllRoutes();

//         listView = findViewById(R.id.list_view);
//         routeListParam = new ArrayList<>(routeList);

// //        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routeList);
//         RouteListAdapter routeListAdapter = new RouteListAdapter(this, R.layout.route_adapter_view_layout, routeListParam);

//         listView.setAdapter(routeListAdapter);
    

    private void initializeUpdateListener() {
        FirebaseFirestore.getInstance().collection("routes")
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapshot != null && !newChatSnapshot.isEmpty()) {
                        renderRoutesList();
                    }
                });
    }
    

    private void renderRoutesList() {
        FirebaseRouteDao routeDao = new FirebaseRouteDao();
        routeDao.retrieveAllRoutes().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Route> routeList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    routeList.add(document.toObject(Route.class));
                }

//                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routeList);

                listView = findViewById(R.id.list_view);
                routeListParam = new ArrayList<>(routeList);

                RouteListAdapter routeListAdapter = new RouteListAdapter(this, R.layout.route_adapter_view_layout,
                        routeListParam);

                listView.setAdapter(routeListAdapter);
//                listView.setAdapter(arrayAdapter);
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

    public void launchRoutesDetailsPage() {
        Intent intent = new Intent(this, RouteDetailsPage.class);
        intent.putExtra("name", routeName);
        startActivity(intent);
    }

    public void launchRouteInfoActivity() {
        Log.d(TAG, "launchRouteInfoActivity: launching the route information page");
        Intent intent = new Intent(this, RouteInfoActivity.class);

        startActivity(intent);
    }
}