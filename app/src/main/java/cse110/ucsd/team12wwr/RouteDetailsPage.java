package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;

public class RouteDetailsPage extends AppCompatActivity {

    String routeName;
    List<Route> routeList;
    RouteDao dao;
    WWRDatabase walkDb;
    Route newRoute;

    // Public constants for string intents
    public static final String TITLE = "ROUTE_TITLE";

    private static final String TAG = "RouteDetailsPage";

    TextView routeTitle, startPoint, endPoint;
    TextView totalTime, totalDist;
    TextView path, terrain, incline, surface;
    TextView difficulty, notes;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);

        Intent intent = getIntent();
        routeName = intent.getStringExtra("name");
        System.err.print("Route Name: ");
        System.err.println(routeName);

        walkDb = WWRDatabase.getInstance(this);
        dao = walkDb.routeDao();

        routeList = dao.retrieveAllRoutes();

        newRoute = walkDb.routeDao().findName(routeName);

        // Header
        routeTitle = findViewById(R.id.route_title_detail);
        startPoint = findViewById(R.id.start_textview);
        endPoint = findViewById(R.id.end_textview);

        // Metrics
        totalTime = findViewById(R.id.total_time_detail);
        totalDist = findViewById(R.id.dist_details);

        // Spinner info
        path = findViewById(R.id.path_details);
        terrain = findViewById(R.id.terrain_deets);
        incline = findViewById(R.id.incline_deets);
        surface = findViewById(R.id.texture_details);

        // Extra info
        difficulty = findViewById(R.id.diff_detail);
        notes = findViewById(R.id.notes_content);

        setContentView(R.layout.activity_route_details_page);
        if (routeName != null) {
            TextView textView = (TextView) findViewById(R.id.route_title_detail);
            textView.setText(routeName);
        }

        if (newRoute != null) {
            if (newRoute.startingPoint != null) {
                TextView textView = (TextView) findViewById(R.id.start_textview);
                textView.setText("Starting Point: " + newRoute.startingPoint);
            }

            if (newRoute.endingPoint != null) {
                TextView textView = (TextView) findViewById(R.id.end_textview);
                textView.setText("Ending Point: " + newRoute.endingPoint);
            }

            if (newRoute.difficulty != null) {
                TextView textView = (TextView) findViewById(R.id.diff_detail);
                if (newRoute.difficulty == Route.Difficulty.EASY) {
                    textView.setText("Easy");
                } else if (newRoute.difficulty == Route.Difficulty.MODERATE) {
                    textView.setText("Moderate");
                } else {
                    textView.setText("Hard");
                }
            }

            if (newRoute.evenness != null) {
                TextView textView = (TextView) findViewById(R.id.texture_details);
                if (newRoute.evenness == Route.Evenness.EVEN_SURFACE) {
                    textView.setText("Surface: Even");
                } else if (newRoute.evenness == Route.Evenness.UNEVEN_SURFACE) {
                    textView.setText("Surface: Uneven");
                }
            }

            if (newRoute.hilliness != null) {
                TextView textView = (TextView) findViewById(R.id.incline_deets);
                if (newRoute.hilliness == Route.Hilliness.FLAT) {
                    textView.setText("Incline: Flat");
                } else if (newRoute.hilliness == Route.Hilliness.HILLY) {
                    textView.setText("Incline: Hilly");
                }
            }

            if (newRoute.routeType != null) {
                TextView textView = (TextView) findViewById(R.id.path_details);
                if (newRoute.routeType == Route.RouteType.LOOP) {
                    textView.setText("Path Type: Loop");
                } else if (newRoute.routeType == Route.RouteType.OUT_AND_BACK) {
                    textView.setText("Path Type: Out and Back");
                }
            }

            if (newRoute.surfaceType != null) {
                TextView textView = (TextView) findViewById(R.id.terrain_deets);
                if (newRoute.surfaceType == Route.SurfaceType.STREETS) {
                    textView.setText("Terrain Type: Streets");
                } else if (newRoute.surfaceType == Route.SurfaceType.TRAIL) {
                    textView.setText("Terrain Type: Trial");
                }
            }

//            if (newRoute.favorite != null) {
//                TextView textView = (TextView) findViewById(R.id.favorited_details);
//                if (newRoute.favorite == Route.Favorite.FAVORITE) {
//                    textView.setText("Surface: Even");
//                } else if (newRoute.evenness == Route.Evenness.UNEVEN_SURFACE) {
//                    textView.setText("Surface: Uneven");
//                }
//            }
        }
        Button back = findViewById(R.id.back_button);
        Button edit = findViewById(R.id.edit_route);
        Button start = findViewById(R.id.add_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRouteInfoActivity();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntentionalActivity();
            }
        });

    }

    public void launchRouteInfoActivity() {
        Log.d(TAG, "launchRouteInfoActivity: launching the route information page");
        Intent intent = new Intent(this, RouteInfoActivity.class);
        intent.putExtra(TITLE, routeName);
        startActivity(intent);
    }

    public void launchIntentionalActivity() {
        Log.d(TAG, "launchActivity: launching the walking activity");
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        intent.putExtra(TITLE, routeName);
        startActivity(intent);
    }

    public String extractString (TextView textView) {
        return textView.getText().toString();
    }


}
