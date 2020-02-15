package cse110.ucsd.team12wwr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import cse110.ucsd.team12wwr.fitness.FitnessService;

public class PedometerService extends Service {
    private long currentSteps;

    public PedometerService() {
    }

    private final IBinder iBinder = new LocalService();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    class LocalService extends Binder {
        public PedometerService getService(){
            return PedometerService.this;
        }

    }


    public void beginStepTracking(FitnessService fitnessService){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fitnessService.updateStepCount();
                setCurrentSteps(fitnessService.getStepValue());
                Log.i("PedometerService.beginStepTracking",
                        "VALUE STORED IN PedometerService.currentSteps IS: " + currentSteps);
                currentSteps = fitnessService.getStepValue();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    public void setCurrentSteps(long value){
        currentSteps = value;
    }

    public long getCurrentSteps(){
        return currentSteps;
    }
}
