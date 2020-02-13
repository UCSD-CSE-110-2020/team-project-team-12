package cse110.ucsd.team12wwr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cse110.ucsd.team12wwr.fitness.FitnessService;

public class PedometerService extends Service {

    //private MainActivity activity;

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
        for (int i=-1; i<0; i--){
            try {
                Thread.sleep(5000);
                System.out.println("CURRENT VALUE OF I: " + i);
                fitnessService.updateStepCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public int tester1(int a){
        return a+1;
    }
}
