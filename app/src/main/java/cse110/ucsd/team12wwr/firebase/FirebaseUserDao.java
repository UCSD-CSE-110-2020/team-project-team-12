package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseUserDao implements UserDao {
    public static final String USER_COLLECTION_KEY = "users";
    
    FirebaseFirestore db;
    public FirebaseUserDao() {
        if (!MainActivity.unitTestFlag) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public void insertAll(User... users) {
        if (db == null) {
            return;
        }

        for (User user : users) {
            db.collection(USER_COLLECTION_KEY).document(user.userID).set(user);
        }
    }

    public void updateTeamID(String userID, String teamID){
        if (db == null){
            return;
        }

        db.collection(USER_COLLECTION_KEY).document(userID).update("teamID", teamID);
    }

    public void delete(String userID) {
        if (db == null) {
            return;
        }

        db.collection(USER_COLLECTION_KEY).document(userID).delete();
    }

    //    @Query("SELECT * FROM User u WHERE u.userID=:userID")
    public void findUserByID(String userID, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(USER_COLLECTION_KEY).whereEqualTo("userID", userID).get()
                .addOnCompleteListener(listener);
    }

    //    @Query("SELECT * FROM User u WHERE u.teamID=:teamID")
    public void findUsersByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(USER_COLLECTION_KEY).whereEqualTo("teamID", teamID).get()
                .addOnCompleteListener(listener);
    }

    public void listenForChanges(EventListener<QuerySnapshot> listener) {
        if (db == null) {
            return;
        }

        db.collection(USER_COLLECTION_KEY).addSnapshotListener(listener);
    }
}
