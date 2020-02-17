package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import cse110.ucsd.team12wwr.database.Route;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;


public class RouteDetailTests {

    private Intent routeIntent;
    private ActivityTestRule<RouteDetailsPage> routeInfoActivityActivityTestRule;

    @Before
    public void setUp(){
        routeIntent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsPage.class);
        routeInfoActivityActivityTestRule = new ActivityTestRule<>(RouteDetailsPage.class);
    }


}
