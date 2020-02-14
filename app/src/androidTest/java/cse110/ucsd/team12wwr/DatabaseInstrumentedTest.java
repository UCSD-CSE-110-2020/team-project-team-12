package cse110.ucsd.team12wwr;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private WWRDatabase db;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, WWRDatabase.class).build();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testInsertSingleWalk() {
        Walk newEntry = new Walk();
        newEntry.time = System.currentTimeMillis();
        newEntry.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        newEntry.steps = String.format("%d", 1337);
        newEntry.distance = String.format("%.2f mi", 13.09);
        db.walkDao().insertAll(newEntry);

        Walk newestWalk = db.walkDao().findNewestEntry();
        assertEquals("1337", newestWalk.steps);
        assertEquals("13.09 mi", newestWalk.distance);
        assertEquals("12:34:56", newestWalk.duration);
    }

    @Test
    public void testNewestMultipleWalks() {
        int NUM_OF_ENTRIES = 3;
        Walk[] walkArr = new Walk[NUM_OF_ENTRIES];
        for (int i = 0; i < NUM_OF_ENTRIES; i++) {
            walkArr[i] = new Walk();
        }

        walkArr[0].time = System.currentTimeMillis();
        walkArr[0].duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        walkArr[0].steps = String.format("%d", 1337);
        walkArr[0].distance = String.format("%.2f mi", 13.09);

        walkArr[1].time = System.currentTimeMillis() + 100000;
        walkArr[1].duration = String.format("%02d:%02d:%02d", 21, 43, 65);
        walkArr[1].steps = String.format("%d", 7331);
        walkArr[1].distance = String.format("%.2f mi", 31.09);

        walkArr[2].time = System.currentTimeMillis() - 100000;
        walkArr[2].duration = String.format("%02d:%02d:%02d", 34, 56, 78);
        walkArr[2].steps = String.format("%d", 888);
        walkArr[2].distance = String.format("%.2f mi", 11.11);
        db.walkDao().insertAll(walkArr[0], walkArr[1], walkArr[2]);

        Walk newestWalk = db.walkDao().findNewestEntry();
        assertEquals("7331", newestWalk.steps);
        assertEquals("31.09 mi", newestWalk.distance);
        assertEquals("21:43:65", newestWalk.duration);
    }

    @Test
    public void testInsertSingleRoute() {
        Route newEntry = new Route();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = Route.RouteType.LOOP;
        newEntry.hilliness = Route.Hilliness.FLAT;
        newEntry.surfaceType = Route.SurfaceType.STREETS;
        newEntry.evenness = Route.Evenness.EVEN_SURFACE;
        newEntry.difficulty = Route.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";
        db.routeDao().insertAll(newEntry);

        List<Route> routes = db.routeDao().retrieveAllRoutes();
        Route r = routes.get(0);
        assertEquals("Mission Hills Tour", r.name);
        assertEquals("Kufuerstendamm & Friedrichstrasse", r.startingPoint);
        assertEquals(Route.RouteType.LOOP, r.routeType);
        assertEquals(Route.Hilliness.FLAT, r.hilliness);
        assertEquals(Route.SurfaceType.STREETS, r.surfaceType);
        assertEquals(Route.Evenness.EVEN_SURFACE, r.evenness);
        assertEquals(Route.Difficulty.MODERATE, r.difficulty);
        assertEquals("This is a pretty dope route wanna do it again", r.notes);
    }

    @Test
    public void testAlphabeticalOrderRoutes() {
        int NUM_OF_ENTRIES = 3;
        Route[] routeArr = new Route[NUM_OF_ENTRIES];
        for (int i = 0; i < NUM_OF_ENTRIES; i++) {
            routeArr[i] = new Route();
        }

        Route newEntry = routeArr[0];
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = Route.RouteType.LOOP;
        newEntry.hilliness = Route.Hilliness.FLAT;
        newEntry.surfaceType = Route.SurfaceType.STREETS;
        newEntry.evenness = Route.Evenness.EVEN_SURFACE;
        newEntry.difficulty = Route.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";

        routeArr[1].name = "Hike to Mars";

        routeArr[2].name = "Zamba!";

        db.routeDao().insertAll(routeArr[0], routeArr[1], routeArr[2]);

        List<Route> routes = db.routeDao().retrieveAllRoutes();
        assertEquals("Hike to Mars", routes.get(0).name);
        assertEquals("Mission Hills Tour", routes.get(1).name);
        assertEquals("Zamba!", routes.get(2).name);
    }

    /* TODO: try specifying an empty primary key */
}
