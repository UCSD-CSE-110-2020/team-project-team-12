package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public interface RouteDao {
    void insertAll(Route... r);

    void delete(String routeName);

//    @Query("SELECT * FROM Route r ORDER BY name ASC")
    void retrieveAllRoutes(OnCompleteListener<QuerySnapshot> listener);

//    @Query("SELECT * FROM Route r WHERE r.name=:routeName")
    void findName(String routeName, OnCompleteListener<QuerySnapshot> listener);

    void listenForChanges(EventListener<QuerySnapshot> listener);
}
