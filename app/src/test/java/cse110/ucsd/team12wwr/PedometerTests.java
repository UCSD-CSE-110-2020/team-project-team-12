package cse110.ucsd.team12wwr;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;

import java.util.concurrent.TimeoutException;

import cse110.ucsd.team12wwr.fitness.FitnessService;
import cse110.ucsd.team12wwr.fitness.FitnessServiceFactory;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class PedometerTests {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";


    ShadowApplication shadowApplication;
    ComponentName expectedComponentName;
    Binder expectedBinder;

    private Intent intent;
    private long nextStepCount;

    ServiceTestRule serviceRule;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        intent.putExtra(FITNESS_SERVICE_KEY, TEST_SERVICE);

        serviceRule = new ServiceTestRule();


    }

    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        //Intent serviceIntent =
          //      new Intent(ApplicationProvider.getApplicationContext(), PedometerService.class);
        Intent serviceIntent =
              new Intent(ApplicationProvider.getApplicationContext(), PedometerService.class);

        // Data can be passed to the service via the Intent.
        //serviceIntent.putExtra(PedometerService.LocalService.SEED_KEY, 42L);

        // Bind the service and grab a reference to the binder.
        System.out.println(serviceIntent);
        IBinder binder = serviceRule.bindService(serviceIntent);
        System.out.println(binder);

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        //PedometerService.LocalService localService = (PedometerService.LocalService)binder;
        //PedometerService localService = ((PedometerService.LocalService) binder).getService();
        PedometerService.LocalService localService = ((PedometerService.LocalService)binder);

        PedometerService pedoService = localService.getService();

        // Verify that the service is working correctly.
        assertThat(pedoService.tester1(5)==6);
    }



    @Test
    public void testConnect() throws Exception {
        shadowApplication.setComponentNameAndServiceForBindService(expectedComponentName, expectedBinder);
        //assertTrue(Robolectric.buildActivity(MainActivity.class).create().get().Connect());
    }

        //assertTrue(Robolectric.buildActivity(MainActivity.class).create().get().Connect());

    @Test
    public void testNumberDisplayedCorrectly() {
        //nextStepCount = 1337;

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textStep = activity.findViewById(R.id.num_steps);
            long stepCount = activity.numSteps;
            //Button btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
            //assertThat(textStep.getText().toString()).isEqualTo("steps will be shown here");
            //btnUpdateSteps.performClick();
            //System.out.println(textStep.getText());
            assertThat(textStep.getText().toString()).isEqualTo(String.valueOf(stepCount));
        });
    }


    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;

        public TestFitnessService(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            mainActivity.setStepCount(mainActivity.numSteps);
        }
    }
}
