package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseInvitationDao {
    FirebaseFirestore db;
    public FirebaseInvitationDao() {
        if (!MainActivity.unitTestFlag) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public void insertAll(Invitation... invitations) {
        if (db == null) {
            return;
        }

        for (Invitation invitation : invitations) {
            db.collection("invitations").document(invitation.inviteeID).set(invitation);
        }
    }

    public void delete(String inviteeID) {
        if (db == null) {
            return;
        }

        db.collection("invitations").document(inviteeID).delete();
    }

    //    @Query("SELECT * FROM Invitation i WHERE i.inviteeID=:inviteeID")
    public Task<QuerySnapshot> findInviteByEmail(String inviteeID) {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("invitations").whereEqualTo("inviteeID", inviteeID).get();
    }

    //    @Query("SELECT * FROM Invitation i WHERE i.teamID=:teamID")
    public Task<QuerySnapshot> findInviteByTeam(String teamID) {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("invitations").whereEqualTo("teamID", teamID).get();
    }
}
