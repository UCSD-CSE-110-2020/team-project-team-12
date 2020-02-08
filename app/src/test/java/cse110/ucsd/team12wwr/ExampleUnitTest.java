package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.Spinner;

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
public class ExampleUnitTest {

    private Intent intent, intent2;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), StartPage.class);
        intent2 = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
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

    // Test going into main activity
    @Test
    public void testValidHeightMainPage() {
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


//            ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
//            int totalHeight = mainActivityTestRule.getActivity().totalHeight;

//            assertEquals(totalHeight, 65);

           //ActivityScenario<MainActivity> scenario1 = AccessibilityInfoCheck
        });

//
//        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(intent2);
//        scenario2.onActivity(activity ->{

//            int totalHeight = activity
//        });
    }
}

/**

 private Intent intent;
 private long nextStepCount;
 private long goodSteps;

 @Before
 public void setUp() {
 FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
 intent = new Intent(ApplicationProvider.getApplicationContext(), StepCountActivity.class);
 intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
 }

 @Test
 public void testUpdateStepsButton() {
 nextStepCount = 1337;

 ActivityScenario<StepCountActivity> scenario = ActivityScenario.launch(intent);
 scenario.onActivity(activity -> {
 TextView textSteps = activity.findViewById(R.id.textSteps);
 Button btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
 assertThat(textSteps.getText().toString()).isEqualTo("steps will be shown here");
 btnUpdateSteps.performClick();
 assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
 //assertThat()
 });
 }
 */