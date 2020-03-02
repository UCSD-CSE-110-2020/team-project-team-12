package cse110.ucsd.team12wwr.fitness;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import cse110.ucsd.team12wwr.MainActivity;

import static com.google.android.gms.fitness.data.Field.FIELD_STEPS;

public class GoogleFitUtility {


    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private GoogleSignInAccount account;
    private boolean subscribed = false;
    private long currentStepValue = 0;

    private Activity activity;

    public GoogleFitUtility(Activity activity) {
        Log.i("GoogleFitUtility", "Construction complete");
        this.activity = activity;
    }


    public void init() {
        Log.i("GoogleFitUtility.init", "START OF METHOD");
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);


        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Log.i("GoogleFitUtility.init", "requesting permissions");
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            Log.i("GoogleFitUtility.init", "GoogleSignIn.hasPermissions, account initialized");
            //startRecording();
        }

        Log.i("GoogleFitUtility.init", "END OF METHOD");
        startRecording();

    }


    public void startRecording() {
        Log.i("GoogleFitUtility.startRecording", "START OF METHOD");
        if (account == null) {
            Log.i("GoogleFitUtility.startRecording", "GoogleSignInAccount is null");
            return;
        }
        long startTimer = System.currentTimeMillis();
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("GoogleFitUtility.startRecording", "Successfully subscribed!");
                        long endTimer = System.currentTimeMillis();
                        Log.i("GoogleFitUtility.startRecording", "Time to subscribe: " + (endTimer-startTimer));
                        subscribed = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("GoogleFitUtility.startRecording", "There was a problem subscribing.");
                    }
                });
        Log.i("GoogleFitUtility.startRecording", "END OF METHOD");
    }


    public void updateStepCount() {
        //Log.i("GoogleFitUtility.updateStepCount", "START OF METHOD");
        if (account == null) {
            Log.i("GoogleFitUtility.updateStepCount", "ACCOUNT NULL, MISTAKES MADE");
            return;
        }
        if(!getSubscribed()){
            Log.i("GoogleFitUtility.updateStepCount", "GoogleFit not yet subscribed");
            return;
        }
        Fitness.getHistoryClient(activity, account)
                .readDailyTotalFromLocalDevice(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                //Log.i("GoogleFitAdapter.updateStepCount", "onSuccess called");
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(FIELD_STEPS).asInt();
                                Log.i("GoogleFitUtility.updateStepCount", "CURRENT STEP COUNT IS " + total);
                                Log.i("GoogleFitUtility.updateStepCount", "Is dataset empty: " + dataSet.isEmpty());
                                currentStepValue = total;
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("GoogleFitAdapter.updateStepCount", "There was a problem getting the step count.", e);
                            }
                        })
                .addOnCanceledListener(
                        new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Log.i("GoogleFitAdapter.updateStepCount", "onCanceled was called");
                            }
                        })
                .addOnCompleteListener(
                        new OnCompleteListener<DataSet>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSet> dataSet) {
                                //Log.i("GoogleFitAdapter.updateStepCount", "onCompleted was called");
                            }
                        });
        //Log.i("GoogleFitUtility.updateStepCount", "END OF METHOD");
    }



    public long getStepValue(){
        return currentStepValue;
    }
    public boolean getSubscribed(){
        return subscribed;
    }


    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
}
