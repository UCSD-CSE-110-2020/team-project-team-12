package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.ContentView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TeamPageTests {

    private Intent intent, mainIntent, teamPageIntent;
    private ActivityTestRule<MainActivity> mainActivityActivityTestRule;
    private ActivityTestRule<TeamScreen> teamScreenActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        mainIntent =
                new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        teamPageIntent =
                new Intent(ApplicationProvider.getApplicationContext(), TeamScreen.class );
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



//    @Test
//    public void testInviteMember() {
//        teamScreenActivityTestRule.launchActivity(teamPageIntent);
////        FloatingActionButton fab = teamScreenActivityTestRule.getActivity().findViewById(R.id.floatingActionButton);
////        fab.performClick();
//        teamScreenActivityTestRule.finishActivity();
//
//        ActivityScenario<TeamScreen> scenario = ActivityScenario.launch(teamPageIntent);
//        scenario.onActivity(activity -> {
//            FloatingActionButton fab = teamScreenActivityTestRule.getActivity().findViewById(R.id.floatingActionButton);
//            fab.performClick();
//            TextView email = activity.findViewById(R.id.username);
//            email.setText("gmail@gmail.com");
//            TextView firstName = activity.findViewById(R.id.first_name);
//            firstName.setText("Jane");
//            TextView lastName = activity.findViewById(R.id.last_name);
//            lastName.setText("Joe");
//            assertEquals(firstName.getText(), "Jane");
//            assertEquals(lastName.getText(), "Joe");
//            assertEquals(email.getText(), "gmail@gmail.com");
//        });
//    }




}
