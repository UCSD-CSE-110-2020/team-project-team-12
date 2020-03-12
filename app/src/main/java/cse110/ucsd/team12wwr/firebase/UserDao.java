package cse110.ucsd.team12wwr.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public interface UserDao {
    void insertAll(User... users);

    void updateTeamID(String userID, String teamID);

    void delete(String userID);

    //    @Query("SELECT * FROM User u WHERE u.userID=:userID")
    void findUserByID(String userID, OnCompleteListener<QuerySnapshot> listener);

    //    @Query("SELECT * FROM User u WHERE u.teamID=:teamID")
    void findUsersByTeam(String teamID, OnCompleteListener<QuerySnapshot> listener);

    void listenForChanges(EventListener<QuerySnapshot> listener);
}
