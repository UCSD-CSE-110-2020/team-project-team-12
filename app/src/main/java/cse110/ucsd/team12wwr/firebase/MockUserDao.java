package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MockUserDao implements UserDao {
    @Override
    public void insertAll(User... users) {
        return;
    }

    @Override
    public void updateTeamID(String userID, String teamID) {
        return;
    }

    @Override
    public void delete(String userID) {
        return;
    }

    @Override
    public void findUserByID(String userID, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void findUsersByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        return;
    }
}
