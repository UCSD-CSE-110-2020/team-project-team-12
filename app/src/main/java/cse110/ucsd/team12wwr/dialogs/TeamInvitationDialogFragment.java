package cse110.ucsd.team12wwr.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import cse110.ucsd.team12wwr.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cse110.ucsd.team12wwr.TeamScreenActivity;

public class TeamInvitationDialogFragment extends DialogFragment {

    public interface InviteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private String invitedEmail = "";
    private String invitedLastName = "";
    private String invitedFirstName = "";
    private String invitedName;
    InviteDialogListener listener;

    public String getInvitedName(){
        return invitedFirstName + " " + invitedLastName;
    }
    public String getInvitedFirstName() {
        return invitedFirstName;
    }

    public String getInvitedLastName() {
        return invitedLastName;
    }
    public String getInvitedEmail(){
        return invitedEmail;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (InviteDialogListener) context;
        }
        catch (ClassCastException e) {
            Log.i("TeamInvitationDialogFragment.onAttach() ", "Activity doesn't implement interface");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invitation,null);
        builder.setView(view)
                .setPositiveButton("Invite!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText invitedEmailField = view.findViewById(R.id.username);
                        invitedEmail = invitedEmailField.getText().toString();

                        EditText invitedLastNameField = view.findViewById(R.id.first_name);
                        invitedFirstName = invitedLastNameField.getText().toString();

                        EditText invitedFirstNameField = view.findViewById(R.id.last_name);
                        invitedLastName = invitedFirstNameField.getText().toString();

                        if(validInput(invitedEmail)){
                            Log.i("Invited Email ", "was valid gmail");
                        }
                        else{
                            invitedEmail = "ERROR";
                            getDialog().cancel();
                        }
                        listener.onDialogPositiveClick(TeamInvitationDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(TeamInvitationDialogFragment.this);
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public static boolean validInput(String email) {
        String expression = "^[\\w.+\\-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        //return matcher.matches(); COMMENTED 3/4/2020 8:17 TO ALLOW UCSD EMAIL CHECKS
        return true;
    }
}