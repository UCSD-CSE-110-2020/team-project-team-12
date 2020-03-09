package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseWalkDao implements WalkDao {
    public static final String WALK_COLLECTION_KEY = "walks";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void insertAll(Walk... w) {
        if (db == null) {
            return;
        }

        for (Walk walk : w) {
            db.collection(WALK_COLLECTION_KEY).add(walk);
        }
    }

//    @Query("SELECT * FROM Walk w WHERE NOT EXISTS (SELECT a.time FROM Walk a WHERE a.time > w.time)")
    public void findNewestEntries(OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(WALK_COLLECTION_KEY).orderBy("time", Query.Direction.DESCENDING).get().
                addOnCompleteListener(listener);
    }

//    @Query("SELECT * FROM Walk w WHERE w.routeName=:routeName ORDER BY time DESC")
    public void findByRouteName(String routeName, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(WALK_COLLECTION_KEY).whereEqualTo("routeName", routeName)
                .orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(listener);
    }

    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(WALK_COLLECTION_KEY).addSnapshotListener(listener);
    }
}
