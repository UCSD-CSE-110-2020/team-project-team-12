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

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import cse110.ucsd.team12wwr.database.Route;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class RouteInfoTests {

    // Test that when we don't input something into the fields, it won't allow us
    // Test favorite Button
    // Test easy, hard, moderate button
    // Test setting spinners

    // Test database
    // Entering items into the data base eg only name
    // yes favorite, no favorite, etc.

    private Intent routeIntent;
    private ActivityTestRule<RouteInfoActivity> routeInfoActivityActivityTestRule;

    @Before
    public void setUp(){
        routeIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteInfoActivity.class);
        routeInfoActivityActivityTestRule = new ActivityTestRule<>(RouteInfoActivity.class);
    }

    @Test
    public void testNoEntry() {
        ActivityScenario<RouteInfoActivity> scenario = ActivityScenario.launch(routeIntent);
        scenario.onActivity(activity -> {
            Button saveBtn = activity.findViewById(R.id.save_btn);
            EditText titleField = activity.findViewById(R.id.title_text);

            saveBtn.performClick();
            assertNotNull(titleField.getError());
        });
    }

    @Test
    public void testEnterOnlyStartLocation() {
        ActivityScenario<RouteInfoActivity> scenario = ActivityScenario.launch(routeIntent);
        scenario.onActivity(activity -> {
            Button saveBtn = activity.findViewById(R.id.save_btn);
            EditText titleField = activity.findViewById(R.id.title_text);
            EditText startField = activity.findViewById(R.id.start_text);

            startField.setText("Death Valley");
            assertNotNull(startField);
            saveBtn.performClick();
            assertNotNull(titleField.getError());

        });
    }

    @Test
    public void testDifficultyButtons() {
        // Retrieve booleans from the page
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        Boolean isEasy = routeInfoActivityActivityTestRule.getActivity().isEasy;
        Boolean isModerate = routeInfoActivityActivityTestRule.getActivity().isModerate;
        Boolean isHard = routeInfoActivityActivityTestRule.getActivity().isHard;

        Button easyBtn = routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.easy_btn);
        assertFalse(isEasy);
        assertFalse(isModerate);
        assertFalse(isHard);

        easyBtn.performClick();
        assertFalse(isHard);
        assertFalse(isModerate);
//        assertTrue(isEasy);
        assertEquals(easyBtn.getTextColors(), "#000000");
//        ActivityScenario<RouteInfoActivity> scenario = ActivityScenario.launch(routeIntent);
//        scenario.onActivity(activity -> {
//            /* Default should be all false */
//            assertFalse(isEasy);
//            assertFalse(isModerate);
//            assertFalse(isHard);
//
//            Button easyBtn = activity.findViewById(R.id.easy_btn);
//            easyBtn.performClick();
//            assertFalse(isHard);
//            assertFalse(isModerate);
//            assertTrue(isEasy);
//            assertEquals(easyBtn.getTextColors(), "#000000");
//        });
    }
}
