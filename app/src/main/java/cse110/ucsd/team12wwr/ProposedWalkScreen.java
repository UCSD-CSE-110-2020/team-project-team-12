package cse110.ucsd.team12wwr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Schedule;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProposedWalkScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static List<Item> itemList;
    private RecycleAdapter recycleAdapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_screen);

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
}
