package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class USFourTest {
    private Intent intent, mainIntent;
    private ActivityTestRule<TeamIndividRoutes> teamIndividRoutesActivityTestRule;

    @Before
    public void setUp() {
        MainActivity.unitTestFlag = true;
        intent = new Intent(ApplicationProvider.getApplicationContext(), StartPage.class);
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), TeamIndividRoutes.class);
        intent.putExtras(mainIntent);
        teamIndividRoutesActivityTestRule = new ActivityTestRule<>(TeamIndividRoutes.class);
    }

    @Test
    public void testSplitRoutes() {
        teamIndividRoutesActivityTestRule.launchActivity(mainIntent);
        TabLayout tabLayout = teamIndividRoutesActivityTestRule.getActivity().findViewById(R.id.tabs);
        assertEquals(2, tabLayout.getTabCount());
        assertEquals("Your Routes", tabLayout.getTabAt(0).getText().toString());
        assertEquals("Team Routes", tabLayout.getTabAt(1).getText().toString());
    }
}
