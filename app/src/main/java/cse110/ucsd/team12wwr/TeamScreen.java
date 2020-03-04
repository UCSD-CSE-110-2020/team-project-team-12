package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cse110.ucsd.team12wwr.dialogs.TeamInvitationDialogFragment;


public class TeamScreen extends FragmentActivity
                        implements TeamInvitationDialogFragment.InviteDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_screen2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        FloatingActionButton fab = findViewById(R.id.add_fab);
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
                        launchTeamRouteActivity();

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
    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
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
        // User touched the dialog's positive button
        Log.i("onDialogPositiveClick ", "POSITIVE CLICKED INTO ACTIVITY");
        dialog = (TeamInvitationDialogFragment) dialog;
        String invitedEmail = ((TeamInvitationDialogFragment) dialog).getInvitedEmail();
        String invitedUser = ((TeamInvitationDialogFragment) dialog).getInvitedName();
        Log.i("onDialogPositiveClick ", "EMAIL is: " + invitedEmail
                + " NAME is: " + invitedUser);

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
}
