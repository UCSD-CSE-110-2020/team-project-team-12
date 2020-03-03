package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;
import cse110.ucsd.team12wwr.database.WalkDao;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class RouteDetailTests {

    private Intent routeIntent;
    private ActivityTestRule<RouteDetailsPage> routeInfoActivityActivityTestRule;
    private Intent mainIntent;
    private ActivityTestRule<MainActivity> mainActivityTestRule;
    private ActivityTestRule<IntentionalWalkActivity> intentionalWalkActivityActivityTestRule;

    @Before
    public void setUp(){
        routeIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class);
        routeInfoActivityActivityTestRule = new ActivityTestRule<>(RouteDetailsPage.class);
    }

    @Test
    public void testDefaultState() {
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        TextView title = routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.route_title_detail);
        title.setText("Title");
        assertEquals(true, (routeInfoActivityActivityTestRule.getActivity().extractString(title)).equals("Title"));
        routeInfoActivityActivityTestRule.finishActivity();
    }

//    @Test
//    public void testLaunchRouteInfoActivity() {
//        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
//        routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.route_title_detail);
//        routeInfoActivityActivityTestRule.getActivity().launchRouteInfoActivity();
//        assertEquals(true, routeInfoActivityActivityTestRule.getActivity().getIntent().hasExtra("ROUTE_TITLE"));
//
//    }
//
//    @Test
//    public void testLaunchIntentionalActivity() {
//        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
//        routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.route_title_detail);
//        routeInfoActivityActivityTestRule.getActivity().launchIntentionalActivity();
//        assertEquals(true, routeInfoActivityActivityTestRule.getActivity().getIntent().hasExtra("ROUTE_TITLE"));
//    }
}
