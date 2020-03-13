package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;

import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.firebase.Walk;
import cse110.ucsd.team12wwr.firebase.WalkDao;

public class RouteDetailsPage extends AppCompatActivity {
    // Public constants for string intents
    public static final String TITLE = "ROUTE_TITLE";

    private static final String TAG = "RouteDetailsPage";

    private String routeName;
    private Boolean fromActivity;

    SharedPreferences email;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);

        Intent intent = getIntent();
        routeName = intent.getStringExtra("name");
        fromActivity = intent.getBooleanExtra("fromTeam", false);
        email = getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = email.getString("EMAIL_ID", null);
        userEmail = "nicholasalimit@gmail.com";

        Button back = findViewById(R.id.back_button);
        Button edit = findViewById(R.id.edit_route);
        Button start = findViewById(R.id.add_button);
        Button schedule = findViewById(R.id.sched_walk_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if ( !fromActivity ) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchRouteInfoActivity();
                }
            });
        }

        if ( fromActivity ) { // && !hasScheduledWalk[0]) {
            schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { launchTeamInviteActivity(); }
            });
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntentionalActivity();
            }
        });

    }

    public void startingPointClickListener(View v) {
        launchGoogleMaps();
    }

    public Intent launchGoogleMaps() {
        TextView startPoint = findViewById(R.id.start_textview);
        String location = startPoint.getText().toString();
        if (location.length() > 16)  {
            location = location.substring(16).replaceAll(" ", "+");
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
            return mapIntent;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDetails();

        CheckBox star = findViewById(R.id.favorited_details);
        star.setEnabled(false);

        Button back = findViewById(R.id.back_button);
        Button edit = findViewById(R.id.edit_route);
        Button start = findViewById(R.id.add_button);
        Button schedule = findViewById(R.id.sched_walk_btn);

        if ( fromActivity ) {
            edit.setVisibility(View.GONE);
            schedule.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.VISIBLE);
            schedule.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if ( !fromActivity ) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchRouteInfoActivity();
                }
            });
        }

        if ( fromActivity ) {
            schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { launchTeamInviteActivity(); }
            });
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntentionalActivity();
            }
        });
    }

    private void populateDetails() {
        setContentView(R.layout.activity_route_details_page);
        if (routeName != null) {
            TextView routeTitle = findViewById(R.id.route_title_detail);
            routeTitle.setText(routeName);
        }

        RouteDao routeDao = DaoFactory.getRouteDao();
        routeDao.findName(routeName, task -> {
            if (task.isSuccessful()) {
                Route newRoute = null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (newRoute == null) {
                        newRoute = document.toObject(Route.class);
                    } else {
                        Log.w(TAG, "There is a duplicate route entry in the database!");
                    }
                }

                populateRouteInfo(newRoute);
            }
        });

        WalkDao walkDao = DaoFactory.getWalkDao();
        walkDao.findByRouteName(routeName, task -> {
            if (task.isSuccessful()) {
                List<Walk> walks = new ArrayList<Walk>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    walks.add(document.toObject(Walk.class));
                }

                determineWalk(walks);
            }
        });
    }

    public void populateRouteInfo(Route newRoute) {
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
    }

    public void determineWalk(List<Walk> walks) {
        Walk mostRecentWalk = null;
        Walk substituteWalk = null;
        for (Walk walk : walks) {
            if (mostRecentWalk == null) {
                mostRecentWalk = walk;
                if (!mostRecentWalk.userID.equals(userEmail)) {
                    substituteWalk = mostRecentWalk;
                    mostRecentWalk = null;
                }
                break;
            }
        }

        if (mostRecentWalk != null) {
            populateWalkInfo(mostRecentWalk);
        } else {
            populateSubstitutedWalkInfo(substituteWalk);
        }
    }

    void populateWalkInfo(Walk mostRecentWalk) {
        if (mostRecentWalk != null) {
            if (mostRecentWalk.duration != null) {
                TextView duration = findViewById(R.id.total_time_detail);
                duration.setText(mostRecentWalk.duration);
            }

            if (mostRecentWalk.distance != null) {
                TextView distance = findViewById(R.id.dist_details);
                distance.setText(mostRecentWalk.distance);
            }

            TextView checkmark = findViewById(R.id.checkmark_detail);
            checkmark.setVisibility(View.VISIBLE);
        }
    }

    void populateSubstitutedWalkInfo(Walk substituteWalk) {
        if (substituteWalk != null) {
            if (substituteWalk.duration != null) {
                TextView duration = findViewById(R.id.total_time_detail);
                duration.setText(substituteWalk.duration);
            }

            if (substituteWalk.distance != null) {
                TextView distance = findViewById(R.id.dist_details);
                distance.setText(substituteWalk.distance);
            }

            if (substituteWalk.userID != null) {
                UserDao dao = DaoFactory.getUserDao();
                dao.findUserByID(substituteWalk.userID, task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            String fullName = user.firstName + " " + user.lastName;

                            TextView notes = findViewById(R.id.notes_content);
                            String oldNotes = notes.getText().toString();
                            notes.setText(String.format("%s's stats – %s", fullName, oldNotes));
                        }
                    }
                });
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
        intent.putExtra("fromTeam",fromActivity);
        startActivity(intent);
    }

    public void launchTeamInviteActivity() {
        Log.d(TAG, "launchTeamInviteActivity: Launching team invitation page");
        Intent intent = new Intent(this, InviteWalk.class);
        intent.putExtra(TITLE, routeName);
        intent.putExtra("fromTeam",fromActivity);
        startActivity(intent);
    }

    public String extractString (TextView textView) {
        return textView.getText().toString();
    }
}
