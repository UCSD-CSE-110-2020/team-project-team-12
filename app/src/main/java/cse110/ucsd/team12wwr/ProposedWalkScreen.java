package cse110.ucsd.team12wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;
import cse110.ucsd.team12wwr.firebase.Schedule;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.firebase.Walk;
import cse110.ucsd.team12wwr.firebase.WalkDao;
import cse110.ucsd.team12wwr.recycler.Item;
import cse110.ucsd.team12wwr.recycler.RecycleAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProposedWalkScreen extends AppCompatActivity {

    private static final String TAG = "ProposedWalkScreen";
    private RecyclerView recyclerView;
    public static List<Item> itemList;
    private RecycleAdapter recycleAdapter;
    List<User> userList;
    private String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_screen);

        CheckBox star = findViewById(R.id.proposed_favorited_details);
        star.setEnabled(false);

        Button withdrawWalk = findViewById(R.id.cancel_btn);
        Button scheduleWalk = findViewById(R.id.schedule_btn);
        TextView noProposeText = findViewById(R.id.no_proposal);
        ScrollView allDetails = findViewById(R.id.scrollable_proposal);

        SharedPreferences emailprefs = getSharedPreferences("USER_ID", MODE_PRIVATE);
        String userEmail = emailprefs.getString("EMAIL_ID", null);

        DaoFactory.getUserDao().findUserByID(userEmail, task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User u = document.toObject(User.class);
                    DaoFactory.getScheduleDao().findScheduleByTeam(u.teamID, scheduleTask -> {
                        if (scheduleTask.isSuccessful()) {
                            Schedule s = null;
                            for (QueryDocumentSnapshot scheduleDocument : scheduleTask.getResult()) {
                                s = scheduleDocument.toObject(Schedule.class);
                            }

                            if (s != null) {
                                routeName = s.routeName;
                                Log.d(TAG, "onCreate: Route name is : " + routeName);
                                populateDetails();
                                TextView proposedDate = findViewById(R.id.proposed_date_actual_textview);
                                TextView proposedTime = findViewById(R.id.proposed_time_actual_textview);

                                proposedDate.setText(s.date);
                                proposedTime.setText(s.time);
                                noProposeText.setVisibility(View.GONE);
                                allDetails.setVisibility(View.VISIBLE);
                                if (s.proposerUserID.equals(userEmail)) {
                                    final String teamID = s.teamID;

                                    withdrawWalk.setVisibility(View.VISIBLE);
                                    withdrawWalk.setOnClickListener(v -> {
                                        DaoFactory.getScheduleDao().delete(teamID);
                                    });

                                    scheduleWalk.setVisibility(View.VISIBLE);
                                    scheduleWalk.setOnClickListener(v -> {
                                        DaoFactory.getScheduleDao().updateScheduledState(teamID, true);
                                    });

                                    if (s.isScheduled) {
                                        withdrawWalk.setText("Cancel");
                                        scheduleWalk.setVisibility(View.GONE);
                                    } else {
                                        withdrawWalk.setText("Withdraw");
                                        scheduleWalk.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                noProposeText.setVisibility(View.VISIBLE);
                                allDetails.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        finish();
                        break;
                    case R.id.navigation_routes:
                        finish();
                        launchTeamRouteActivity();
                        break;
                    case R.id.navigation_walk:
                        break;
                    case R.id.navigation_teams:
                        finish();
                        launchTeamScreenActivity();
                        break;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.vote_view);
        SharedPreferences emailPrefs  = this.getSharedPreferences("USER_ID", MODE_PRIVATE);
        String email = emailPrefs.getString("EMAIL_ID", null);

        UserDao userDao = DaoFactory.getUserDao();
        userDao.findUserByID(email, task1 -> {
            if ( task1.isSuccessful()) {
                User user = null;
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    user = document.toObject(User.class);
                }
                if ( user != null) {
                    userDao.findUsersByTeam(user.teamID, task2 -> {
                        userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            userList.add(document.toObject(User.class));
                        }
                        itemList = new ArrayList<>();

                        for (User u : userList) {
                            Item item = new Item();
                            item.setName(u.firstName + " " + u.lastName);
                            itemList.add(item);
                        }

                        recycleAdapter = new RecycleAdapter(this);

                        recyclerView.setAdapter(recycleAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    });
                }
            }
        });
    }

    public void launchTeamRouteActivity() {
        Intent intent = new Intent(this, TeamIndividRoutes.class);
        startActivity(intent);
    }

    public void launchTeamScreenActivity() {
        Intent intent = new Intent(this, TeamScreen.class);
        startActivity(intent);
    }


    private void populateDetails() {
        if (routeName != null) {
            TextView routeTitle = findViewById(R.id.proposed_route_title_detail);
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
    }

    public void populateRouteInfo(Route newRoute) {
        if (newRoute != null) {
            if (newRoute.startingPoint != null) {
                TextView startPoint = findViewById(R.id.proposed_start_textview);
                startPoint.setText("Starting Point: " + newRoute.startingPoint);
            }

            if (newRoute.endingPoint != null) {
                TextView endPoint = findViewById(R.id.proposed_end_textview);
                endPoint.setText("Ending Point: " + newRoute.endingPoint);
            }

            if (newRoute.difficulty != null) {
                TextView difficulty = findViewById(R.id.proposed_diff_detail);
                if (newRoute.difficulty == Route.Difficulty.EASY) {
                    difficulty.setText("Easy");
                } else if (newRoute.difficulty == Route.Difficulty.MODERATE) {
                    difficulty.setText("Moderate");
                } else {
                    difficulty.setText("Hard");
                }
            }

            if (newRoute.evenness != null) {
                TextView surface = findViewById(R.id.proposed_texture_details);
                if (newRoute.evenness == Route.Evenness.EVEN_SURFACE) {
                    surface.setText("Surface: Even");
                } else if (newRoute.evenness == Route.Evenness.UNEVEN_SURFACE) {
                    surface.setText("Surface: Uneven");
                }
            }

            if (newRoute.hilliness != null) {
                TextView incline = findViewById(R.id.proposed_incline_deets);
                if (newRoute.hilliness == Route.Hilliness.FLAT) {
                    incline.setText("Incline: Flat");
                } else if (newRoute.hilliness == Route.Hilliness.HILLY) {
                    incline.setText("Incline: Hilly");
                }
            }

            if (newRoute.routeType != null) {
                TextView path = findViewById(R.id.proposed_path_details);
                if (newRoute.routeType == Route.RouteType.LOOP) {
                    path.setText("Path Type: Loop");
                } else if (newRoute.routeType == Route.RouteType.OUT_AND_BACK) {
                    path.setText("Path Type: Out and Back");
                }
            }

            if (newRoute.surfaceType != null) {
                TextView terrain = findViewById(R.id.proposed_terrain_deets);
                if (newRoute.surfaceType == Route.SurfaceType.STREETS) {
                    terrain.setText("Terrain Type: Streets");
                } else if (newRoute.surfaceType == Route.SurfaceType.TRAIL) {
                    terrain.setText("Terrain Type: Trial");
                }
            }

            if (newRoute.favorite != null) {
                CheckBox star = findViewById(R.id.proposed_favorited_details);
                if (newRoute.favorite == Route.Favorite.FAVORITE) {
                    star.setChecked(true);
                } else {
                    star.setChecked(false);
                }
            }

            if (newRoute.notes != null && !newRoute.notes.equals("")) {
                TextView notes = findViewById(R.id.proposed_notes_content);
                notes.setText(newRoute.notes);
            }
        }
    }
}
