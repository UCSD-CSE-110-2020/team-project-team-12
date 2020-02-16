package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
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

public class RoutesScreen extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;
    List<Route> routeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_screen);

        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
        databaseWriteExecutor.execute(() -> {
            WWRDatabase walkDb = WWRDatabase.getInstance(this);
            RouteDao dao = walkDb.routeDao();
            routeList = dao.retrieveAllRoutes();
        });

        listView = (ListView)findViewById(R.id.list_view);

        arrayList = new ArrayList<>();

        arrayList.add("Mission Bay");
        arrayList.add("Torrey Pines Hike");
        arrayList.add("Potato Chip Rock Hike");

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

            }
        });
    }
}
