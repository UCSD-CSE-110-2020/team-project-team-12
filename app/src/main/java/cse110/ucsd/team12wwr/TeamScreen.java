package cse110.ucsd.team12wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cse110.ucsd.team12wwr.dialogs.TeamInvitationDialogFragment;
import cse110.ucsd.team12wwr.firebase.FirebaseInvitationDao;
import cse110.ucsd.team12wwr.firebase.FirebaseRouteDao;
import cse110.ucsd.team12wwr.firebase.FirebaseUserDao;
import cse110.ucsd.team12wwr.firebase.Invitation;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.teamlist.TeamListAdapter;
import cse110.ucsd.team12wwr.teamlist.TeamScreenRowItem;


public class TeamScreen extends FragmentActivity
                        implements TeamInvitationDialogFragment.InviteDialogListener {

    private static final String TAG = "TeamScreen";

    private String firstName, lastName;
    List<User> teamList = new ArrayList<>();
    List<TeamScreenRowItem> rowItems = new ArrayList<>();
    ListView listView;
    TeamListAdapter adapter;
    String userEmail;

    /* my code */
    String validatedEmail;
    FirebaseInvitationDao db = new FirebaseInvitationDao();
    Invitation inv = new Invitation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_screen2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        Context context;
        SharedPreferences emailprefs = getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = emailprefs.getString("EMAIL_ID", "jane@gmail.com");
        //userEmail = "jane@gmail.com";

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
        initializeUpdateListener();
//        createUsers();


//        for ( int i = 0; i < teamList.size(); i++ ) {
//            String name = teamList.get(i).firstName + " " + teamList.get(i).lastName;
//            TeamScreenRowItem item = new TeamScreenRowItem(name, teamList.get(i).userIcon, teamList.get(i).teamID );
//            rowItems.add(item);
//        }
//
//        listView = findViewById(R.id.team_list);
//        adapter = new TeamListAdapter(this, rowItems);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

    }

    private void initializeUpdateListener() {
        FirebaseFirestore.getInstance().collection("users")
                .addSnapshotListener((newChatSnapshot, error) -> {
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
        FirebaseUserDao dao = new FirebaseUserDao();
        dao.findUserByID(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User u = null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    u = document.toObject(User.class);
                }

                dao.findUsersByTeam(u.teamID).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userList.add(document.toObject(User.class));
                        }

                        for ( int i = 0; i < teamList.size(); i++ ) {
                            String name = teamList.get(i).firstName + " " + teamList.get(i).lastName;
                            TeamScreenRowItem item = new TeamScreenRowItem(name, teamList.get(i).userIcon, teamList.get(i).teamID );
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
                });
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

        if(invitedEmail.equalsIgnoreCase("ERROR")){
            Log.i("onDialogPositiveClick ", "INVALID EMAIL INPUT");
            Toast toast = Toast.makeText(this, "Invalid Gmail address", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else {
            Log.i("onDialogPositiveClick ", "EMAIL is: " + invitedEmail
                    + " NAME is: " + invitedUser);
            validatedEmail = invitedEmail;
            Toast toast = Toast.makeText(this, "Invite sent to " + invitedUser, Toast.LENGTH_LONG);
            toast.show();

            inv.inviteeID = validatedEmail;
            inv.teamID = "TEAM !yee";
            db.insert(inv);
        }



        FirebaseUserDao userDao = new FirebaseUserDao();
        User user = new User();
        user.firstName = ((TeamInvitationDialogFragment) dialog).getInvitedFirstName();
        user.lastName = ((TeamInvitationDialogFragment) dialog).getInvitedLastName();
        String initials = String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedFirstName().charAt(0)) +
                          String.valueOf(((TeamInvitationDialogFragment) dialog).getInvitedLastName().charAt(0));
        user.userIcon = initials;
        user.teamID = "";
        user.userID = invitedEmail;
        userDao.insertAll(user);
        teamList.add(user);
        TeamScreenRowItem item = new TeamScreenRowItem(invitedUser, initials, "" );
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
        firstUser.userIcon = "JE";
        firstUser.teamID = "Team A";
        userDao.insertAll(firstUser);
        teamList.add(firstUser);

        User secondUser = new User();
        secondUser.userID = "susan@gmail.com";
        secondUser.firstName = "Susan";
        secondUser.lastName = "Sath";
        secondUser.userIcon = "SS";
        secondUser.teamID = "Team A";
        userDao.insertAll(secondUser);
        teamList.add(secondUser);

    }
}
