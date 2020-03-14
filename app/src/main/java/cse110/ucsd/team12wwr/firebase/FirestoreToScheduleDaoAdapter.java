package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreToScheduleDaoAdapter implements ScheduleDao {
    public static final String SCHEDULE_COLLECTION_KEY = "schedules";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void insertAll(Schedule... schedules) {
        if (db == null) {
            return;
        }

        for (Schedule schedule : schedules) {
            db.collection(SCHEDULE_COLLECTION_KEY).document(schedule.teamID).set(schedule);
        }
    }

    public void delete(String teamID) {
        if (db == null) {
            return;
        }

        db.collection(SCHEDULE_COLLECTION_KEY).document(teamID).delete();
    }

    public void updateScheduledState(String teamID, boolean isScheduled) {
        db.collection(SCHEDULE_COLLECTION_KEY).document(teamID).update("isScheduled", isScheduled);
    }

    public void updateVoteMap(String teamID, Map<String, Schedule.Vote> userVoteMap) {
        db.collection(SCHEDULE_COLLECTION_KEY).document(teamID).update("userVoteMap", userVoteMap);
    }

    //    @Query("SELECT * FROM Schedule s WHERE s.teamID=:teamID")
    public void findScheduleByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(SCHEDULE_COLLECTION_KEY).whereEqualTo("teamID", teamID).get()
                .addOnCompleteListener(listener);
    }

    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(SCHEDULE_COLLECTION_KEY).addSnapshotListener(listener);
    }
}
