package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageItemInfo;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.Schedule;
import cse110.ucsd.team12wwr.firebase.ScheduleDao;
import cse110.ucsd.team12wwr.firebase.Walk;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ScheduledWalkTest {

    private Intent proposedWalkIntent, mainActivityIntent;
    private ActivityTestRule<ProposedWalkScreen> proposedWalkActivityActivityTestRule;
    private ActivityTestRule<MainActivity> mainActivityActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        proposedWalkIntent = new Intent(ApplicationProvider.getApplicationContext(), ProposedWalkScreen.class);
        mainActivityIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        proposedWalkActivityActivityTestRule = new ActivityTestRule<>(ProposedWalkScreen.class);
        mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    }

    @Test
    public void testHasNoScheduleWalk() {

        ActivityScenario<ProposedWalkScreen> scenario = ActivityScenario.launch(proposedWalkIntent);
        scenario.onActivity(activity -> {

            ScrollView details = (ScrollView) activity.findViewById(R.id.scrollable_proposal);
            assertEquals(View.GONE, details.getVisibility());

        });
    }

    @Test
    public void testHasScheduledWalk() {
//        Schedule mockScheduleWalk = new Schedule();
////        mockScheduleWalk.isScheduled = false;
////        mockScheduleWalk.proposerUserID = "jane@gmail.com";
////        mockScheduleWalk.teamID = "Team A";
////        mockScheduleWalk.routeName = "Fun Route";
////        mockScheduleWalk.time = "1 : 03 PM";
////        mockScheduleWalk.date = "04/20/2020";
////        HashMap<String, Schedule.Vote> map = new HashMap<>();
////        map.put("jane@gmail.com", Schedule.Vote.ABSTAINED);
////        map.put("nicholasalimit@gmail.com", Schedule.Vote.ACCEPTED);
////        mockScheduleWalk.userVoteMap = map;
        Route mockRoute = new Route();
        mockRoute.userID = "nicholasalimit@gmail.com";
        mockRoute.startingPoint = "Canyon Vista";
        mockRoute.name = "Fun Route";
        mockRoute.notes = "Oh my god";

        ActivityScenario<ProposedWalkScreen> scenario = ActivityScenario.launch(proposedWalkIntent);
        scenario.onActivity(activity2 -> {
            activity2.populateRouteInfo(mockRoute);
            TextView startPoint = activity2.findViewById(R.id.proposed_start_textview);
            assertEquals("Starting Point: Canyon Vista", startPoint.getText().toString());
            TextView notes = activity2.findViewById(R.id.proposed_notes_content);
            assertEquals("Oh my god", notes.getText().toString());

        });

    }
}
