package cse110.ucsd.team12wwr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cse110.ucsd.team12wwr.firebase.FirebaseWalkDao;
import cse110.ucsd.team12wwr.firebase.Walk;
import cse110.ucsd.team12wwr.fitness.FitnessService;

public class MainActivity extends AppCompatActivity {

    /* constants */
    private static final String TAG = "MainActivity";

    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final int MILE_FACTOR = 63360;
    DecimalFormat DF = new DecimalFormat("#.##");
    final String FIRST_LAUNCH_KEY = "HAVE_HEIGHT";
    final String HEIGHT_SPF_NAME = "HEIGHT";
    final String FEET_KEY = "FEET";
    final String INCHES_KEY = "INCHES";
    final String STEP_SPF_NAME = "TOTAL_DIST_STEP";
    final String TOTAL_STEPS_KEY = "totalSteps";

    /* height */
    SharedPreferences spf, spf2, prefs;

    /* steps */
    TextView textStep;
    long numSteps = 0;

    /* GoogleFit */
    private FitnessService fitnessService;
    private final String fitnessServiceKey = "GOOGLE_FIT";


    /*SERVICE*/
    private PedometerService pedService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            Log.i("MainActivity.onServiceConnected", "PedometerService Connection Initializing");
            PedometerService.LocalService localService = (PedometerService.LocalService)service;
            try {
                pedService = localService.getService();
            }
            catch(Exception e){
                Log.i("Not real", "NOT REAL THIS DOESN'T HAPPEN");
            }
            isBound = true;
            Log.i("MainActivity.onServiceConnected", "PedometerService Connected");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {isBound = false;}
    };

    /* distance */
    TextView textDist;
    int totalHeight;
    double strideLength;

    /* TESTING */
    public boolean testingFlag = false;

    public boolean startServCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTestingFlag(true);

        launchTeamRouteActivity();

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(FIRST_LAUNCH_KEY, false);

        // Launches height activity only on first start
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(FIRST_LAUNCH_KEY, Boolean.TRUE);
            edit.commit();
            launchHeightActivity();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

//        // Create and adapt the FitnessService
//        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
//            @Override
//            public FitnessService create(MainActivity mainActivity) {
//                return new GoogleFitAdapter(mainActivity);
//            }
//        });
//        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        textDist = findViewById(R.id.num_miles);
        textStep = findViewById(R.id.num_steps);

        setSupportActionBar(toolbar);
        closeOptionsMenu();

        // Collect the height from the height page
        spf = getSharedPreferences(HEIGHT_SPF_NAME, MODE_PRIVATE);
        int feet = spf.getInt(FEET_KEY, 0);
        int inches = spf.getInt(INCHES_KEY, 0);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;

//        /* PEDOMETER START */
//        Log.i("MainActivity.onCreate", "calling fitnessService.setup()");
//        if(testingFlag) {
//            fitnessService.setup();
//            startStepUpdaterMethod();
//        }

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_routes:
                        launchRoutesScreenActivity();

                        break;
                    case R.id.navigation_walk:
                        launchActivity();

                        break;
                    case R.id.navigation_teams:
                        launchTeamScreenActivity();

                        break;
                }
                return false;
            }
        });

    }

    public void setFitnessService(FitnessService newService){
        fitnessService = newService;
    }
    public void setTestingFlag(boolean flag){
        testingFlag = flag;
    }

    public void startStepUpdaterMethod(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainActivity.startStepUpdaterMethod", "start runner called");
                if(!isBound || pedService==null) {
                    Log.i("MainActivity.startStepUpdaterMethod", "Still waiting for successful bind");
                    handler.postDelayed(this, 2000);
                }
                else{
                    Log.i("MainActivity.startStepUpdaterMethod", "Successful bind achieved");
                    pedService.beginStepTracking(fitnessService);
                    startServCalled = true;
                }
            }
        }, 1500);
    }
    public void continueStepUpdaterMethod(){
        final Handler bindCheckHandler = new Handler();
        bindCheckHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainActivity.continueStepUpdaterMethod", "continue Runner Called");
                if(!isBound) {
                    Log.i("MainActivity.continueStepUpdaterMethod", "Still waiting for successful bind");
                    bindCheckHandler.postDelayed(this, 2000);
                }
                if(!startServCalled){
                    Log.i("MainActivity.continueStepUpdaterMethod", "beginStepTracking Uncalled");
                    bindCheckHandler.postDelayed(this, 2000);
                }
                else{
                    Log.i("MainActivity.continueStepUpdaterMethod", "Successful bind achieved");
                    final Handler updateStepsHandler = new Handler();
                    updateStepsHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setStepCount(pedService.getCurrentSteps());
                            updateStepsHandler.postDelayed(this, 3000);
                        }
                    }, 3000);
                }
            }
        }, 1500);
    }
    public void bindPedService(){
        if(!testingFlag)
            return;
        Intent intent = new Intent(this, PedometerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    public void rebindPedService(){
        Intent intent = new Intent(this, PedometerService.class);
        bindService(intent, serviceConnection, 0);
        isBound = true;
    }
    public void unbindPedometerService() {
        unbindService(serviceConnection);
    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    public void launchRoutesScreenActivity() {
        Intent intent = new Intent(this, RoutesScreen.class);
        startActivity(intent);
    }

    public void launchTeamScreenActivity() {
        Intent intent = new Intent(this, TeamScreen.class);
        startActivity(intent);
    }

    public void launchTeamRouteActivity() {
        Intent intent = new Intent(this, TeamIndividRoutes.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity.onPause", "onPause() has been called");
//        stopService(new Intent(this, PedometerService.class));
//        isBound = false;
//        unbindPedometerService();
    }

    public void launchHeightActivity() {
        Intent intent = new Intent( this, StartPage.class );
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity.onResume", "onResume() has been called");
//        rebindPedService();
//        continueStepUpdaterMethod();

        FirebaseWalkDao dao = new FirebaseWalkDao();
        dao.findNewestEntries().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Walk newestWalk = null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (newestWalk == null) {
                        newestWalk = document.toObject(Walk.class);
                    }
                }

                if (newestWalk != null) {
                    TextView stepsWalkText = findViewById(R.id.text_steps_value);
                    TextView distWalkText = findViewById(R.id.text_distance_value);
                    TextView timeWalkText = findViewById(R.id.text_time_value);

                    stepsWalkText.setText(newestWalk.steps);
                    distWalkText.setText(newestWalk.distance);
                    timeWalkText.setText(newestWalk.duration);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.debug_walk) {
            Intent intent = new Intent(this, DebugWalkActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    //Updates numSteps with pedometer data, sets textDist and textStep
    public void setStepCount(long stepCount) {
        numSteps = stepCount;
        DecimalFormat df = new DecimalFormat("#.##");
        textDist.setText(df.format((strideLength / MILE_FACTOR) * numSteps));
        textStep.setText(""+numSteps);
    }
    //For GoogleFit
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////       If authentication was required during google fit setup, this will be called after the user authenticates
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == fitnessService.getRequestCode()) {
//                //fitnessService.updateStepCount();
//                fitnessService.startRecording();
//            }
//        } else {
//            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
//        }
//    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity.onDestroy", "onDestroy() has been called");
//        if(isBound){
//            unbindService(serviceConnection);
//            isBound = false;
//        }
        super.onDestroy();
    }

}
