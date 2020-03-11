package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public interface InvitationDao {
    void insertAll(Invitation... invitations);

    void insert(Invitation invite);

    void delete(String inviteeID);

    //    @Query("SELECT * FROM Invitation i WHERE i.inviteeID=:inviteeID")
    void findInviteByEmail(String inviteeID, OnCompleteListener<QuerySnapshot> listener);

    //    @Query("SELECT * FROM Invitation i WHERE i.teamID=:teamID")
    void findInviteByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener);

    void listenForChanges(EventListener<QuerySnapshot> listener);
}
