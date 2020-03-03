package cse110.ucsd.team12wwr.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.TeamScreenActivity;

public class TeamInvitationDialogFragment extends DialogFragment {

    public interface InviteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
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



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_invitation, null))
                // Add action buttons
                .setPositiveButton("Invite!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialog Input: ", "Invite sent!");
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialog Input: ", "Cancelled");
                        //TeamScreenActivity.teamInvitationDialogFragment.this.getDialog().cancel();
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

