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
        checkForInvites(userEmail);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

    private void checkForInvites(String email) {
        FirebaseInvitationDao dao = new FirebaseInvitationDao();
        dao.findInviteByEmail(email, task -> {
            if (task.isSuccessful()) {
                inv = null;
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        inv = document.toObject(Invitation.class);
                    }
                    originTeam = inv.teamID;
                    displayInvite(inv.teamID);
                }
                catch(NullPointerException e){
                    Log.i("PendingInviteActivity.checkForInvites returns ", "NO SUCH FRIENDSHIP");
                }
            }
        });
    }

    private void displayInvite(String teamID){
        TextView teamName = findViewById(R.id.textView);
        Button accept = findViewById(R.id.button);
        Button decline = findViewById(R.id.button2);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamName.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
                decline.setVisibility(View.INVISIBLE);
                FirebaseUserDao dao1 = new FirebaseUserDao();
                dao1.updateTeamID(userEmail, originTeam);
                FirebaseInvitationDao dao = new FirebaseInvitationDao();
                dao.delete(userEmail);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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