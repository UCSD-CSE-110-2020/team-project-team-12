package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.DialogFragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        FloatingActionButton fab = teamScreenActivityTestRule.getActivity().findViewById(R.id.floatingActionButton);
        fab.performClick();

        DialogFragment frag = (DialogFragment) teamScreenActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag("open");
        LayoutInflater inflater = frag.requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invitation,null);

        TextView email = view.findViewById(R.id.username);
        TextView firstName = view.findViewById(R.id.first_name);
        TextView lastName = view.findViewById(R.id.last_name);

        email.setText("gmail@gmail.com");
        firstName.setText("Jane");
        lastName.setText("Hartono");

        assertEquals("Jane", firstName.getText().toString());
        assertEquals("Hartono", lastName.getText().toString());
        assertEquals("gmail@gmail.com", email.getText().toString());

        teamScreenActivityTestRule.finishActivity();
    }
}
