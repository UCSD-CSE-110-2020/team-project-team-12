package cse110.ucsd.team12wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;

public class RoutesScreen extends AppCompatActivity {

    private static final String TAG = "RoutesScreen";

    private String routeName;
    private ListView listView;

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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        finish();
                        break;
                    case R.id.navigation_routes:
                        break;
                    case R.id.navigation_walk:
                        finish();
                        launchActivity();


                        break;
                    case R.id.navigation_teams:
                        finish();
                        launchTeamScreenActivity();


                        break;
                }
                return false;
            }
        });
    }

    private void initializeUpdateListener() {
        RouteDao dao = DaoFactory.getRouteDao();
        dao.listenForChanges((newChatSnapshot, error) -> {
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
        RouteDao routeDao = DaoFactory.getRouteDao();
        routeDao.retrieveAllRoutes(task -> {
            if (task.isSuccessful()) {
                List<Route> routeList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    routeList.add(document.toObject(Route.class));
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routeList);

                listView = findViewById(R.id.list_view);
                listView.setAdapter(arrayAdapter);
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

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    public void launchTeamScreenActivity() {
        Intent intent = new Intent(this, TeamScreen.class);
        startActivity(intent);
    }
}