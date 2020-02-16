package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DebugWalkActivity extends AppCompatActivity {

    private long stepCount;
    private PedometerService pedService;
    private boolean isBound = false;

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_walk);

        Intent intent = new Intent(this, PedometerService.class);
        //bindService(intent, serviceConnection, 0);
        isBound = bindService(intent, serviceConnection, 0);

        Button incrementSteps = (Button) findViewById(R.id.btn_continue_walk);
        TextView stepsText = (TextView) findViewById(R.id.text_steps_value);
        incrementSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isBound) {
                    Log.i("DebugWalk", "Service not yet bound, click later");
                    return;
                }
                if(isBound && pedService!=null &&!pedService.getEngaged()) {
                    Log.i("DebugWalk", "Service bound, GFIT engaged");
                    pedService.turnOffStepTracking();
                    return;
                }
                Log.i("DebugWalk", "Bound and disengaged");
                long temp = Long.parseLong(stepsText.getText().toString());
                temp+=100;
                pedService.setCurrentSteps(temp);
                stepsText.setText(temp+"");
            }
        });
    }
}
