package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;
import cse110.ucsd.team12wwr.database.WalkDao;

public class RoutesScreen extends AppCompatActivity {

    private static final String TAG = "RoutesScreen";

    ListView listView;
    ArrayList<String> arrayList;
    List<Route> routeList;
    RouteDao dao;
    String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_screen);

        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
        databaseWriteExecutor.execute(() -> {
            WWRDatabase walkDb = WWRDatabase.getInstance(this);
            dao = walkDb.routeDao();

            Route newRoute = new Route();

            newRoute.name = "Potato Chip Rock Hike";
            newRoute.startingPoint = "Street";

//            dao.insertAll(newRoute);

            routeList = dao.retrieveAllRoutes();
        });

        listView = (ListView)findViewById(R.id.list_view);

        arrayList = new ArrayList<>();

        arrayList.add("Mission Bay");
        arrayList.add("Torrey Pines Hike");
        arrayList.add("Potato Chip Rock Hike");

        System.err.println(routeList);

        if (routeList != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routeList);

            listView.setAdapter(arrayAdapter);
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

            listView.setAdapter(arrayAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeName = routeList.get(position).name;
                launchRoutesDetailsPage();
            }
        });
    }

    public void launchRoutesDetailsPage() {
        Log.d(TAG, "launchRoutesDetailsPage: launching the route details page");
        Intent intent = new Intent(this, RouteDetailsPage.class);
//        intent.putExtra(name, routeName);
        startActivity(intent);
    }
}
