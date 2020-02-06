package cse110.ucsd.team12wwr;

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
    Handler handler;
    long timeBuff, msTime, startTime;
    TextView stopwatchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentional_walk);

        Button debugStartButton = findViewById(R.id.debug_btn_start_walk);
        Button stopButton = findViewById(R.id.btn_stop_walk);
        stopwatchText = findViewById(R.id.text_time_value);

        handler = new Handler();

        debugStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();

                handler.postDelayed(runnable, 0);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += msTime;
                handler.removeCallbacks(runnable);
            }
        });
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            msTime = SystemClock.uptimeMillis() - startTime;

            long updateTime = timeBuff + msTime;

            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            seconds %= 60;

            int milliseconds = (int) (updateTime % 1000);

            stopwatchText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0);
        }
    };


}
