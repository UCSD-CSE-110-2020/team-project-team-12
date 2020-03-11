package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MockInvitationDao implements InvitationDao {
    @Override
    public void insertAll(Invitation... invitations) {
        return;
    }

    @Override
    public void insert(Invitation invite) {
        return;
    }

    @Override
    public void delete(String inviteeID) {
        return;
    }

    @Override
    public void findInviteByEmail(String inviteeID, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void findInviteByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        return;
    }

    @Override
    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        return;
    }
}
