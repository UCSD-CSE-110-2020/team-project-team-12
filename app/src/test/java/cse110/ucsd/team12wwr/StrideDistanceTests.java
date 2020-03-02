package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityInfoCheck;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StrideDistanceTests {

    private Intent intent, mainIntent;
    private ActivityTestRule<MainActivity> mainActivityTestRule;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), StartPage.class);
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        intent.putExtras(mainIntent);
        mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    }

    @Test
    public void testNoHeight() {
        ActivityScenario<StartPage> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Spinner feetSpin = activity.findViewById(R.id.feet_spinner);
            Spinner inchSpin = activity.findViewById(R.id.inch_spinner);
            Button enterBtn = activity.findViewById(R.id.enter_button);

            feetSpin.setSelection(0); // Sets to 0 feet
            inchSpin.setSelection(0); // Sets to 0 inches

            assertEquals(feetSpin.getSelectedItemPosition(), 0);
            assertEquals(inchSpin.getSelectedItemPosition(), 0);

            enterBtn.performClick();

            String toastMessage = ShadowToast.getTextOfLatestToast();
            assertNotNull(toastMessage);
        });
    }

    @Test
    public void testValidHeight() {
        ActivityScenario<StartPage> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Spinner feetSpin = activity.findViewById(R.id.feet_spinner);
            Spinner inchSpin = activity.findViewById(R.id.inch_spinner);
            Button enterBtn = activity.findViewById(R.id.enter_button);

            feetSpin.setSelection(5); // Sets to 5 feet
            inchSpin.setSelection(5); // Sets to 5 inches

            assertEquals(feetSpin.getSelectedItem(), 5);
            assertEquals(inchSpin.getSelectedItem(), 5);

            enterBtn.performClick();

            String toastMessage = ShadowToast.getTextOfLatestToast();
            assertNull(toastMessage);
        });
    }

    @Test
    public void testValidStrideLength() {
        mainActivityTestRule.launchActivity(mainIntent);
        SharedPreferences spf = mainActivityTestRule.getActivity().spf;
        SharedPreferences.Editor editor = spf.edit();

        editor.putInt("FEET", 5);
        editor.putInt("INCHES", 4);
        editor.apply();

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainIntent);
        scenario.onActivity(activity -> {

            //Button takeStep = activity.findViewById(R.id.btn_debug_increment_steps);
            TextView dist = (TextView) activity.findViewById(R.id.num_miles);
            TextView step = (TextView) activity.findViewById(R.id.num_steps);

            assertEquals(spf.getInt("FEET", 0), 5);
            assertEquals(spf.getInt("INCHES", 0), 4);

            //System.out.print(spf.getInt("INCHES", 0));
            //assertNotNull(takeStep);

            //takeStep.performClick();
            activity.setStepCount(100);
            assertEquals(step.getText().toString(), "100");
            assertEquals(dist.getText().toString(), "0.04");

            //takeStep.performClick();
            activity.setStepCount(200);
            assertEquals(step.getText().toString(), "200");
            assertEquals(dist.getText().toString(), "0.08");

        });
    }

    @Test
    public void testValidStrideLengthTestTwo() {
        mainActivityTestRule.launchActivity(mainIntent);
        SharedPreferences spf = mainActivityTestRule.getActivity().spf;
        SharedPreferences.Editor editor = spf.edit();

        SharedPreferences spf2 = mainActivityTestRule.getActivity().spf2;

        editor.putInt("FEET", 6);
        editor.putInt("INCHES", 11);
        editor.apply();

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainIntent);
        scenario.onActivity(activity -> {

            //Button takeStep = activity.findViewById(R.id.btn_debug_increment_steps);
            TextView dist = (TextView) activity.findViewById(R.id.num_miles);
            TextView step = (TextView) activity.findViewById(R.id.num_steps);

            assertEquals(spf.getInt("FEET", 0), 6);
            assertEquals(spf.getInt("INCHES", 0), 11);

            //assertNotNull(takeStep);

            //takeStep.performClick();
            activity.setStepCount(100);
            assertEquals(step.getText().toString(), "100");
            assertEquals(dist.getText().toString(), "0.05");

            //takeStep.performClick();
            activity.setStepCount(200);
            assertEquals(step.getText().toString(), "200");
            assertEquals(dist.getText().toString(), "0.11");

        });
    }
}

/**

 public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


 private Intent intent;
 private long nextStepCount;
 private long goodSteps;

 @Before
 public void setUp() {
 FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
 intent = new Intent(ApplicationProvider.getApplicationContext(), StepCountActivity.class);
 intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
 }
 */

/**
 @RunWith(AndroidJUnit4.class)
 public class NextActivityTest {

 @Rule
 public ActivityTestRule<NextActivity> activityRule
 = new ActivityTestRule<>(
 NextActivity.class,
 true,     // initialTouchMode
 false);   // launchActivity. False to customize the intent

 @Test
 public void intent() {
 Intent intent = new Intent();
 intent.putExtra("your_key", "your_value");

 activityRule.launchActivity(intent);

 // Continue with your test
 }
 }
 */