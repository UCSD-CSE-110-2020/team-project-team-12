package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class SaveHeightTest {
    private Intent intent, mainIntent;
    private ActivityTestRule<MainActivity> mainActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        intent = new Intent(ApplicationProvider.getApplicationContext(), StartPage.class);
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        intent.putExtras(mainIntent);
        mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    }

    @Test
    public void testSaveHeight() {
        mainActivityTestRule.launchActivity(mainIntent);
        SharedPreferences spf = mainActivityTestRule.getActivity().spf;
        SharedPreferences.Editor editor = spf.edit();
        SharedPreferences spf2 = mainActivityTestRule.getActivity().spf2;

        editor.putInt("FEET", 7);
        editor.putInt("INCHES", 3);
        editor.apply();

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainIntent);
        scenario.onActivity(activity -> {

            //Button takeStep = activity.findViewById(R.id.btn_debug_increment_steps);
            TextView dist = (TextView) activity.findViewById(R.id.num_miles);

            assertEquals(spf.getInt("FEET", 0), 7);
            assertEquals(spf.getInt("INCHES", 0), 3);

            //assertNotNull(takeStep);

            //takeStep.performClick();
            //assertEquals(spf2.getInt("totalSteps", 0), 100);
            activity.setStepCount(100);
            assertEquals(dist.getText().toString(), "0.06");

            //takeStep.performClick();
            //assertEquals(spf2.getInt("totalSteps", 0), 200);
            activity.setStepCount(200);
            assertEquals(dist.getText().toString(), "0.11");

        });
    }

    @Test
    public void testHeightIsSaved() {

        mainActivityTestRule.launchActivity(mainIntent);

        SharedPreferences prefs = mainActivityTestRule.getActivity().prefs;
        SharedPreferences spf = mainActivityTestRule.getActivity().spf;
        SharedPreferences.Editor editor = spf.edit();

        assertEquals(prefs.getBoolean("HAVE_HEIGHT", false), true);

        assertEquals(0 , spf.getInt("FEET", 0));
        assertEquals(0 , spf.getInt("INCHES", 0));

        editor.putInt("FEET", 5);
        editor.putInt("INCHES", 6);
        editor.apply();

        mainActivityTestRule.finishActivity();
        mainActivityTestRule.getActivityResult();
        mainActivityTestRule.launchActivity(mainIntent);

        prefs = mainActivityTestRule.getActivity().prefs;

        assertEquals(prefs.getBoolean("HAVE_HEIGHT", false), true);
        assertEquals(5 , spf.getInt("FEET", 0));
        assertEquals(6 , spf.getInt("INCHES", 0));

    }


}
