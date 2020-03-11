package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MockScheduleDao implements ScheduleDao {
    @Override
    public void insertAll(Schedule... schedules) {
        return;
    }

    @Override
    public void delete(String teamID) {
        return;
    }

    @Override
    public void findScheduleByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        return;
    }
}
