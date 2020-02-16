package cse110.ucsd.team12wwr.fitness;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class GoogleFitAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private GoogleSignInAccount account;
    private boolean subscribed = false;
    private long currentStepValue = 0;

    private MainActivity activity;

    public GoogleFitAdapter(MainActivity activity) {
        this.activity = activity;
    }


    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Log.i("GoogleFitAdapter.setup", "requesting permissions");
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            Log.i("GoogleFitAdapter.setup", "GoogleSignIn.hasPermissions = true. start recording");
            startRecording();
        }
    }

    public boolean getSubscribed(){
        return subscribed;
    }

    public void startRecording() {
        if (account == null) {
            Log.i("GoogleFitAdapter.startRecording", "GoogleSignInAccount is null");
            return;
        }
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("GoogleFitAdapter.startRecording", "Successfully subscribed!");
                        subscribed = true;
                        activity.bindPedService();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("GoogleFitAdapter.startRecording", "There was a problem subscribing.");
                    }
                });
    }


    public void updateStepCount() {
        if (account == null) {
            Log.i("GoogleFitAdapter.updateStepCount", "GoogleSignInAccount is null");
            return;
        }
        if(!getSubscribed()){
            Log.i("GoogleFitAdapter.updateStepCount", "GoogleFit not yet subscribed");
            return;
        }
        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                //Log.i("GoogleFitAdapter.updateStepCount", "onSuccess called");
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(FIELD_STEPS).asInt();
                                //Log.i("GoogleFitAdapter.updateStepCount", "CURRENT STEP COUNT IS " + total);
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
                    public void onComplete(@NonNull Task <DataSet> dataSet) {
                        //Log.i("GoogleFitAdapter.updateStepCount", "onCompleted was called");
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
