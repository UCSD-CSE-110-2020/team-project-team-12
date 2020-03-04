package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cse110.ucsd.team12wwr.dialogs.TeamInvitationDialogFragment;
import cse110.ucsd.team12wwr.firebase.FirebaseUserDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.teamlist.TeamListAdapter;
import cse110.ucsd.team12wwr.teamlist.TeamScreenRowItem;

public class TeamScreen extends FragmentActivity
                        implements TeamInvitationDialogFragment.InviteDialogListener {

    private String firstName, lastName;
    List<User> teamList = new ArrayList<>();
    List<TeamScreenRowItem> rowItems = new ArrayList<>();
    ListView listView;
    TeamListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_screen2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        finish();
                        break;
                    case R.id.navigation_routes:
                        finish();
                        launchRoutesScreenActivity();

                        break;
                    case R.id.navigation_walk:
                        finish();
                        launchActivity();

                        break;
                    case R.id.navigation_teams:

                        break;
                }
                return false;
            }
        });

        createUsers();

        for ( int i = 0; i < teamList.size(); i++ ) {
            String name = teamList.get(i).firstName + " " + teamList.get(i).lastName;
            TeamScreenRowItem item = new TeamScreenRowItem( name, teamList.get(i).userColor, teamList.get(i).userIcon, teamList.get(i).teamID );
            rowItems.add(item);
        }

        listView = findViewById(R.id.team_list);
        adapter = new TeamListAdapter(this, rowItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    public void launchRoutesScreenActivity() {
        Intent intent = new Intent(this, RoutesScreen.class);
        startActivity(intent);
    }

    public void openDialog() {
        DialogFragment newFragment = new TeamInvitationDialogFragment();
        newFragment.show(getSupportFragmentManager(), "open");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.i("onDialogPositiveClick ", "POSITIVE CLICKED INTO ACTIVITY");
        dialog = (TeamInvitationDialogFragment) dialog;
        String invitedEmail = ((TeamInvitationDialogFragment) dialog).getInvitedEmail();
        String invitedUser = ((TeamInvitationDialogFragment) dialog).getInvitedName();
        Log.i("onDialogPositiveClick ", "EMAIL is: " + invitedEmail
                + " NAME is: " + invitedUser);

        FirebaseUserDao userDao = new FirebaseUserDao();
        User user = new User();
        user.firstName = ((TeamInvitationDialogFragment) dialog).getInvitedFirstName();
        user.lastName = ((TeamInvitationDialogFragment) dialog).getInvitedLastName();
        String initials = String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedFirstName().charAt(0)) +
                          String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedLastName().charAt(0));
        user.userIcon = initials;
        user.teamID = "";
        user.userColor = "";
        user.userID = invitedEmail;
        userDao.insertAll(user);
        teamList.add(user);
        TeamScreenRowItem item = new TeamScreenRowItem(invitedUser, "", initials, "" );
        rowItems.add(item);
        adapter.updateItems(rowItems);
        updateList();

        Toast toast = Toast.makeText(this, "Invite sent to " + invitedUser, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Log.i("onDialogPositiveClick ", "NEGATIVE CLICKED INTO ACTIVITY");
        Toast toast = Toast.makeText(this, "Invite cancelled!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void updateList() {
        listView = findViewById(R.id.team_list);
        adapter = new TeamListAdapter(this, rowItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
    public void createUsers() {
        FirebaseUserDao userDao = new FirebaseUserDao();

        User firstUser = new User();
        firstUser.userID = "jane@gmail.com";
        firstUser.firstName = "Jane";
        firstUser.lastName = "Ease";
        firstUser.userColor = "BLACK";
        firstUser.userIcon = "JE";
        firstUser.teamID = "Team A";
        userDao.insertAll(firstUser);
        teamList.add(firstUser);

        User secondUser = new User();
        secondUser.userID = "susan@gmail.com";
        secondUser.firstName = "Susan";
        secondUser.lastName = "Sath";
        secondUser.userColor = "GREEN";
        secondUser.userIcon = "SS";
        secondUser.teamID = "Team A";
        userDao.insertAll(secondUser);
        teamList.add(secondUser);

    }
}