package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

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
    public void updateScheduledState(String teamID, boolean isScheduled) {
        return;
    }

    @Override
    public void updateVoteMap(String teamID, Map<String, Schedule.Vote> userVoteMap) {
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
