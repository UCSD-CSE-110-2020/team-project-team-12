package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MockWalkDao implements WalkDao {
    @Override
    public void insertAll(Walk... w) {
        return;
    }

    @Override
    public void findNewestEntries(OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void findByRouteName(String routeName, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        return;
    }
}
