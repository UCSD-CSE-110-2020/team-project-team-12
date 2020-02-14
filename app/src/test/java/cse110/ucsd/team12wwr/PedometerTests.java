package cse110.ucsd.team12wwr;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;

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

//LITERALLY DO NONE OF THIS WE JUST NEED TO TWO TESTERS FOR USER STORY 1 THEY DON'T NEED TO BE WHETHER OR NOT GOOGLE FIT FUCKING CONNECTS NERD
//test setStepCount FOR SURE BECAUSE EZPZ
//we can do that with test fitness service just pass it a thing to set the count to so fake fitness.update{setstepcount(42)}
//as the before thing
//and then two tests that check that 42 is the value in numsteps
//and the other that checks 42 gets displayed to the text correctly


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
/*
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


 */

//we can probably do something close to this with like idfk

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;

        public TestFitnessService(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public long getStepValue(){
            return 0;
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

        @Override
        public boolean getSubscribed(){
            return true;
        }
    }
}
