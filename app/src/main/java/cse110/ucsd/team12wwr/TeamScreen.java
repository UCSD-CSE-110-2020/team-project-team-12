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
    String teamName;

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
        userEmail = getIntent().getStringExtra("user Email");

        Log.i("USER EMAIL IS: ", userEmail);

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
        initializeUpdateListener();
    }

    private void initializeUpdateListener() {
        Log.i("FUCKWHYPLS ", " PLS STAHP");
        FirebaseFirestore.getInstance().collection("users")
                .addSnapshotListener((newChatSnapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapshot != null && !newChatSnapshot.isEmpty()) {
                        Log.i("initUpList ", "RENDER INCOMING");
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
                teamName = u.teamID;
                Log.i("RENDERROUTES ", u.teamID);

                dao.findUsersByTeam(u.teamID).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            userList.add(document.toObject(User.class));
                            User forUser = null;
                            forUser = document.toObject(User.class);
                            Log.i("RENDERROUTESXXXXXX ", forUser.teamID);
                            Log.i("RENDERROUTES ", "ID FOR LOOP");
                            Log.i("RENDERROUTES ", forUser.userID);
                        }

                        for ( int i = 0; i < userList.size(); i++ ) {
                            String name = userList.get(i).firstName + " " + userList.get(i).lastName;
                            TeamScreenRowItem item = new TeamScreenRowItem(name, userList.get(i).userIcon, userList.get(i).teamID );
                            boolean fartsalot = false;
                            for (TeamScreenRowItem FUCK : rowItems){
                                //Log.i("FUCKEVERYTHING", FUCK.getMemberName());
                                ///Log.i("FUCKEVERYTHINGTWICE", ""+rowItems.size());
                                if(item.getMemberName().equals(FUCK.getMemberName())){
                                    fartsalot = true;
                                }
                            }
                            if(!fartsalot)
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
            inv.teamID = teamName;
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


        TeamScreenRowItem item = new TeamScreenRowItem(invitedUser, initials,"");
        rowItems.add(item);

        adapter.updateItems(rowItems);
        updateList();


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
        for (TeamScreenRowItem item : rowItems){
            Log.i("TEAMSCREENUPDATE IN rowItems ", " : " + item.getMemberName());
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
