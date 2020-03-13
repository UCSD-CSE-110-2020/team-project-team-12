package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.Walk;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WalkingStatsTests {
    private Intent routeDetailsPageIntent;
    private ActivityTestRule<RouteDetailsPage> routeDetailsPageTestRule;
    private Route route;
    private RouteDetailsPage activity;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        routeDetailsPageIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class );
        routeDetailsPageTestRule = new ActivityTestRule<>(RouteDetailsPage.class);

        routeDetailsPageTestRule.launchActivity(routeDetailsPageIntent);
        activity= routeDetailsPageTestRule.getActivity();

        route = new Route();
        route.userID = "nicholasalimit@gmail.com";
        route.name = "Mission Hills Tour";
        route.startingPoint = "Mission";
        route.endingPoint = "Hills;";
        route.routeType = Route.RouteType.LOOP;
        route.hilliness = Route.Hilliness.HILLY;
        route.surfaceType = Route.SurfaceType.TRAIL;
        route.evenness = Route.Evenness.EVEN_SURFACE;
        route.difficulty = Route.Difficulty.MODERATE;
        route.notes = "This is a dope trail, me gusto";
        route.favorite = Route.Favorite.FAVORITE;

        SharedPreferences sharedPreferences = activity.getSharedPreferences("USER_ID", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL_ID", "nicholasalimit@gmail.com");
        editor.apply();
    }

    @Test
    public void testBothWalksOwnRouteExist() {
        List<Walk> walks = new ArrayList<>();

        Walk walk1 = new Walk();
        walk1.userID = "nicholasalimit@gmail.com";
        walk1.routeName = "Mission Hills Tour";
        walk1.duration = "11:13:15";
        walk1.distance = "5.21 mi";
        walk1.steps = "2350";
        walk1.time = System.currentTimeMillis();
        walks.add(walk1);

        Walk walk2 = new Walk();
        walk2.userID = "nessikomar@gmail.com";
        walk2.routeName = "Mission Hills Tour";
        walk2.duration = "01:03:05";
        walk2.distance = "8.33 mi";
        walk2.steps = "4221";
        walk2.time = System.currentTimeMillis();
        walks.add(walk2);

        activity.populateRouteInfo(route);
        activity.determineWalk(walks);

        TextView duration = activity.findViewById(R.id.total_time_detail);
        assertEquals("11:13:15", duration.getText().toString());

        TextView distance = activity.findViewById(R.id.dist_details);
        assertEquals("5.21 mi", distance.getText().toString());
    }

    @Test
    public void testTeamWalkOwnRouteExists() {
        List<Walk> walks = new ArrayList<>();

        Walk walk = new Walk();
        walk.userID = "nessikomar@gmail.com";
        walk.routeName = "Mission Hills Tour";
        walk.duration = "01:03:05";
        walk.distance = "8.33 mi";
        walk.steps = "4221";
        walk.time = System.currentTimeMillis();
        walks.add(walk);

        activity.populateRouteInfo(route);
        activity.determineWalk(walks);

        TextView duration = activity.findViewById(R.id.total_time_detail);
        assertEquals("01:03:05", duration.getText().toString());

        TextView distance = activity.findViewById(R.id.dist_details);
        assertEquals("8.33 mi", distance.getText().toString());
    }

    @Test
    public void testOwnWalkOwnRouteExists() {
        List<Walk> walks = new ArrayList<>();

        Walk walk = new Walk();
        walk.userID = "nicholasalimit@gmail.com";
        walk.routeName = "Mission Hills Tour";
        walk.duration = "11:13:15";
        walk.distance = "5.21 mi";
        walk.steps = "2350";
        walk.time = System.currentTimeMillis();
        walks.add(walk);

        activity.populateRouteInfo(route);
        activity.determineWalk(walks);

        TextView duration = activity.findViewById(R.id.total_time_detail);
        assertEquals("11:13:15", duration.getText().toString());

        TextView distance = activity.findViewById(R.id.dist_details);
        assertEquals("5.21 mi", distance.getText().toString());
    }
}
