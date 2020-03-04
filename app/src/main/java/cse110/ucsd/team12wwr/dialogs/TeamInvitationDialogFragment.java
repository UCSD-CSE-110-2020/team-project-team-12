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

public class TeamInvitationDialogFragment extends DialogFragment {

    public interface InviteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private String invitedEmail = "";
    private String invitedLastName = "";
    private String invitedFirstName = "";

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

    InviteDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (InviteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.i("TeamInvitationDialogFragment.onAttach() ", "Activity doesn't implement interface");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_invitation,null);

        //AlertDialog dialog1 = (AlertDialog) getDialog();
        //dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Invite!", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialog Input: ", "Invite sent!");

                        EditText invitedEmailField = view.findViewById(R.id.username);
                        invitedEmail = invitedEmailField.getText().toString();

                        EditText invitedLastNameField = view.findViewById(R.id.first_name);
                        invitedLastName = invitedLastNameField.getText().toString();

                        EditText invitedFirstNameField = view.findViewById(R.id.last_name);
                        invitedFirstName = invitedFirstNameField.getText().toString();

                        listener.onDialogPositiveClick(TeamInvitationDialogFragment.this);

                        // if(not valid gmail) cancel
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialog Input: ", "Cancelled");
                        listener.onDialogNegativeClick(TeamInvitationDialogFragment.this);
                        getDialog().cancel();
                    }
                });

        return builder.create();
    }
}