package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseWalkDao {
    FirebaseFirestore db;
    public FirebaseWalkDao() {
        if (!MainActivity.unitTestFlag) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public void insertAll(Walk... w) {
        if (db == null) {
            return;
        }

        for (Walk walk : w) {
            db.collection("walks").add(walk);
        }
    }

//    @Query("SELECT * FROM Walk w WHERE NOT EXISTS (SELECT a.time FROM Walk a WHERE a.time > w.time)")
    public Task<QuerySnapshot> findNewestEntries() {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("walks").orderBy("time", Query.Direction.DESCENDING).get();
    }

//    @Query("SELECT * FROM Walk w WHERE w.routeName=:routeName ORDER BY time DESC")
    public Task<QuerySnapshot> findByRouteName(String routeName) {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("walks").whereEqualTo("routeName", routeName)
                .orderBy("time", Query.Direction.DESCENDING).get();
    }
}
