package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cse110.ucsd.team12wwr.MainActivity;

public class FirebaseUserDao {
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
            db.collection("users").document(user.userID).set(user);
        }
    }

    public void delete(String userID) {
        if (db == null) {
            return;
        }

        db.collection("users").document(userID).delete();
    }

    //    @Query("SELECT * FROM User u WHERE u.userID=:userID")
    public Task<QuerySnapshot> findUserByID(String userID) {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("users").whereEqualTo("userID", userID).get();
    }

    //    @Query("SELECT * FROM User u WHERE u.teamID=:teamID")
    public Task<QuerySnapshot> findUsersByTeam(String teamID) {
        if (db == null) {
            return new MockTask<>();
        }

        return db.collection("users").whereEqualTo("teamID", teamID).get();
    }
}
