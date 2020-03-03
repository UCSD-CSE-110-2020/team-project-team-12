package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;
import cse110.ucsd.team12wwr.database.WalkDao;
import cse110.ucsd.team12wwr.fitness.GoogleFitUtility;

public class MainActivity extends AppCompatActivity {

    /* constants */
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
    private static final String TAG = "MainActivity";
    //private FitnessService fitnessService;
    private final String fitnessServiceKey = "GOOGLE_FIT";

    /* distance */
    TextView textDist;
    int totalHeight;
    double strideLength;

    /* GoogleFit Refactor */
    int RC_SIGN_IN = 4;
    GoogleFitUtility gFitUtil;
    public boolean googleSubscribedStatus = false;
    public boolean gFitUtilLifecycleFlag;

    /* Team Related Variables */
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity.onCreate", "onCreate() called");

        /* START GOOGLE LOGIN */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(FIRST_LAUNCH_KEY, false);

        // Launches height activity only on first start
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(FIRST_LAUNCH_KEY, Boolean.TRUE);
            edit.commit();
            launchHeightActivity();
        }

        Button launchIntentionalWalkActivity = (Button) findViewById(R.id.btn_start_walk);
        launchIntentionalWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });

        Button launchRoutesScreen = (Button) findViewById(R.id.routes_list_button);
        launchRoutesScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRoutesScreenActivity();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);




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

        /* PEDOMETER START */
        gFitUtil = new GoogleFitUtility(this);
        gFitUtil.init();
        final Handler checkSubscription = new Handler();
        checkSubscription.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gFitUtil.getSubscribed()) {
                    Log.i("checkSubscription", "Not yet subscribed, trying again in 2 seconds");
                    checkSubscription.postDelayed(this, 2000);
                }
                else{
                    Log.i("checkSubscription", "Ending handler.run");
                    googleSubscribedStatus = true;
                }
            }
        }, 1500);


    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = "account not retrieved";
        try{
            userEmail = account.getEmail();
        }
        catch(NullPointerException e){
            Log.i("ACCOUNT NOT SIGNED IN PRIOR", " Null pointer caught");
        }
        Log.i("GMAIL: ", userEmail);

    }


    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    public void launchRoutesScreenActivity() {
        Intent intent = new Intent(this, RoutesScreen.class);
        startActivity(intent);
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity.onPause", "onPause() has been called");
        gFitUtilLifecycleFlag = false;
    }

    public void launchHeightActivity() {
        Intent intent = new Intent( this, StartPage.class );
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity.onResume", "onResume() has been called");


        gFitUtilLifecycleFlag = true;

        final Handler stepsUpdaterHandler = new Handler();
        stepsUpdaterHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gFitUtilLifecycleFlag) {
                    Log.i("stepsUpdaterHandler", "Updating step value");
                    gFitUtil.updateStepCount();
                    setStepCount(gFitUtil.getStepValue());
                    stepsUpdaterHandler.postDelayed(this, 4000);
                }
                else{
                    Log.i("stepsUpdaterHandler", "THREAD DISABLED");
                }
            }
        }, 4000);


        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
        databaseWriteExecutor.execute(() -> {
            WWRDatabase walkDb = WWRDatabase.getInstance(this);
            WalkDao dao = walkDb.walkDao();

            Walk newestWalk = dao.findNewestEntry();
            if (newestWalk != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView stepsWalkText = findViewById(R.id.text_steps_value);
                        TextView distWalkText = findViewById(R.id.text_distance_value);
                        TextView timeWalkText = findViewById(R.id.text_time_value);

                        stepsWalkText.setText(newestWalk.steps);
                        distWalkText.setText(newestWalk.distance);
                        timeWalkText.setText(newestWalk.duration);
                    }
                });
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
        else if (id == R.id.team_screen){
            Intent intent = new Intent(this, TeamScreenActivity.class);
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
    /*
    //For GoogleFit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                //fitnessService.updateStepCount();
                fitnessService.startRecording();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }*/

    @Override
    protected void onDestroy() {
        Log.i("MainActivity.onDestroy", "onDestroy() has been called");
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account.getEmail() != null) {
                Log.i("MainActivity.handleSignInResult() yields: ", account.getEmail());
                userEmail = account.getEmail();
            }
            else
                Log.i("MainActivity.handleSignInResult() yields: ", "NULL");
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

}
