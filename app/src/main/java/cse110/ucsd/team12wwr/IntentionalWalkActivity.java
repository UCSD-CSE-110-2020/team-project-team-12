package cse110.ucsd.team12wwr;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntentionalWalkActivity extends AppCompatActivity {
    TextView stopwatchText;
    private AsyncTaskRunner runner;
    long timeBuff, msTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentional_walk);

        Button debugStartButton = findViewById(R.id.debug_btn_start_walk);
        Button stopButton = findViewById(R.id.btn_stop_walk);
        stopwatchText = findViewById(R.id.text_time_value);

        debugStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                runner = new AsyncTaskRunner();
                runner.execute();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += msTime;
                if (runner != null) {
                    runner.cancel(true);
                }
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private long startTime;

        @Override
        protected void onPreExecute() {
            startTime = SystemClock.uptimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            while (true){
                msTime = SystemClock.uptimeMillis() - startTime;

                long updateTime = timeBuff + msTime;

                int seconds = (int) (updateTime / 1000);
                int minutes = (seconds / 60)  % 60;
                int hours = (seconds / 3600)  % 60;
                seconds %= 60;

                publishProgress(String.format("Async %02d:%02d:%02d", hours, minutes, seconds));
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    return e.toString();
                }
            }

        }

//        @Override
//        protected void onPostExecute(String result) {
//            // execution of result of Long time consuming operation
//            stopwatchText.setText(result);
//        }

        @Override
        protected void onProgressUpdate(String... text) {
            stopwatchText.setText(text[0]);
        }

//        @Override
//        protected void onCancelled() {
//            stopwatchText.setText("Cancelled");
//        }
    }
}
