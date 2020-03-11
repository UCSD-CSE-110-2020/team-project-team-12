package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cse110.ucsd.team12wwr.firebase.Walk;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class PreviouslyWalkedTests {

    private Intent routeDetailsIntent;
    private ActivityTestRule<RouteDetailsPage> routeDetailsActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        routeDetailsIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class);
        routeDetailsActivityTestRule = new ActivityTestRule<>(RouteDetailsPage.class);
    }

    @Test
    public void testIsVisibleRouteDetailsPage() {
        Walk mockVisibleCheck = new Walk();
        mockVisibleCheck.distance = "";
        mockVisibleCheck.duration = "not null";
        mockVisibleCheck.routeName = "";
        mockVisibleCheck.steps = "";
        mockVisibleCheck.time = System.currentTimeMillis();
        mockVisibleCheck.userID = "";

        ActivityScenario<RouteDetailsPage> scenario = ActivityScenario.launch(routeDetailsIntent);

        scenario.onActivity(activity -> {
            activity.populateWalkInfo(mockVisibleCheck);
            TextView checkmark = activity.findViewById(R.id.checkmark_detail);
            assertEquals(View.VISIBLE, checkmark.getVisibility());
        });
    }

    @Test
    public void testIsInvisibleRouteDetailsPage() {
        ActivityScenario<RouteDetailsPage> scenario = ActivityScenario.launch(routeDetailsIntent);

        scenario.onActivity(activity -> {
            TextView checkmark = activity.findViewById(R.id.checkmark_detail);
            assertEquals(View.INVISIBLE, checkmark.getVisibility());
        });
    }
}
