package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.chrono.MinguoChronology;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class PedometerTests {
    private Intent intent;


    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
    }


    @Test
    public void testgFitStepsEqualActivitySteps() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            assertEquals(activity.gFitUtil.getStepValue(), activity.numSteps);
        });
    }
    @Test

    public void testnumStepsEqualDisplayedSteps() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textStep = activity.findViewById(R.id.num_steps);
            assertEquals(Long.parseLong(textStep.getText().toString()), activity.numSteps);
        });
    }
}
