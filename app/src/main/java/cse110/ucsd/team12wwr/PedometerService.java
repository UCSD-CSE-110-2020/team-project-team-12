package cse110.ucsd.team12wwr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import cse110.ucsd.team12wwr.fitness.FitnessService;

public class PedometerService extends Service {

    //private MainActivity activity;
    private long currentSteps;

    public void setCurrentSteps(long value){
        currentSteps = value;
    }
    public long getCurrentSteps(){
        return currentSteps;
    }

    public PedometerService() {
    }

    private final IBinder iBinder = new LocalService();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }
    class LocalService extends Binder {
        public PedometerService getService(){
            return PedometerService.this;
        }

    }

    //GOOGLE FIT API SHIT GO HURRR
    public void gimmethemsteppies(FitnessService fitnessService){
        //if (fitnessService.getSubscribed()) {
            //fitnessService.updateStepCount();
            //Log.i("gimmethemsteppies", "updateStepCount called");
        //}
        //else{
            //Log.i("gimmethemsteppies", "NOT YET SUBSCRIBED PLZ TRY LATER");
        //}

        Log.i("gimmethemsteppies", "BEFORE HANDLER");
        //fitnessService.updateStepCount();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("gimmethemsteppies", "handler running");
                fitnessService.updateStepCount();
                currentSteps = fitnessService.getStepValue();
                Log.i("gimmethemsteppies", "VALUE STORED IN SERVICE.STEPS IS: " + currentSteps);
                handler.postDelayed(this, 8000);
            }
        }, 8000);
/*
        for (int i=-1; i<0; i--){
            try {

                Log.i("gimmethemsteppies", "updateStepCount called");
                fitnessService.updateStepCount();
                Thread.sleep(15000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}
