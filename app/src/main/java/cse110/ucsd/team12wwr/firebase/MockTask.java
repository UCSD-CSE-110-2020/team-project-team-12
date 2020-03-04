package cse110.ucsd.team12wwr.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executor;

public class MockTask<X> extends Task {
    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Nullable
    @Override
    public Object getResult() {
        return null;
    }

    @Nullable
    @Override
    public Exception getException() {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnCompleteListener(@NonNull OnCompleteListener onCompleteListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener onCompleteListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener onCompleteListener) {
        return null;
    }

    @Nullable
    @Override
    public Object getResult(@NonNull Class aClass) throws Throwable {
        return null;
    }


}
