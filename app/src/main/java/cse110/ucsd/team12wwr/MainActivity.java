package cse110.ucsd.team12wwr;
//Comments with ACF are things I removed from hopefully the older implementation
//that, hopefully don't blow up the whole program but lets find out
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cse110.ucsd.team12wwr.fitness.FitnessService;
import cse110.ucsd.team12wwr.fitness.FitnessServiceFactory;
import cse110.ucsd.team12wwr.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    /* constants */
    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final int MILE_FACTOR = 63360;

    SharedPreferences spf;

    /* steps */
    /*SensorManager sensorManager;
    boolean running = false; ACF */
    TextView textStep;
    long numSteps;

    /*GoogleFit Nonsense     */
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";
    private FitnessService fitnessService;
    private String fitnessServiceKey = "GOOGLE_FIT";

    /*Async Nonsense */
    AsyncStepUpdate asyncStepsUpdater;

    /* distance */
    TextView textDist;
    int totalHeight;
    double strideLength;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button launchIntentionalWalkActivity = (Button) findViewById(R.id.btn_start_walk);

        launchIntentionalWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);

        //sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); ACF
        //ACF Added for GoogleFit reasons
        ///////////////////////////////////////
        //This happens in Main Activity, the next 2 lines are in stepCountActivity
        /*FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });*/
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        
        textDist = findViewById(R.id.num_miles);
        textStep = findViewById(R.id.num_steps);

        Button btnDebugIncSteps = findViewById(R.id.btn_debug_increment_steps);
        setSupportActionBar(toolbar);
        closeOptionsMenu();

        // Collect the height from the previous page
        spf = getSharedPreferences("height", MODE_PRIVATE);
        int feet = spf.getInt("feet", 0);
        int inches = spf.getInt("inches", 0);

        System.out.println("feet: " + feet + " inches: "  + inches);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;

        numSteps = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        textDist.setText(df.format((strideLength / MILE_FACTOR) * numSteps));

        btnDebugIncSteps.setOnClickListener((view) -> {
            numSteps += 100;
            textDist.setText(df.format((strideLength / MILE_FACTOR) * numSteps));
            textStep.setText(""+numSteps);
        });

        //ACF begins fitnessService and gets the initial pedometer data
        /*NOTE: There might eventually be a reason where this shouldn't be near the bottom of onCreate
        If onCreate ever gets massively unwieldy, */
        fitnessService.setup();
        fitnessService.updateStepCount();
        asyncStepsUpdater = new AsyncStepUpdate();
        asyncStepsUpdater.execute();
    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }ACF*/

        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
        databaseWriteExecutor.execute(() -> {
            WalkDatabase walkDb = WalkDatabase.getInstance(this);
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
    protected void onPause() {
        super.onPause();
        //running = false; ACF
        // if you unregister the hardware will stop detecting steps
        // sensorManager.unregisterListener(this);
    }
    /*
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            textStep.setText(String.valueOf(event.values[0]));
        }
    }ACF

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }ACF*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ACF - method to update textStep and numSteps
    public void setStepCount(long stepCount) {
        textStep.setText(String.valueOf(stepCount));
        numSteps = stepCount;
    }
    //ACF - Added as part of GoogleFit functionality 2/10
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }
    //ACF - Added to use Async to update info from pedometer
    private class AsyncStepUpdate extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params){
            for (int i=-1; i<0; i--){
                try {
                    Thread.sleep(5000);
                    System.out.println("CURRENT VALUE OF I: " + i);
                    publishProgress(resp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {

            //textResult.setText(result);
            System.out.println("onPostExecute successful");
        }
        @Override
        protected void onPreExecute() {
            //textResult.setText("Counter starting");
            System.out.println("onPreExecute successful");
        }
        @Override
        protected void onProgressUpdate(String...text){
            //textResult.setText(text[0]);
            fitnessService.updateStepCount();
            //System.out.println("onProgressUpdate successful");
        }



    }



}