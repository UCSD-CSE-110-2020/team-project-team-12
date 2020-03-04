package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

//redo like all of this, there's not even a point to having pedometer service
//just fake the main screen, but change the buttons to be like increment walk and set current time
//and then for some reason have a clock displayed

public class DebugWalkActivity extends AppCompatActivity {

    private long stepCount;
    private boolean isBound = false;
    /*
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            Log.i("DebugWalkActivity.onServiceConnected", "PedometerService Connection Initializing");
            PedometerService.LocalService localService = (PedometerService.LocalService)service;
            try {
                pedService = localService.getService();
            }
            catch(Exception e){
                Log.i("Not real", "NOT REAL THIS DOESN'T HAPPEN");
            }
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };*/
    int totalHeight;
    double strideLength;
    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final int MILE_FACTOR = 63360;

    final String HEIGHT_SPF_NAME = "HEIGHT";
    final String FEET_KEY = "FEET";
    final String INCHES_KEY = "INCHES";

    int hours;
    int minutes;
    int seconds;

    boolean userTimeInputFlag = false;

    Date currentTime;

    long temp;

    SharedPreferences spf;

    long userInputTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_walk);

        //Intent intent = new Intent(this, PedometerService.class);
        //bindService(intent, serviceConnection, 0);
        //isBound = bindService(intent, serviceConnection, 0);
        //pedService.startService(intent);

        Calendar newCal = Calendar.getInstance();
        currentTime = Calendar.getInstance().getTime();
        //hours = newCal.HOUR_OF_DAY;
        //minutes = newCal.MINUTE;
        //seconds = newCal.SECOND;
        hours = currentTime.getHours();
        minutes = currentTime.getMinutes();
        seconds = currentTime.getSeconds();
        TextView currentTimeText = (TextView) findViewById(R.id.text_time_value);
        //currentTimeText.setText(currentTime);
        //Log.i("currentTime is", "" + currentTime);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    //hours = newCal.HOUR_OF_DAY;
                    //minutes = newCal.MINUTE;
                    //seconds = newCal.SECOND;
                    currentTime = Calendar.getInstance().getTime();
                    hours = currentTime.getHours();
                    minutes = currentTime.getMinutes();
                    seconds = currentTime.getSeconds();
                    if(seconds<10)
                    currentTimeText.setText(hours + ":" + minutes + ":0" + seconds);
                    else
                    currentTimeText.setText(hours + ":" + minutes + ":" + seconds);
                    Log.i("currentTime is", "" + currentTime);
                    Log.i("MainActivity.startStepUpdaterMethod", "Still waiting for successful bind");
                    if(!userTimeInputFlag)
                    handler.postDelayed(this, 1000);
                    else{
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                currentTimeText.setText("00:00:00");
                                handler2.postDelayed(this, 1000);
                            }
                        }, 1000);
                    }
            }
        }, 1000);


        //THE DEFAULT HEIGHT IS NOW 5'8 BECAUSE I SAID SO
        spf = getSharedPreferences(HEIGHT_SPF_NAME, MODE_PRIVATE);
        int feet = spf.getInt(FEET_KEY, 5);
        int inches = spf.getInt(INCHES_KEY, 8);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );
        strideLength = totalHeight * STRIDE_CONVERSION;
        Log.i("totalHeight", "" + strideLength);
        Log.i("STRIDE_CONVERSION", "" + STRIDE_CONVERSION);

        TextView distance = (TextView) findViewById(R.id.text_distance_value);

        TextView stepsText = (TextView) findViewById(R.id.text_steps_value);

        temp = Long.parseLong(stepsText.getText().toString());

        DecimalFormat df = new DecimalFormat("#.##");

        //stepsText.setText(""+temp);

        Button incrementSteps = (Button) findViewById(R.id.btn_continue_walk);

        incrementSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("DebugWalk", "Bound and disengaged");
                //temp = Long.parseLong(stepsText.getText().toString());
                temp+=100;
                //pedService.setCurrentSteps(temp);
                stepsText.setText(temp+"");
                Log.i("strideLength", "" + strideLength);
                Log.i("MILE_FACTOR", "" + MILE_FACTOR);
                Log.i("temp", "" + temp);
                distance.setText(df.format((strideLength / MILE_FACTOR) * temp));
            }
        });

        Button userTimeInput = (Button) findViewById(R.id.btn_stop_walk);
        userTimeInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                userInputTime = System.currentTimeMillis();
                //userInputTime = 1581892761487;
                currentTime = new Date(userInputTime);

                hours = currentTime.getHours();
                minutes = currentTime.getMinutes();
                seconds = currentTime.getSeconds();

                if(seconds<10)
                    currentTimeText.setText(hours + ":" + minutes + ":0" + seconds);
                else
                    currentTimeText.setText(hours + ":" + minutes + ":" + seconds);
                //currentTimeText.setText(""+currentTime);

                Log.i("userInputTime","" + userInputTime);
                userTimeInputFlag=true;
            }
        });
    }
}
