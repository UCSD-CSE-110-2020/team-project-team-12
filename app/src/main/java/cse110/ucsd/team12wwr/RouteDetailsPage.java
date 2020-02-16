package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;

public class RouteDetailsPage extends AppCompatActivity {

    String routeName;
    List<Route> routeList;
    RouteDao dao;
    WWRDatabase walkDb;
    Route newRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);

        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
        databaseWriteExecutor.execute(() -> {
            WWRDatabase walkDb = WWRDatabase.getInstance(this);
            dao = walkDb.routeDao();

            routeList = dao.retrieveAllRoutes();

            Intent intent = getIntent();
            routeName = intent.getStringExtra("name");
            System.err.print("Route Name: ");
            System.err.println(routeName);

        });

        while (newRoute == null);


        setContentView(R.layout.activity_route_details_page);
        if (routeName != null) {
            TextView textView = (TextView) findViewById(R.id.route_title_detail);
            textView.setText(routeName);
            newRoute = walkDb.routeDao().findName(routeName);
        }


        if (newRoute != null) {
            if (newRoute.startingPoint != null) {
                TextView textView = (TextView) findViewById(R.id.start_textview);
                textView.setText("Starting Point: " + newRoute.startingPoint);
            }

            if (newRoute.endingPoint != null) {
                TextView textView1 = (TextView) findViewById(R.id.end_textview);
                textView1.setText("Ending Point: " + newRoute.endingPoint);
            }

            if (newRoute.difficulty != null) {
                TextView textView1 = (TextView) findViewById(R.id.diff_detail);
                if (newRoute.difficulty != null) {
                    if (newRoute.difficulty == Route.Difficulty.EASY) {
                        textView1.setText("Easy");
                    } else if (newRoute.difficulty == Route.Difficulty.MODERATE) {
                        textView1.setText("Moderate");
                    } else {
                        textView1.setText("Hard");
                    }
                }
            }
        }
    }
}
