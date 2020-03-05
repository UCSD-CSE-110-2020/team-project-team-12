package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import cse110.ucsd.team12wwr.firebase.FirebaseInvitationDao;
import cse110.ucsd.team12wwr.firebase.FirebaseUserDao;
import cse110.ucsd.team12wwr.firebase.Invitation;

public class PendingInviteActivity extends AppCompatActivity {


    Invitation inv;
    String userEmail;
    String originTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_invite);

        userEmail = getIntent().getStringExtra("user Email");
        Log.i("THIS SHIT RIGHT HERE", userEmail);


        //findInviteByEmail
        checkForInvites(userEmail);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);




    }


    private void checkForInvites(String email) {
        FirebaseInvitationDao dao = new FirebaseInvitationDao();
        dao.findInviteByEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                inv = null;
                //Okay technically this would just get whatever the last one into the db was right?
                //fix later
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        inv = document.toObject(Invitation.class);
                    }
                    Log.i("INVITE SENT FROM: ", inv.teamID);
                    originTeam = inv.teamID;
                    displayInvite(inv.teamID);
                }
                catch(NullPointerException e){
                    Log.i("PendingInviteActivity.checkForInvites returns ", "NO SUCH FRIENDSHIP");
                }
            }
        });
        //if (inv != null)
        //displayInvite(inv.teamID);
    }

    private void displayInvite(String teamID){
        Log.i("DISPLAY INVITE ", teamID);
        TextView teamName = findViewById(R.id.textView);
        Button accept = findViewById(R.id.button);
        Button decline = findViewById(R.id.button2);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INVITATION HAS BEEN ", "accepted");
                teamName.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
                decline.setVisibility(View.INVISIBLE);

                FirebaseUserDao dao1 = new FirebaseUserDao();
                dao1.updateTeamID(userEmail, originTeam);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INVITATION HAS BEEN ", "REJECTED");
                teamName.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
                decline.setVisibility(View.INVISIBLE);
                FirebaseInvitationDao dao = new FirebaseInvitationDao();
                dao.delete(userEmail);
            }
        });

        teamName.setText(teamID);

        teamName.setVisibility(View.VISIBLE);
        accept.setVisibility(View.VISIBLE);
        decline.setVisibility(View.VISIBLE);

    }
}