package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public interface WalkDao {
    void insertAll(Walk... w);

//    @Query("SELECT * FROM Walk w WHERE NOT EXISTS (SELECT a.time FROM Walk a WHERE a.time > w.time)")
    void findNewestEntries(OnCompleteListener<QuerySnapshot> listener);

//    @Query("SELECT * FROM Walk w WHERE w.routeName=:routeName ORDER BY time DESC")
    void findByRouteName(String routeName, OnCompleteListener<QuerySnapshot> listener);

    void listenForChanges(EventListener<QuerySnapshot> listener);
}
