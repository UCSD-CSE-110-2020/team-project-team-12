package cse110.ucsd.team12wwr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cse110.ucsd.team12wwr.clock.DeviceClock;
import cse110.ucsd.team12wwr.clock.IClock;
import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;
import cse110.ucsd.team12wwr.database.Walk;
import cse110.ucsd.team12wwr.database.WalkDao;

public class IntentionalWalkActivity extends AppCompatActivity {
    // TODO code repetition
    private final int HEIGHT_FACTOR = 12;
    private final double STRIDE_CONVERSION = 0.413;
    private final int MILE_FACTOR = 63360;

    private TextView stopwatchText, stepsText, distanceText;
    private AsyncTaskRunner runner;
    private int timeWhenPaused, timeElapsed;
    private double strideLength;

    private int temporaryNumSteps;

    private IClock clock;

    private String result;
    private static final String TAG = "IntentionalWalkActivity";
    private static int LAUNCH_SECOND_ACTIVITY = 1;
    //private long stepsFromService = -1;

    /*
    private PedometerService pedoService;
    private boolean isBound;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            PedometerService.LocalService localService = (PedometerService.LocalService)service;
            pedoService = localService.getService();
            isBound = true;
            stepsFromService = pedoService.getCurrentSteps();
            Log.i("onServiceConnected", "Current Step Count is " + stepsFromService);

            //pedoService.gimmethemsteppies(fitnessService);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {isBound = false;}
    };

    public void bindTheThing(){
        Intent intent = new Intent(this, PedometerService.class);
        Log.i("Intentional Walk Activity", "COMMENCE THE BINDING");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentional_walk);
        //bindTheThing();
        clock = new DeviceClock();

        result = null;
        // TODO this is code repetition, should just declare getStrideLength() somewhere
        SharedPreferences spf = getSharedPreferences("HEIGHT", MODE_PRIVATE);
        int feet = spf.getInt("FEET", 0);
        int inches = spf.getInt("INCHES", 0);
        int totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;

        final Button pauseButton = findViewById(R.id.btn_pause_walk);
        final Button continueButton = findViewById(R.id.btn_continue_walk);
        final Button stopButton = findViewById(R.id.btn_stop_walk);

        stopwatchText = findViewById(R.id.text_time_value);
        stepsText = findViewById(R.id.text_steps_value);
        distanceText = findViewById(R.id.text_distance_value);

        /* starts the stopwatch */
        if (runner != null) {
            runner.cancel(true);
        }

        runner = new AsyncTaskRunner();
        runner.execute();

        continueButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeWhenPaused += timeElapsed;
                if (runner != null) {
                    runner.cancel(true);
                }

                continueButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runner != null) {
                    runner.cancel(true);
                }

                runner = new AsyncTaskRunner();
                runner.execute();

                continueButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener((view) -> {

//            timeWhenPaused += timeElapsed;
//            if (runner != null) {
//                runner.cancel(true);
//            }


            launchRouteInfoPage();

            // TODO: Help me
//            ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
//            databaseWriteExecutor.execute(() -> {
//                WWRDatabase walkDb = WWRDatabase.getInstance(this);
//                WalkDao dao = walkDb.walkDao();
//                RouteDao rDao = walkDb.routeDao();
//
//                //Route route = rDao.findName(result);
//                Walk newEntry = new Walk();
//                newEntry.time = System.currentTimeMillis();
//                newEntry.duration = stopwatchText.getText().toString();
//                newEntry.steps = stepsText.getText().toString();
//                newEntry.distance = distanceText.getText().toString();
//                newEntry.routeName = result;//route.name;
//
//                dao.insertAll(newEntry);
//            });
//
//            finish();
        });
    }

    private void launchRouteInfoPage() {
        Log.d(TAG, "launchRouteInfoPage: launching the route information page");
        Intent intent = new Intent(this, RouteInfoActivity.class);
        intent.putExtra("duration", stopwatchText.getText().toString());
        intent.putExtra("distance", distanceText.getText().toString());
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    protected void setClock(IClock clock) {
        this.clock = clock;
    }

    protected int getNumSteps() {
        temporaryNumSteps += 10;
        return temporaryNumSteps;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private int startTime;

        @Override
        protected void onPreExecute() {
            startTime = clock.getCurrentClock();
        }

        @Override
        protected String doInBackground(String... params) {
            while (true){
                timeElapsed = clock.getCurrentClock() - startTime;
                int updateTime = timeWhenPaused + timeElapsed;

                int hours = (updateTime / 3600)  % 60;
                int minutes = (updateTime / 60)  % 60;
                int seconds = updateTime % 60;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                //TODO replace with Thuy's functionality
                int numSteps = getNumSteps();
                String stepsString = String.format("%d", numSteps);

                double distance = (strideLength / MILE_FACTOR) * numSteps;
                String distanceString = String.format("%.2f mi", distance);

                publishProgress(timeString, stepsString, distanceString);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    return e.toString();
                }

                if (isCancelled()) {
                    break;
                }
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... text) {
            stopwatchText.setText(text[0]);
            stepsText.setText(text[1]);
            distanceText.setText(text[2]);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                result = data.getExtras().getString("routeTitle");
                System.out.println("RESULT IS: " + result);
                ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
                databaseWriteExecutor.execute(() -> {
                    WWRDatabase walkDb = WWRDatabase.getInstance(this);
                    WalkDao dao = walkDb.walkDao();
                    RouteDao rDao = walkDb.routeDao();

                    Walk newEntry = new Walk();
                    newEntry.time = System.currentTimeMillis();
                    newEntry.duration = stopwatchText.getText().toString();
                    newEntry.steps = stepsText.getText().toString();
                    newEntry.distance = distanceText.getText().toString();
                    newEntry.routeName = result;

                    dao.insertAll(newEntry);
                });

                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
}
