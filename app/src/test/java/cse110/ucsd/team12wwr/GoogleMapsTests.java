package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cse110.ucsd.team12wwr.firebase.Route;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GoogleMapsTests {
    private Intent routeDetailsPageIntent;
    private ActivityTestRule<RouteDetailsPage> routeDetailsPageTestRule;
    private Route ownRoute, teamRoute;
    private RouteDetailsPage activity;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        routeDetailsPageIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class );
        routeDetailsPageTestRule = new ActivityTestRule<>(RouteDetailsPage.class);

        routeDetailsPageTestRule.launchActivity(routeDetailsPageIntent);
        activity= routeDetailsPageTestRule.getActivity();
    }

    @Test
    public void testLaunchGoogleMaps() {
        TextView startPoint = activity.findViewById(R.id.start_textview);
        startPoint.setText("Starting Point: McDonalds");
        startPoint.performClick();
        Intent googleMapsIntent = activity.launchGoogleMaps();
        assertNotNull(googleMapsIntent);
    }

    @Test
    public void testLaunchGoogleMapsUnmodifiedStartingPoint() {
        TextView startPoint = activity.findViewById(R.id.start_textview);
        startPoint.performClick();
        Intent googleMapsIntent = activity.launchGoogleMaps();
        assertEquals(null, googleMapsIntent);
    }
}
