package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    /* constants */
    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final int MILE_FACTOR = 63360;
    DecimalFormat DF = new DecimalFormat("#.##");
    final String FIRST_LAUNCH = "HAVE_HEIGHT";
    final String HEIGHT = "height";
    final String FEET = "FEET";
    final String INCHES = "INCHES";
    final String STEP_SPF = "TOTAL_DIST_STEP";
    final String TOTAL_STEPS = "totalSteps";

    /* height */
    SharedPreferences spf, spf2, prefs;

    /* steps */
    SensorManager sensorManager;
    boolean running = false;
    TextView textStep;
    int numSteps;

    /* distance */
    TextView textDist;
    int totalHeight;
    double strideLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(FIRST_LAUNCH, false);

        // Launches height activity only on first start
        if(!previouslyStarted) {
//            System.out.println("Never started!");
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(FIRST_LAUNCH, Boolean.TRUE);
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
        Toolbar toolbar = findViewById(R.id.toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        textDist = findViewById(R.id.num_miles);
        textStep = findViewById(R.id.num_steps);

        Button btnDebugIncSteps = findViewById(R.id.btn_debug_increment_steps);
        setSupportActionBar(toolbar);
        closeOptionsMenu();

        // Collect the height from the height page
        spf = getSharedPreferences(HEIGHT, MODE_PRIVATE);
        int feet = spf.getInt(FEET, 0);
        int inches = spf.getInt(INCHES, 0);

        System.out.println("feet: " + feet + " inches: "  + inches);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;

        spf2 = getSharedPreferences(STEP_SPF, MODE_PRIVATE);
        SharedPreferences.Editor editor = spf2.edit();
        if ( spf2.getInt(TOTAL_STEPS, 0) == 0 ) {
            editor.putInt(TOTAL_STEPS, 0);
            editor.apply();
        }

        numSteps = spf2.getInt(TOTAL_STEPS, 0);
        textStep.setText(""+numSteps);
        textDist.setText(DF.format((strideLength / MILE_FACTOR) * numSteps));

        btnDebugIncSteps.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                numSteps += 100;
                editor.putInt(TOTAL_STEPS, numSteps+=100);
//                double totalDist = strideLength / MILE_FACTOR * numSteps;
                editor.apply();
                textDist.setText(DF.format((strideLength / MILE_FACTOR) * numSteps));
                textStep.setText(""+spf2.getInt(TOTAL_STEPS, 0));
            }
        });
    }

    public void launchActivity() {
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        startActivity(intent);
    }

    public void launchHeightActivity() {
        Intent intent = new Intent( this, StartPage.class );
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }

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
        running = false;
        // if you unregister the hardware will stop detecting steps
        // sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            textStep.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

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
}

/*
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
    if(!previouslyStarted) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
        edit.commit();
        showHelp();
    }
 */