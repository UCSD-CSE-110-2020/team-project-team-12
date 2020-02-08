package cse110.ucsd.team12wwr;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntentionalWalkActivity extends AppCompatActivity {
    private TextView stopwatchText;
    private AsyncTaskRunner runner;
    private int timeWhenPaused, timeElapsed;

    private iClock clock;
    // TODO inject dependency on CLOCK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentional_walk);
        clock = new DeviceClock();

        Button debugStartButton = findViewById(R.id.debug_btn_start_walk);
        final Button pauseButton = findViewById(R.id.btn_pause_walk);
        final Button continueButton = findViewById(R.id.btn_continue_walk);
        final Button stopButton = findViewById(R.id.btn_stop_walk);
        stopwatchText = findViewById(R.id.text_time_value);

        debugStartButton.setOnClickListener(new View.OnClickListener(){
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
    }

    protected void setClock(iClock clock) {
        this.clock = clock;
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

                publishProgress(String.format("%02d:%02d:%02d", hours, minutes, seconds));
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
        }

    }
}
