package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;

public class RouteDetailsPage extends AppCompatActivity {
    // Public constants for string intents
    public static final String TITLE = "ROUTE_TITLE";

    private static final String TAG = "RouteDetailsPage";

    private String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);

        Intent intent = getIntent();
        routeName = intent.getStringExtra("name");
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRouteDetails();

        CheckBox star = findViewById(R.id.favorited_details);
        star.setEnabled(false);

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

    private void populateRouteDetails() {
        WWRDatabase db = WWRDatabase.getInstance(this);
        Route newRoute = db.routeDao().findName(routeName);

        setContentView(R.layout.activity_route_details_page);
        if (routeName != null) {
            TextView routeTitle = findViewById(R.id.route_title_detail);
            routeTitle.setText(routeName);
        }

        if (newRoute != null) {
            if (newRoute.startingPoint != null) {
                TextView startPoint = findViewById(R.id.start_textview);
                startPoint.setText("Starting Point: " + newRoute.startingPoint);
            }

            if (newRoute.endingPoint != null) {
                TextView endPoint = findViewById(R.id.end_textview);
                endPoint.setText("Ending Point: " + newRoute.endingPoint);
            }

            if (newRoute.difficulty != null) {
                TextView difficulty = findViewById(R.id.diff_detail);
                if (newRoute.difficulty == Route.Difficulty.EASY) {
                    difficulty.setText("Easy");
                } else if (newRoute.difficulty == Route.Difficulty.MODERATE) {
                    difficulty.setText("Moderate");
                } else {
                    difficulty.setText("Hard");
                }
            }

            if (newRoute.evenness != null) {
                TextView surface = findViewById(R.id.texture_details);
                if (newRoute.evenness == Route.Evenness.EVEN_SURFACE) {
                    surface.setText("Surface: Even");
                } else if (newRoute.evenness == Route.Evenness.UNEVEN_SURFACE) {
                    surface.setText("Surface: Uneven");
                }
            }

            if (newRoute.hilliness != null) {
                TextView incline = findViewById(R.id.incline_deets);
                if (newRoute.hilliness == Route.Hilliness.FLAT) {
                    incline.setText("Incline: Flat");
                } else if (newRoute.hilliness == Route.Hilliness.HILLY) {
                    incline.setText("Incline: Hilly");
                }
            }

            if (newRoute.routeType != null) {
                TextView path = findViewById(R.id.path_details);
                if (newRoute.routeType == Route.RouteType.LOOP) {
                    path.setText("Path Type: Loop");
                } else if (newRoute.routeType == Route.RouteType.OUT_AND_BACK) {
                    path.setText("Path Type: Out and Back");
                }
            }

            if (newRoute.surfaceType != null) {
                TextView terrain = findViewById(R.id.terrain_deets);
                if (newRoute.surfaceType == Route.SurfaceType.STREETS) {
                    terrain.setText("Terrain Type: Streets");
                } else if (newRoute.surfaceType == Route.SurfaceType.TRAIL) {
                    terrain.setText("Terrain Type: Trial");
                }
            }

            if (newRoute.favorite != null) {
                CheckBox star = findViewById(R.id.favorited_details);
                if (newRoute.favorite == Route.Favorite.FAVORITE) {
                    star.setChecked(true);
                } else {
                    star.setChecked(false);
                }
            }

            if (newRoute.notes != null && !newRoute.notes.equals("")) {
                TextView notes = findViewById(R.id.notes_content);
                notes.setText(newRoute.notes);
            }
        }

        List<Walk> recentWalks = db.walkDao().findByRouteName(routeName);
        Walk mostRecentWalk = null;
        if (recentWalks.size() > 0) {
            mostRecentWalk = recentWalks.get(0);
        }

        if (mostRecentWalk != null) {
            if (mostRecentWalk.duration != null) {
                TextView duration = findViewById(R.id.total_time_detail);
                duration.setText(mostRecentWalk.duration);
            }

            if (mostRecentWalk.distance != null) {
                TextView distance = findViewById(R.id.dist_details);
                distance.setText(mostRecentWalk.distance);
            }
        }
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
