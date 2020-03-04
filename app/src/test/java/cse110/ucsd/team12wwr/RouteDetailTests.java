package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RouteDetailTests {

    private Intent routeIntent;
    private ActivityTestRule<RouteDetailsPage> routeDetailsPageTestRule;
    private ActivityTestRule<RouteInfoActivity> routeInfoActivityTestRule;
    private ActivityTestRule<IntentionalWalkActivity> intentionalWalkActivityActivityTestRule;

    @Before
    public void setUp(){
        MainActivity.unitTestFlag = true;
        routeIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class);
        routeDetailsPageTestRule = new ActivityTestRule<>(RouteDetailsPage.class);
        routeInfoActivityTestRule = new ActivityTestRule<>(RouteInfoActivity.class);
        intentionalWalkActivityActivityTestRule = new ActivityTestRule<>(IntentionalWalkActivity.class);
    }

    @Test
    public void testDefaultState() {
        routeDetailsPageTestRule.launchActivity(routeIntent);
        TextView title = routeDetailsPageTestRule.getActivity().findViewById(R.id.route_title_detail);
        title.setText("Title");
        assertEquals(true, (routeDetailsPageTestRule.getActivity().extractString(title)).equals("Title"));
        routeDetailsPageTestRule.finishActivity();
    }

//    @Test
//    public void testLaunchRouteInfoActivity() {
//        routeDetailsPageTestRule.launchActivity(routeIntent);
//        routeDetailsPageTestRule.getActivity().findViewById(R.id.route_title_detail);
//        routeDetailsPageTestRule.getActivity().launchRouteInfoActivity();
//
//        routeInfoActivityTestRule.launchActivity(new Intent(ApplicationProvider.getApplicationContext(), RouteInfoActivity.class));
//        assertEquals(true, routeInfoActivityTestRule.getActivity().getIntent().hasExtra("ROUTE_TITLE"));
//
//    }
//
//    @Test
//    public void testLaunchIntentionalActivity() {
//        routeDetailsPageTestRule.launchActivity(routeIntent);
//        routeDetailsPageTestRule.getActivity().findViewById(R.id.route_title_detail);
//        routeDetailsPageTestRule.getActivity().launchIntentionalActivity();
//
//        intentionalWalkActivityActivityTestRule.launchActivity(new Intent(ApplicationProvider.getApplicationContext(), IntentionalWalkActivity.class));
//        assertEquals(true, intentionalWalkActivityActivityTestRule.getActivity().getIntent().hasExtra("ROUTE_TITLE"));
//    }
}
