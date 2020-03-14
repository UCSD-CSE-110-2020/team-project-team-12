package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cse110.ucsd.team12wwr.firebase.Route;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

@RunWith(AndroidJUnit4.class)
public class FavoriteTests {
    private Intent routeInfoPageIntent, routeDetailsPageIntent;
    private ActivityTestRule<RouteInfoActivity> routeInfoPageTestRule;
    private ActivityTestRule<RouteDetailsPage> routeDetailsPageActivityTestRule;
    private Route ownRoute, favoriteRoute;
    private RouteInfoActivity activity;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        routeInfoPageIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteInfoActivity.class);
        routeInfoPageTestRule = new ActivityTestRule<>(RouteInfoActivity.class);

        routeInfoPageTestRule.launchActivity(routeInfoPageIntent);
        activity= routeInfoPageTestRule.getActivity();
    }

    @Test
    public void testFavoriteToggleOn() {
        ActivityScenario<RouteInfoActivity> scenario = ActivityScenario.launch(routeInfoPageIntent);

        scenario.onActivity(activity1 -> {
            routeInfoPageTestRule.launchActivity(routeInfoPageIntent);
            boolean isFavorite = routeInfoPageTestRule.getActivity().isFavorite;
            routeInfoPageTestRule.getActivity().findViewById(R.id.favoriteCheckBtn).performClick();
            assertEquals(routeInfoPageTestRule.getActivity().isFavorite, !isFavorite);
        });

    }


    @Test
    public void testFavoriteToggleOff() {
        ActivityScenario<RouteInfoActivity> scenario = ActivityScenario.launch(routeInfoPageIntent);

        scenario.onActivity(activity1 -> {
            routeInfoPageTestRule.launchActivity(routeInfoPageIntent);
            boolean isFavorite = routeInfoPageTestRule.getActivity().isFavorite;
            assertFalse(routeInfoPageTestRule.getActivity().isFavorite);
        });

    }


}
