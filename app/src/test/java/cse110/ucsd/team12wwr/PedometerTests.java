package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import cse110.ucsd.team12wwr.fitness.FitnessService;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class PedometerTests {
    private MainActivity mainActivity;
    private TestFitnessService testService;
    private Intent intent;
    TestPedometerService testPedService;



    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        testPedService = new TestPedometerService();
    }
    @Test
    public void testFitnessStepValue() {
        testService = new TestFitnessService(mainActivity, 42);
        assertEquals(testService.getStepValue(), 42);
    }

    @Test
    public void testStepsDisplayedCorrectlyInMainActivity() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            testService = new TestFitnessService(activity, 69);
            testService.updateStepCount();
            TextView displayedSteps = activity.findViewById(R.id.num_steps);
            assert(Long.parseLong(displayedSteps.getText().toString()) == 69);

        });
    }
    @Test
    public void testStepsUpdatedInMainDisplayANDService() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            testService = new TestFitnessService(activity, 420);
            testPedService.beginStepTracking(testService);
            assert(testPedService.getCurrentSteps() == 420);
            TextView displayedSteps = activity.findViewById(R.id.num_steps);
            assert(Long.parseLong(displayedSteps.getText().toString()) == 420);
        });
    }


    private class TestPedometerService extends PedometerService{
        private long currentSteps;
        @Override
        public void beginStepTracking(FitnessService fitnessService){
            fitnessService.updateStepCount();
            currentSteps = fitnessService.getStepValue();
        }
        @Override
        public long getCurrentSteps(){
            return currentSteps;
        }

    }
    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;
        private long steps;

        private TestFitnessService(MainActivity mainActivity, long stepValue) {
            this.mainActivity = mainActivity;
            steps = stepValue;
        }

        @Override
        public long getStepValue(){
            return steps;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {

        }

        @Override
        public void updateStepCount() {
            mainActivity.setStepCount(steps);
        }

        @Override
        public boolean getSubscribed(){
            return true;
        }
    }
}
