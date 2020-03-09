package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseRouteDao implements RouteDao {
    public static final String ROUTE_COLLECTION_KEY = "routes";

    FirebaseFirestore db = FirebaseFirestore.getInstance();;

    public void insertAll(Route... r) {
        if (db == null) {
            return;
        }

        for (Route route : r) {
            db.collection(ROUTE_COLLECTION_KEY).document(route.name).set(route);
        }
    }

    public void delete(String routeName) {
        if (db == null) {
            return;
        }

        db.collection(ROUTE_COLLECTION_KEY).document(routeName).delete();
    }

//    @Query("SELECT * FROM Route r ORDER BY name ASC")
    public void retrieveAllRoutes(OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(ROUTE_COLLECTION_KEY).orderBy("name", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(listener);
    }

//    @Query("SELECT * FROM Route r WHERE r.name=:routeName")
    public void findName(String routeName, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(ROUTE_COLLECTION_KEY).whereEqualTo("name", routeName).get()
                .addOnCompleteListener(listener);
    }

    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(ROUTE_COLLECTION_KEY).addSnapshotListener(listener);
    }
}
