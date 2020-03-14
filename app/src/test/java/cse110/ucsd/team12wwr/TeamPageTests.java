package cse110.ucsd.team12wwr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;

import androidx.fragment.app.DialogFragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cse110.ucsd.team12wwr.teamlist.TeamScreenRowItem;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TeamPageTests {
    private Intent mainIntent, teamPageIntent;
    private ActivityTestRule<MainActivity> mainActivityActivityTestRule;
    private ActivityTestRule<TeamScreen> teamScreenActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        teamPageIntent = new Intent(ApplicationProvider.getApplicationContext(), TeamScreen.class );
        mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
        teamScreenActivityTestRule = new ActivityTestRule<>(TeamScreen.class);
    }

    @Test
    public void testDefaultState() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainIntent);
        scenario.onActivity(activity -> {
            teamScreenActivityTestRule.launchActivity(teamPageIntent);
            int numOfItems = teamScreenActivityTestRule.getActivity().rowItems.size();
            assertEquals(numOfItems, 0);
            teamScreenActivityTestRule.finishActivity();
        });
    }

    @Test
    public void testAddMembers() {
        teamScreenActivityTestRule.launchActivity(teamPageIntent);
        teamScreenActivityTestRule.getActivity().createUsers();
        assertEquals(teamScreenActivityTestRule.getActivity().rowItems.size(), 2);
    }

    @Test
    public void testInviteMember() {
        teamScreenActivityTestRule.launchActivity(teamPageIntent);

        FloatingActionButton fab = teamScreenActivityTestRule.getActivity().findViewById(R.id.add_fab);
        fab.performClick();

        DialogFragment frag = (DialogFragment) teamScreenActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag("open");
        LayoutInflater inflater = frag.requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invitation,null);

        TextView email = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.username);
        TextView firstName = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.first_name);
        TextView lastName = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.last_name);

        email.setText("gmail@gmail.com");
        firstName.setText("Jane");
        lastName.setText("Hartono");

        ShadowAlertDialog.getLatestAlertDialog().getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        assertEquals("Invite sent to Jane Hartono" , ShadowToast.getTextOfLatestToast());

        boolean containsUser = false;
        for (TeamScreenRowItem rowItem : teamScreenActivityTestRule.getActivity().rowItems) {
            if (rowItem.getMemberName().equals("Jane Hartono")) {
                containsUser = true;
            }
        }

        assertEquals(true, containsUser);

        teamScreenActivityTestRule.finishActivity();
    }

    @Test
    public void testCancelInvite() {
        teamScreenActivityTestRule.launchActivity(teamPageIntent);

        FloatingActionButton fab = teamScreenActivityTestRule.getActivity().findViewById(R.id.add_fab);
        fab.performClick();

        DialogFragment frag = (DialogFragment) teamScreenActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag("open");
        LayoutInflater inflater = frag.requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invitation,null);

        TextView email = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.username);
        TextView firstName = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.first_name);
        TextView lastName = ShadowAlertDialog.getLatestAlertDialog().findViewById(R.id.last_name);

        email.setText("gmail@gmail.com");
        firstName.setText("Jane");
        lastName.setText("Hartono");

        ShadowAlertDialog.getLatestAlertDialog().getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

        assertEquals("Invite cancelled!" , ShadowToast.getTextOfLatestToast());

        boolean containsUser = false;
        for (TeamScreenRowItem rowItem : teamScreenActivityTestRule.getActivity().rowItems) {
            if (rowItem.getMemberName().equals("Jane Hartono")) {
                containsUser = true;
            }
        }

        assertEquals(false, containsUser);

        teamScreenActivityTestRule.finishActivity();
    }
}

