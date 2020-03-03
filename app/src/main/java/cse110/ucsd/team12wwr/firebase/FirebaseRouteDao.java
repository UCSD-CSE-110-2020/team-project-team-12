package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseRouteDao {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void insertAll(Route... r) {
        for (Route route : r) {
            db.collection("routes").add(route);
        }
    }

//    @Query("SELECT * FROM Route r ORDER BY name ASC")
    public Task<QuerySnapshot> retrieveAllRoutes() {
        return db.collection("routes").orderBy("name", Query.Direction.ASCENDING).get();
    }

//    @Query("SELECT * FROM Route r WHERE r.name=:routeName")
    public Task<QuerySnapshot> findName(String routeName) {
        return db.collection("routes").whereEqualTo("name", routeName).get();
    }
}
