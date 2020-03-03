package cse110.ucsd.team12wwr;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;
import cse110.ucsd.team12wwr.database.WalkDao;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class IntentionalWalkTests {
    private Intent intentionalWalkIntent, mainIntent;
    private ActivityTestRule<mockMainActivity> mainActivityTestRule;
    private ActivityTestRule<IntentionalWalkActivity> intentionalWalkActivityActivityTestRule;

    @Before
    public void setUp() {
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), mockMainActivity.class);
    }

    @Test
    public void testWalkDatabase() {
        ActivityScenario<mockMainActivity> scenario = ActivityScenario.launch(mainIntent);
        scenario.onActivity(activity -> {
            WWRDatabase db = WWRDatabase.getInstance(activity);

            Route newRoute = new Route();
            newRoute.name = "Solar Path";
            db.routeDao().insertAll(newRoute);

            Walk newEntry = new Walk();
            newEntry.time = System.currentTimeMillis();
            newEntry.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
            newEntry.steps = String.format("%d", 1337);
            newEntry.distance = String.format("%.2f mi", 13.09);
            newEntry.routeName = "Solar Path";

            db.walkDao().insertAll(newEntry);

            Walk newestWalk = db.walkDao().findNewestEntry();
            assertEquals("1337", newestWalk.steps);
            assertEquals("13.09 mi", newestWalk.distance);
            assertEquals("12:34:56", newestWalk.duration);
            assertEquals("Solar Path", newestWalk.routeName);
        });
    }

//    @Test
//    public void testDisplayWalkStats() {
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainIntent);
//        scenario.onActivity(activity -> {
//            TextView stepsWalkText = activity.findViewById(R.id.text_steps_value);
//            TextView distWalkText = activity.findViewById(R.id.text_distance_value);
//            TextView timeWalkText = activity.findViewById(R.id.text_time_value);
//
//            assertEquals("0", stepsWalkText.getText().toString());
//            assertEquals("0 mi", distWalkText.getText().toString());
//            assertEquals("00:00:00", timeWalkText.getText().toString());
//
//            ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);
//            databaseWriteExecutor.execute(() -> {
//                WWRDatabase walkDb = WWRDatabase.getInstance(activity);
//                WalkDao dao = walkDb.walkDao();
//
//                Walk newEntry = new Walk();
//                newEntry.time = System.currentTimeMillis();
//                newEntry.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
//                newEntry.steps = String.format("%d", 1337);
//                newEntry.distance = String.format("%.2f mi", 13.09);
//
//                dao.insertAll(newEntry);
//            });
//
//        });
//
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            TextView stepsWalkText = activity.findViewById(R.id.text_steps_value);
//            TextView distWalkText = activity.findViewById(R.id.text_distance_value);
//            TextView timeWalkText = activity.findViewById(R.id.text_time_value);
//
//            assertEquals("1337", stepsWalkText.getText().toString());
//            assertEquals("13.09 mi", distWalkText.getText().toString());
//            assertEquals("12:34:56", timeWalkText.getText().toString());
//        });
//    }
}