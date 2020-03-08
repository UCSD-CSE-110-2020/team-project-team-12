package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseInvitationDao implements InvitationDao {
    public static final String INVITATION_COLLECTION_KEY = "invitations";

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
            db.collection(INVITATION_COLLECTION_KEY).document(invitation.inviteeID).set(invitation);
        }
    }

    public void insert(Invitation invite){
        if (db == null) {
            return;
        }
            db.collection(INVITATION_COLLECTION_KEY).document(invite.inviteeID).set(invite);
    }

    public void delete(String inviteeID) {
        if (db == null) {
            return;
        }

        db.collection(INVITATION_COLLECTION_KEY).document(inviteeID).delete();
    }

    //    @Query("SELECT * FROM Invitation i WHERE i.inviteeID=:inviteeID")
    public void findInviteByEmail(String inviteeID, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(INVITATION_COLLECTION_KEY).whereEqualTo("inviteeID", inviteeID).get()
                .addOnCompleteListener(listener);
    }

    //    @Query("SELECT * FROM Invitation i WHERE i.teamID=:teamID")
    public void findInviteByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(INVITATION_COLLECTION_KEY).whereEqualTo("teamID", teamID).get()
                .addOnCompleteListener(listener);
    }

    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(INVITATION_COLLECTION_KEY).addSnapshotListener(listener);
    }
}
