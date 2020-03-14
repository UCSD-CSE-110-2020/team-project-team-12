package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public interface ScheduleDao {
    void insertAll(Schedule... schedules);

    void delete(String teamID);

    void updateScheduledState(String teamID, boolean isScheduled);

    void updateVoteMap(String teamID, Map<String, Schedule.Vote> userVoteMap);

    //    @Query("SELECT * FROM Schedule s WHERE s.teamID=:teamID")
    void findScheduleByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener);

    void listenForChanges(EventListener<QuerySnapshot> listener);
}