package cse110.ucsd.team12wwr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cse110.ucsd.team12wwr.dialogs.TeamInvitationDialogFragment;
import cse110.ucsd.team12wwr.firebase.FirebaseInvitationDao;
import cse110.ucsd.team12wwr.firebase.Invitation;

public class TeamScreenActivity extends FragmentActivity
                                implements TeamInvitationDialogFragment.InviteDialogListener{

    String validatedEmail;
    FirebaseInvitationDao db = new FirebaseInvitationDao();
    Invitation inv = new Invitation();


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.i("onDialogPositiveClick ", "POSITIVE CLICKED INTO ACTIVITY");
        dialog = (TeamInvitationDialogFragment) dialog;
        String invitedEmail = ((TeamInvitationDialogFragment) dialog).getInvitedEmail();
        String invitedUser = ((TeamInvitationDialogFragment) dialog).getInvitedName();

        if(invitedEmail.equalsIgnoreCase("ERROR")){
            Log.i("onDialogPositiveClick ", "INVALID EMAIL INPUT");
            Toast toast = Toast.makeText(this, "Invalid Gmail address", Toast.LENGTH_LONG);
            toast.show();
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

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Log.i("onDialogPositiveClick ", "NEGATIVE CLICKED INTO ACTIVITY");
        Toast toast = Toast.makeText(this, "Invite cancelled!", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_screen);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        DialogFragment newFragment = new TeamInvitationDialogFragment();
        newFragment.show(getSupportFragmentManager(), "open");
    }
}