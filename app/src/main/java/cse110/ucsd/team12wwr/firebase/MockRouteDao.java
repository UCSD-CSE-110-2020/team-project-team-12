package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MockRouteDao implements RouteDao {
    @Override
    public void insertAll(Route... r) {
        return;
    }

    @Override
    public void delete(String routeName) {
        return;
    }

    @Override
    public void retrieveAllRoutes(OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void findName(String routeName, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        return;
    }
}
