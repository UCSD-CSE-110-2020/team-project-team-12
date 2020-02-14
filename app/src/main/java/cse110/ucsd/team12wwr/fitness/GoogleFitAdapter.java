package cse110.ucsd.team12wwr.fitness;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import cse110.ucsd.team12wwr.MainActivity;

import static com.google.android.gms.fitness.data.Field.FIELD_STEPS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class GoogleFitAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";
    private GoogleSignInAccount account;
    private boolean subscribed = false;
    private long currentStepValue = 0;

    private MainActivity activity;

    public GoogleFitAdapter(MainActivity activity) {
        this.activity = activity;
        Log.i("GFit Constructor", "constructed");
    }


    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        Log.i("GFit setup", "fitnessOptions built");


        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Log.i("GFit setup", "requesting permissions");
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            Log.i("GFit setup", "GoogleSignIn.hasPermissions = true. start recording");
            //updateStepCount();
            startRecording();
        }
    }

    public boolean getSubscribed(){
        return subscribed;
    }

    private void startRecording() {
        if (account == null) {
            Log.i("startRecording", "GoogleSignInAccount is null");
            return;
        }
        Log.i("startRecording", "GoogleSignInAccount is NOT null");
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("startRecording", "Successfully subscribed!");
                        subscribed = true;
                        activity.bindTheThing();
                        /*
                        try{
                            Thread.sleep(2000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    public void updateStepCount() {
        if (account == null) {
            Log.i("updateStepCount", "GoogleSignInAccount is null in updateStepCount");
            return;
        }
        if(!getSubscribed()){
            Log.i("updateStepCount", "NOT YET SUBSCRIBED PLZ TRY LATER");
            return;
        }

        Log.i("updateStepCount", "GoogleSignInAccount is NOT null in updateStepCount");

        //Log.i("updateStepCount", "getHistoryClient " + Fitness.getHistoryClient(activity, account));
        ///Log.i("updateStepCount", "getHistoryClient.readDailyTotal " + Fitness.getHistoryClient(activity, account).readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA));
        //Above not null returns
        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.i("updateStepCount", "onSuccess called");
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(FIELD_STEPS).asInt();
                                Log.i("updateStepCount", "CURRENT STEP COUNT IS " + total);
                                activity.setStepCount(total); //THIS IS WHERE SETSTEPCOUNT HAPPENS
                                currentStepValue = total;

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        })
                .addOnCanceledListener(
                        new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i("updateStepCount", "onCancelled was called");
                    }
                })
        .addOnCompleteListener(
                new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task <DataSet> dataSet) {
                        Log.i("updateStepCount", "onCompleted was called");
                    }
                });
    }


    @Override
    public long getStepValue(){
        return currentStepValue;
    }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
}
