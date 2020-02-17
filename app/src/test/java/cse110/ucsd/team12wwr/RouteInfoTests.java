package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
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
    public void setUp() {
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
    public void testEasyButton() {
        // Retrieve booleans from the page
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        Boolean isEasy = routeInfoActivityActivityTestRule.getActivity().isEasy;
        Boolean isModerate = routeInfoActivityActivityTestRule.getActivity().isModerate;
        Boolean isHard = routeInfoActivityActivityTestRule.getActivity().isHard;

        Button easyBtn = routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.easy_btn);
        assertFalse(isEasy);
        assertFalse(isModerate);
        assertFalse(isHard);

        // Test Easy Button
        easyBtn.performClick();
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertTrue(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(easyBtn.getCurrentTextColor(), Color.WHITE);
        assertEquals(((ColorDrawable) easyBtn.getBackground()).getColor(), Color.DKGRAY);

        easyBtn.performClick();
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(easyBtn.getCurrentTextColor(), Color.BLACK);
        assertEquals(easyBtn.getBackground(),
                routeInfoActivityActivityTestRule.getActivity().defaultColor);

    }

    @Test
    public void testHardButton() {
        // Retrieve booleans from the page
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        Boolean isEasy = routeInfoActivityActivityTestRule.getActivity().isEasy;
        Boolean isModerate = routeInfoActivityActivityTestRule.getActivity().isModerate;
        Boolean isHard = routeInfoActivityActivityTestRule.getActivity().isHard;

        Button hardBtn = routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.hard_btn);
        assertFalse(isEasy);
        assertFalse(isModerate);
        assertFalse(isHard);

        // Test Hard Button
        hardBtn.performClick();
        assertTrue(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(hardBtn.getCurrentTextColor(), Color.WHITE);
        assertEquals(((ColorDrawable) hardBtn.getBackground()).getColor(), Color.DKGRAY);

        hardBtn.performClick();
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(hardBtn.getCurrentTextColor(), Color.BLACK);
        assertEquals(hardBtn.getBackground(),
                routeInfoActivityActivityTestRule.getActivity().defaultColor);

    }

    @Test
    public void testModerateButton() {
        // Retrieve booleans from the page
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        Boolean isEasy = routeInfoActivityActivityTestRule.getActivity().isEasy;
        Boolean isModerate = routeInfoActivityActivityTestRule.getActivity().isModerate;
        Boolean isHard = routeInfoActivityActivityTestRule.getActivity().isHard;

        Button modBtn = routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.hard_btn);
        assertFalse(isEasy);
        assertFalse(isModerate);
        assertFalse(isHard);

        // Test Hard Button
        modBtn.performClick();
        assertTrue(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(modBtn.getCurrentTextColor(), Color.WHITE);
        assertEquals(((ColorDrawable) modBtn.getBackground()).getColor(), Color.DKGRAY);

        modBtn.performClick();
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isHard);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isModerate);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isEasy);
        assertEquals(modBtn.getCurrentTextColor(), Color.BLACK);
        assertEquals(modBtn.getBackground(),
                routeInfoActivityActivityTestRule.getActivity().defaultColor);
    }

    @Test
    public void testFavoriteButton() {
        routeInfoActivityActivityTestRule.launchActivity(routeIntent);
        Button isFav = (Button) routeInfoActivityActivityTestRule.getActivity().findViewById(R.id.favoriteCheckBtn);
        assertFalse(routeInfoActivityActivityTestRule.getActivity().isFavorite);
        isFav.performClick();
        assertTrue(routeInfoActivityActivityTestRule.getActivity().isFavorite);
    }


}
