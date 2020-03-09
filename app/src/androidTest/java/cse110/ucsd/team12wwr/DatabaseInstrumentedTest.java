package cse110.ucsd.team12wwr;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import cse110.ucsd.team12wwr.roomdb.RoomDatabase;
import cse110.ucsd.team12wwr.roomdb.RoomRoute;
import cse110.ucsd.team12wwr.roomdb.RoomWalk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private RoomDatabase db;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, RoomDatabase.class).build();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testInsertSingleWalk() {
        RoomRoute missionHills = new RoomRoute();
        missionHills.name = "Mission Hills Tour";
        missionHills.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        db.routeDao().insertAll(missionHills);

        RoomWalk newEntry = new RoomWalk();
        newEntry.time = System.currentTimeMillis();
        newEntry.routeName = "Mission Hills Tour";
        newEntry.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        newEntry.steps = String.format("%d", 1337);
        newEntry.distance = String.format("%.2f mi", 13.09);
        db.walkDao().insertAll(newEntry);

        RoomWalk newestWalk = db.walkDao().findNewestEntry();
        assertEquals("1337", newestWalk.steps);
        assertEquals("13.09 mi", newestWalk.distance);
        assertEquals("12:34:56", newestWalk.duration);
        assertEquals("Mission Hills Tour", newestWalk.routeName);
    }

    @Test
    public void testNewestMultipleWalks() {
        RoomRoute missionHills = new RoomRoute();
        missionHills.name = "Mission Hills Tour";
        missionHills.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        db.routeDao().insertAll(missionHills);
        
        int NUM_OF_ENTRIES = 3;
        RoomWalk[] walkArr = new RoomWalk[NUM_OF_ENTRIES];
        for (int i = 0; i < NUM_OF_ENTRIES; i++) {
            walkArr[i] = new RoomWalk();
        }

        walkArr[0].time = System.currentTimeMillis();
        walkArr[0].routeName = "Mission Hills Tour";
        walkArr[0].duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        walkArr[0].steps = String.format("%d", 1337);
        walkArr[0].distance = String.format("%.2f mi", 13.09);

        walkArr[1].time = System.currentTimeMillis() + 100000;
        walkArr[1].routeName = "Mission Hills Tour";
        walkArr[1].duration = String.format("%02d:%02d:%02d", 21, 43, 65);
        walkArr[1].steps = String.format("%d", 7331);
        walkArr[1].distance = String.format("%.2f mi", 31.09);

        walkArr[2].time = System.currentTimeMillis() - 100000;
        walkArr[2].routeName = "Mission Hills Tour";
        walkArr[2].duration = String.format("%02d:%02d:%02d", 34, 56, 78);
        walkArr[2].steps = String.format("%d", 888);
        walkArr[2].distance = String.format("%.2f mi", 11.11);
        db.walkDao().insertAll(walkArr[0], walkArr[1], walkArr[2]);

        RoomWalk newestWalk = db.walkDao().findNewestEntry();
        assertEquals("7331", newestWalk.steps);
        assertEquals("31.09 mi", newestWalk.distance);
        assertEquals("21:43:65", newestWalk.duration);
    }

    @Test
    public void testFindWalksGivenRouteName() {
        RoomRoute missionHills = new RoomRoute();
        missionHills.name = "Mission Hills Tour";
        missionHills.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        db.routeDao().insertAll(missionHills);

        RoomWalk firstWalk = new RoomWalk();
        firstWalk.time = System.currentTimeMillis();
        firstWalk.routeName = "Mission Hills Tour";
        firstWalk.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        firstWalk.steps = String.format("%d", 1337);
        firstWalk.distance = String.format("%.2f mi", 13.09);

        RoomWalk secondWalk = new RoomWalk();
        secondWalk.time = System.currentTimeMillis() + 100000;
        secondWalk.routeName = "Mission Hills Tour";
        secondWalk.duration = String.format("%02d:%02d:%02d", 21, 43, 65);
        secondWalk.steps = String.format("%d", 7331);
        secondWalk.distance = String.format("%.2f mi", 31.09);

        RoomWalk thirdWalk = new RoomWalk();
        thirdWalk.time = System.currentTimeMillis() - 100000;
        thirdWalk.routeName = "Mission Hills Tour";
        thirdWalk.duration = String.format("%02d:%02d:%02d", 34, 56, 78);
        thirdWalk.steps = String.format("%d", 888);
        thirdWalk.distance = String.format("%.2f mi", 11.11);

        db.walkDao().insertAll(firstWalk, secondWalk, thirdWalk);

        List<RoomWalk> walks = db.walkDao().findByRouteName("Mission Hills Tour");

        assertEquals("7331", walks.get(0).steps);
        assertEquals("31.09 mi", walks.get(0).distance);
        assertEquals("21:43:65", walks.get(0).duration);
        
        assertEquals("1337", walks.get(1).steps);
        assertEquals("13.09 mi", walks.get(1).distance);
        assertEquals("12:34:56", walks.get(1).duration);

        assertEquals("888", walks.get(2).steps);
        assertEquals("11.11 mi", walks.get(2).distance);
        assertEquals("34:56:78", walks.get(2).duration);
    }

    @Test
    public void testFindWalksGivenNonexistentRouteName() {
        RoomRoute missionHills = new RoomRoute();
        missionHills.name = "Mission Hills Tour";
        missionHills.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        db.routeDao().insertAll(missionHills);

        RoomWalk firstWalk = new RoomWalk();
        firstWalk.time = System.currentTimeMillis();
        firstWalk.routeName = "Mission Hills Tour";
        firstWalk.duration = String.format("%02d:%02d:%02d", 12, 34, 56);
        firstWalk.steps = String.format("%d", 1337);
        firstWalk.distance = String.format("%.2f mi", 13.09);

        db.walkDao().insertAll(firstWalk);

        List<RoomWalk> walks = db.walkDao().findByRouteName("Ho Ho Ho!");
        assertEquals(0, walks.size());
    }

    @Test
    public void testInsertSingleRoute() {
        RoomRoute newEntry = new RoomRoute();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = RoomRoute.RouteType.LOOP;
        newEntry.hilliness = RoomRoute.Hilliness.FLAT;
        newEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        newEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        newEntry.difficulty = RoomRoute.Difficulty.EASY;
        newEntry.notes = "This is a pretty dope route wanna do it again";
        db.routeDao().insertAll(newEntry);

        List<RoomRoute> routes = db.routeDao().retrieveAllRoutes();
        RoomRoute r = routes.get(0);
        assertEquals("Mission Hills Tour", r.name);
        assertEquals("Kufuerstendamm & Friedrichstrasse", r.startingPoint);
        assertEquals(RoomRoute.RouteType.LOOP, r.routeType);
        assertEquals(RoomRoute.Hilliness.FLAT, r.hilliness);
        assertEquals(RoomRoute.SurfaceType.STREETS, r.surfaceType);
        assertEquals(RoomRoute.Evenness.EVEN_SURFACE, r.evenness);
        assertEquals(RoomRoute.Difficulty.EASY, r.difficulty);
        assertEquals("This is a pretty dope route wanna do it again", r.notes);
        assertNull("Favorite is not specified", r.favorite);
    }

    @Test
    public void testAlphabeticalOrderRoutes() {
        int NUM_OF_ENTRIES = 3;
        RoomRoute[] routeArr = new RoomRoute[NUM_OF_ENTRIES];
        for (int i = 0; i < NUM_OF_ENTRIES; i++) {
            routeArr[i] = new RoomRoute();
        }

        RoomRoute newEntry = routeArr[0];
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = RoomRoute.RouteType.LOOP;
        newEntry.hilliness = RoomRoute.Hilliness.FLAT;
        newEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        newEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        newEntry.difficulty = RoomRoute.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";

        routeArr[1].name = "Hike to Mars";
        routeArr[1].startingPoint = "Phobos & Deimos";

        routeArr[2].name = "Zamba!";
        routeArr[2].startingPoint = "Alpha & Omega";

        db.routeDao().insertAll(routeArr[0], routeArr[1], routeArr[2]);

        List<RoomRoute> routes = db.routeDao().retrieveAllRoutes();
        assertEquals("Hike to Mars", routes.get(0).name);
        assertEquals("Mission Hills Tour", routes.get(1).name);
        assertEquals("Zamba!", routes.get(2).name);
    }

    @Test
    public void testInsertDuplicateRoutes() {
        RoomRoute newEntry = new RoomRoute();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = RoomRoute.RouteType.LOOP;
        newEntry.hilliness = RoomRoute.Hilliness.FLAT;
        newEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        newEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        newEntry.difficulty = RoomRoute.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";

        RoomRoute duplicateEntry = new RoomRoute();
        duplicateEntry.name = "Mission Hills Tour";
        db.routeDao().insertAll(newEntry);
        try {
            db.routeDao().insertAll(duplicateEntry);
            Assert.fail("Duplicated primary key, should have thrown a SQLiteConstraintException here!");
        } catch (SQLiteConstraintException e) {}

        List<RoomRoute> routes = db.routeDao().retrieveAllRoutes();
        assertEquals(1, routes.size());
        assertEquals("Mission Hills Tour", routes.get(0).name);
    }

    @Test
    public void testInsertEmptyRouteName() {
        RoomRoute emptyNameEntry = new RoomRoute();
        emptyNameEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        emptyNameEntry.routeType = RoomRoute.RouteType.LOOP;
        emptyNameEntry.hilliness = RoomRoute.Hilliness.FLAT;
        emptyNameEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        emptyNameEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        emptyNameEntry.difficulty = RoomRoute.Difficulty.MODERATE;
        emptyNameEntry.notes = "This is a pretty dope route wanna do it again";

        try {
            db.routeDao().insertAll(emptyNameEntry);
            Assert.fail("Empty primary key, should have thrown a SQLiteConstraintException here!");
        } catch (SQLiteConstraintException e) {}
    }

    @Test
    public void testFindRouteGivenName() {
        RoomRoute newEntry = new RoomRoute();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = RoomRoute.RouteType.LOOP;
        newEntry.hilliness = RoomRoute.Hilliness.FLAT;
        newEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        newEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        newEntry.difficulty = RoomRoute.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";
        newEntry.favorite = RoomRoute.Favorite.FAVORITE;

        RoomRoute secondEntry = new RoomRoute();
        secondEntry.name = "Hike Around The Moon";
        secondEntry.startingPoint = "Kennedy Space Center";
        secondEntry.routeType = RoomRoute.RouteType.OUT_AND_BACK;
        secondEntry.hilliness = RoomRoute.Hilliness.FLAT;
        secondEntry.surfaceType = RoomRoute.SurfaceType.TRAIL;
        secondEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        secondEntry.difficulty = RoomRoute.Difficulty.DIFFICULT;
        secondEntry.notes = "Ground Control to Major Tom";
        secondEntry.favorite = RoomRoute.Favorite.NOT_FAVORITE;

        db.routeDao().insertAll(newEntry, secondEntry);

        RoomRoute firstQuery = db.routeDao().findName("Mission Hills Tour");
        assertEquals("Mission Hills Tour", firstQuery.name);
        assertEquals("Kufuerstendamm & Friedrichstrasse", firstQuery.startingPoint);
        assertEquals(RoomRoute.RouteType.LOOP, firstQuery.routeType);
        assertEquals(RoomRoute.Hilliness.FLAT, firstQuery.hilliness);
        assertEquals(RoomRoute.SurfaceType.STREETS, firstQuery.surfaceType);
        assertEquals(RoomRoute.Evenness.EVEN_SURFACE, firstQuery.evenness);
        assertEquals(RoomRoute.Difficulty.MODERATE, firstQuery.difficulty);
        assertEquals("This is a pretty dope route wanna do it again", firstQuery.notes);
        assertEquals(RoomRoute.Favorite.FAVORITE, firstQuery.favorite);

        RoomRoute secondQuery = db.routeDao().findName("Hike Around The Moon");
        assertEquals("Hike Around The Moon", secondQuery.name);
        assertEquals("Kennedy Space Center", secondQuery.startingPoint);
        assertEquals(RoomRoute.RouteType.OUT_AND_BACK, secondQuery.routeType);
        assertEquals(RoomRoute.Hilliness.FLAT, secondQuery.hilliness);
        assertEquals(RoomRoute.SurfaceType.TRAIL, secondQuery.surfaceType);
        assertEquals(RoomRoute.Evenness.EVEN_SURFACE, secondQuery.evenness);
        assertEquals(RoomRoute.Difficulty.DIFFICULT, secondQuery.difficulty);
        assertEquals("Ground Control to Major Tom", secondQuery.notes);
        assertEquals(RoomRoute.Favorite.NOT_FAVORITE, secondQuery.favorite);
    }

    @Test
    public void testFindRouteGivenNonexistentName() {
        RoomRoute newEntry = new RoomRoute();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "A";

        RoomRoute secondEntry = new RoomRoute();
        secondEntry.name = "Hike Around The Moon";
        secondEntry.startingPoint = "B";

        db.routeDao().insertAll(newEntry, secondEntry);

        RoomRoute nonexistentQuery = db.routeDao().findName("Andromeda Galaxy");
        assertNull("Queried object doesn't actually exist", nonexistentQuery);
    }

    @Test
    public void testFindRouteGivenNameCaseInsensitive() {
        RoomRoute newEntry = new RoomRoute();
        newEntry.name = "Mission Hills Tour";
        newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
        newEntry.routeType = RoomRoute.RouteType.LOOP;
        newEntry.hilliness = RoomRoute.Hilliness.FLAT;
        newEntry.surfaceType = RoomRoute.SurfaceType.STREETS;
        newEntry.evenness = RoomRoute.Evenness.EVEN_SURFACE;
        newEntry.difficulty = RoomRoute.Difficulty.MODERATE;
        newEntry.notes = "This is a pretty dope route wanna do it again";

        db.routeDao().insertAll(newEntry);

        RoomRoute inverseCaseQuery = db.routeDao().findName("mISSION hILLS tOUR");
        assertEquals("Mission Hills Tour", inverseCaseQuery.name);
        assertEquals("Kufuerstendamm & Friedrichstrasse", inverseCaseQuery.startingPoint);
        assertEquals(RoomRoute.RouteType.LOOP, inverseCaseQuery.routeType);
        assertEquals(RoomRoute.Hilliness.FLAT, inverseCaseQuery.hilliness);
        assertEquals(RoomRoute.SurfaceType.STREETS, inverseCaseQuery.surfaceType);
        assertEquals(RoomRoute.Evenness.EVEN_SURFACE, inverseCaseQuery.evenness);
        assertEquals(RoomRoute.Difficulty.MODERATE, inverseCaseQuery.difficulty);
        assertEquals("This is a pretty dope route wanna do it again", inverseCaseQuery.notes);

    }
}
