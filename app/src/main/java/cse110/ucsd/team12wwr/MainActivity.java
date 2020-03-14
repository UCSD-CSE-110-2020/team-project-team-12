package cse110.ucsd.team12wwr;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.os.IBinder;
import android.util.Log;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.Random;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Invitation;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.firebase.WalkDao;
import cse110.ucsd.team12wwr.fitness.GoogleFitUtility;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cse110.ucsd.team12wwr.firebase.Walk;

public class MainActivity extends AppCompatActivity {

    /* constants */
    private static final String TAG = "MainActivity";

    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final double MILE_FACTOR = 63360;
    final String FIRST_LAUNCH_KEY = "HAVE_HEIGHT";
    final String HEIGHT_SPF_NAME = "HEIGHT";
    final String FEET_KEY = "FEET";
    final String INCHES_KEY = "INCHES";

    /* height */
    SharedPreferences spf, spf2, prefs;

    /* steps */
    TextView textStep;
    long numSteps = 0;

    /* Testing */
    public static boolean unitTestFlag = false;

    /* Team Related Variables */
    public static String userEmail;
    String teamName;
    cse110.ucsd.team12wwr.firebase.User thisUser;
    String firstName;
    String lastName;

    /* distance */
    TextView textDist;
    int totalHeight;
    double strideLength;

    /* GoogleFit Refactor */
    int RC_SIGN_IN = 4;
    GoogleFitUtility gFitUtil;
    public boolean googleSubscribedStatus = false;
    public boolean gFitUtilLifecycleFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity.onCreate", "onCreate() called");

        spf = getSharedPreferences(HEIGHT_SPF_NAME, MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        /* START GOOGLE LOGIN */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        /* COMMENTED OUT FOR NOW 5:45PM 3/4/2020
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
        }); */
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



        //onDestroy();

        /* PEDOMETER START */
        gFitUtil = new GoogleFitUtility(this);
        final Handler checkSubscription = new Handler();
        checkSubscription.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gFitUtil.getSubscribed()) {
                    Log.i("checkSubscription", " Not yet subscribed, checking again in 5 seconds");
                    checkSubscription.postDelayed(this, 5000);
                }
                else{
                    Log.i("checkSubscription", " Ending handler.run");
                    googleSubscribedStatus = true;
                }
            }
        }, 5000);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity.onStart", "onStart() has been called");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = "account not retrieved";
        userEmail = "nicholasalimit@gmail.com";
        try {
            userEmail = account.getEmail();
            userEmail = "nicholasalimit@gmail.com"; //TODO this is hardcoded
            Log.d(TAG, "onStart: Email put into spf is: " + userEmail);

            SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("EMAIL_ID", userEmail);
            editor.apply();
            //getTeamIDFromDB(userEmail);
        }
        catch(NullPointerException e){
            Log.i("ACCOUNT NOT SIGNED IN PRIOR", " No prior sign in");
        }
        Log.i("GMAIL: ", userEmail);

        /* FOR DEBUG ONLY */





        BottomNavigationView navigation = findViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        Button launchIntentionalWalkActivity = (Button) findViewById(R.id.start_walk_btn);
        launchIntentionalWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_routes:
                        launchTeamRouteActivity();

                        break;
                    case R.id.navigation_walk:
                        launchSuggestedWalkActivity();

                        break;
                    case R.id.navigation_teams:
                        launchTeamScreenActivity();

                        break;
                }
                return false;
            }
        });
    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

//    public void launchRoutesScreenActivity() {
//        Intent intent = new Intent(this, RoutesScreen.class);
//        startActivity(intent);
//    }

    public void launchTeamScreenActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userEmail = "nicholasalimit@gmail.com"; //TODO this is hardcoded
        editor.putString("EMAIL_ID", userEmail);
        editor.apply();

        Intent intent = new Intent(this, TeamScreen.class);
        startActivity(intent);
    }

    public void launchSuggestedWalkActivity() {
        Intent intent = new Intent(this, ScheduledWalkActivity.class);
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
                if (gFitUtilLifecycleFlag && gFitUtil.getSubscribed()) {
                    //Log.i("stepsUpdaterHandler", "Updating step value");
                    gFitUtil.updateStepCount();
                    setStepCount(gFitUtil.getStepValue());
                    stepsUpdaterHandler.postDelayed(this, 4000);
                }
                else if (!gFitUtil.getSubscribed()){
                    Log.i("stepsUpdaterHandler", "NOT YET SUBSCRIBED");
                }
                else{
                    Log.i("stepsUpdaterHandler", "THREAD DISABLED");
                }
            }
        }, 4000);


        WalkDao dao = DaoFactory.getWalkDao();
        dao.findNewestEntries(task -> {
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


        Log.i("MainActivity.onResume", "onResume() has been COMPLETED");
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
            Intent intent = new Intent(this, TeamScreen.class);
            if(userEmail!=null)
                intent.putExtra("user Email", userEmail);
            else
                intent.putExtra("user Email", "FAILED TO RETRIEVE USER INFO");
            startActivity(intent);
        }
        else if (id == R.id.invites){
            Intent intent = new Intent(this, PendingInviteActivity.class);
            if(userEmail!=null)
                intent.putExtra("user Email", userEmail);
            else
                intent.putExtra("user Email", "FAILED TO RETRIEVE USER INFO");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    //Updates numSteps with pedometer data, sets textDist and textStep
    public void setStepCount(long stepCount) {
        // Collect the height from the height page

        int feet = spf.getInt(FEET_KEY, 0);
        int inches = spf.getInt(INCHES_KEY, 0);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;




        numSteps = stepCount;
        DecimalFormat df = new DecimalFormat("#.##");
        textDist.setText(df.format((strideLength / MILE_FACTOR) * numSteps));
        textStep.setText(""+numSteps);
    }


    @Override
    protected void onDestroy() {
        Log.i("MainActivity.onDestroy", "onDestroy() has been called");
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult ", requestCode + "");

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.i("onActivityResult ", "confirm RC");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            gFitUtil.init();
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.i("handleSignInResult ", "begin");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account.getEmail() != null) {
                Log.i("MainActivity.handleSignInResult() yields: ", account.getEmail());
                userEmail = account.getEmail();
                firstName = account.getGivenName();
                lastName = account.getFamilyName();
                getTeamIDFromDB(userEmail);
                getHeightInfo();
            }
            else {

                Log.i("MainActivity.handleSignInResult() yields: ", "NULL");
                // Signed in successfully, show authenticated UI.
                //
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void getHeightInfo(){
        boolean previouslyStarted = prefs.getBoolean(FIRST_LAUNCH_KEY, false);

        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(FIRST_LAUNCH_KEY, Boolean.TRUE);
            edit.commit();
            launchHeightActivity();
        }
    }

    public String generateTeamId(String userID){
        long r1 = userID.hashCode();
        Random rand = new Random(r1);
        int r2 = rand.nextInt();
        return Integer.toString(r2);
    }

    public void getTeamIDFromDB(String userName){
        Log.i("getTeamIDFromDB", "  starting");
        UserDao dao = DaoFactory.getUserDao();
        dao.findUserByID(userName, task -> {
            if (task.isSuccessful()) {
                User u1 = null;
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        u1 = document.toObject(User.class);
                        //Log.i("getTeamIDFromDB ", u1.toString());
                    }
                    if (u1==null){
                        Log.i("getTeamIDFromDB ", "u1 is null");
                        u1 = new User();
                        u1.teamID = generateTeamId(userName);
                        u1.userID = userName;
                        u1.firstName = firstName;
                        u1.lastName = lastName;
                        u1.userIcon = firstName.charAt(0) + "" + lastName.charAt(0);
                        dao.insertAll(u1);
                        //dao.updateTeamID(userName, generateTeamId(userName));
                    }
                    else{
                        Log.i("getTeamIDFromDB ", ":A team was found");
                    }
                    teamName = u1.teamID;
                    thisUser = u1;
                    Log.i("USER/TEAM: ", thisUser.userID + "/" + teamName);
                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.i("TEAM IS: ", "NONSENSE DETECTED");
                }
            }
            else{
                Log.i("getTeamIDFromDB", " TASKUNSUCCESSFUL");
            }
        });
    }
}
