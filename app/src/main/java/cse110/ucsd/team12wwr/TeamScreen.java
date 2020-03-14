package cse110.ucsd.team12wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cse110.ucsd.team12wwr.dialogs.TeamInvitationDialogFragment;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Invitation;
import cse110.ucsd.team12wwr.firebase.InvitationDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.teamlist.TeamListAdapter;
import cse110.ucsd.team12wwr.teamlist.TeamScreenRowItem;


public class TeamScreen extends FragmentActivity
                        implements TeamInvitationDialogFragment.InviteDialogListener {

    private static final String TAG = "TeamScreen";

    List<TeamScreenRowItem> rowItems = new ArrayList<>();
    TeamListAdapter adapter;
    String userEmail;
    String teamName;

    boolean existingUser = false;

    /* my code */
    String validatedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_screen2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        adapter = new TeamListAdapter(this, rowItems, teamName);

        SharedPreferences emailprefs = getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = emailprefs.getString("EMAIL_ID", null);

        Log.d(TAG, "onCreate: Email for current user: " + userEmail);
        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        Log.d(TAG, "onCreate: Navigation bar created");
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

//                        launchRoutesScreenActivity();
                        break;
                    case R.id.navigation_walk:
                        finish();
                        launchProposedWalkActivity();
                        break;
                    case R.id.navigation_teams:
                        break;
                }
                return false;
            }
        });

        initializeUpdateListener();

    }

    private void initializeUpdateListener() {
        UserDao dao = DaoFactory.getUserDao();
        dao.listenForChanges((newChatSnapshot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if (newChatSnapshot != null && !newChatSnapshot.isEmpty()) {
                renderRoutesList(userEmail);
            }
        });
    }

    private void renderRoutesList(String email) {
        Log.d(TAG, "renderRoutesList: Now rendering the list of team members");
        UserDao dao = DaoFactory.getUserDao();
        dao.findUserByID(email, task -> {
            if (task.isSuccessful()) {
                User currUser = null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    currUser = document.toObject(User.class);
                }
                if (currUser != null) {
                    teamName = currUser.teamID;

                    renderTeamMembers(currUser.teamID);
                    renderInvitees(currUser.teamID);
                    
                    ListView listView = findViewById(R.id.team_list);
                    adapter = new TeamListAdapter(this, rowItems, teamName);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    private void renderTeamMembers(String teamID) {
        List<User> userList = new ArrayList<>();

        UserDao dao = DaoFactory.getUserDao();
        dao.findUsersByTeam(teamID, task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    userList.add(document.toObject(User.class));
                }

                addItemsToAdapter(userList);
            }
        });
    }

    public void renderInvitees(String teamID) {
        List<User> userList = new ArrayList<>();

        InvitationDao invDao = DaoFactory.getInvitationDao();
        invDao.findInviteByTeam(teamID, task3 -> {
            if (task3.isSuccessful()) {
                Invitation inv = null;
                for (QueryDocumentSnapshot document : task3.getResult()) {
                    inv = document.toObject(Invitation.class);
                }

                if (inv != null) {
                    UserDao userDao = DaoFactory.getUserDao();
                    userDao.findUserByID(inv.inviteeID, task4 -> {
                        if (task4.isSuccessful()){
                            User invitedItem = null;
                            for(QueryDocumentSnapshot document2 : task4.getResult()){
                                invitedItem = document2.toObject(User.class);
                                userList.add(invitedItem);
                            }

                            addItemsToAdapter(userList);
                        }
                    });
                }
            }
        });
    }

    private void addItemsToAdapter(List<User> userList) {
        for ( int i = 0; i < userList.size(); i++ ) {
            String name = userList.get(i).firstName + " " + userList.get(i).lastName;
            TeamScreenRowItem item = new TeamScreenRowItem(name, userList.get(i).userIcon, userList.get(i).teamID );
            boolean duplicateFlag = false;

            for (TeamScreenRowItem itr : rowItems){
                if(item.getMemberName().equals(itr.getMemberName())){
                    duplicateFlag = true;
                }
            }
            if(!duplicateFlag) {
                rowItems.add(item);
            }
        }

        adapter.updateItems(rowItems);
    }

    public void launchProposedWalkActivity() {
        Intent intent = new Intent(this, ProposedWalkScreen.class);
        startActivity(intent);
    }

    public void launchTeamRouteActivity() {
        Intent intent = new Intent(this, TeamIndividRoutes.class);
        startActivity(intent);
    }

    public void openDialog() {
        DialogFragment newFragment = new TeamInvitationDialogFragment();
        newFragment.show(getSupportFragmentManager(), "open");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog = (TeamInvitationDialogFragment) dialog;
        String invitedEmail = ((TeamInvitationDialogFragment) dialog).getInvitedEmail();
        String invitedUser = ((TeamInvitationDialogFragment) dialog).getInvitedName();
        if(invitedEmail.equalsIgnoreCase("ERROR")){
            Toast toast = Toast.makeText(this, "Invalid Gmail address", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else {
            validatedEmail = invitedEmail;
            Toast toast = Toast.makeText(this, "Invite sent to " + invitedUser, Toast.LENGTH_LONG);
            toast.show();

            InvitationDao db = DaoFactory.getInvitationDao();
            Invitation inv = new Invitation();
            inv.inviteeID = validatedEmail;
            inv.teamID = teamName;
            db.insert(inv);
        }


        UserDao userDao = DaoFactory.getUserDao();
        User user = new User();
        user.firstName = ((TeamInvitationDialogFragment) dialog).getInvitedFirstName();
        user.lastName = ((TeamInvitationDialogFragment) dialog).getInvitedLastName();
        String initials = String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedFirstName().charAt(0)) +
                          String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedLastName().charAt(0));
        user.userIcon = initials;
        user.teamID = "";
        user.userID = invitedEmail;
        userDao.findUserByID(invitedEmail, task -> {
            if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                        existingUser = true;
                    if(!existingUser)
                        userDao.insertAll(user);
                }
        });
        TeamScreenRowItem item = new TeamScreenRowItem(invitedUser, initials,"");
        rowItems.add(item);
        adapter.updateItems(rowItems);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast toast = Toast.makeText(this, "Invite cancelled!", Toast.LENGTH_SHORT);
        toast.show();
    }


    public void createUsers() {
        ListView listView = findViewById(R.id.team_list);
        adapter = new TeamListAdapter(this, rowItems, "Team A");
        listView.setAdapter(adapter);

        List<User> teamList = new ArrayList<>();

        User firstUser = new User();
        firstUser.userID = "jane@gmail.com";
        firstUser.firstName = "Jane";
        firstUser.lastName = "Ease";
        firstUser.userIcon = "JE";
        firstUser.teamID = "Team A";
        teamList.add(firstUser);

        User secondUser = new User();
        secondUser.userID = "susan@gmail.com";
        secondUser.firstName = "Susan";
        secondUser.lastName = "Sath";
        secondUser.userIcon = "SS";
        secondUser.teamID = "Team A";
        teamList.add(secondUser);

        addItemsToAdapter(teamList);
    }
}
