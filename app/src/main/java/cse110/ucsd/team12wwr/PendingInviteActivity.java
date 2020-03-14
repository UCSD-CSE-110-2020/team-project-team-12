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

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Invitation;
import cse110.ucsd.team12wwr.firebase.InvitationDao;
import cse110.ucsd.team12wwr.firebase.UserDao;

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
        InvitationDao dao = DaoFactory.getInvitationDao();
        dao.findInviteByEmail(email, task -> {
            Log.i("checkForInvites ", email);
            if (task.isSuccessful()) {
                inv = null;
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        inv = document.toObject(Invitation.class);
                        Log.i("checkForInvites ", ""+inv.toString());
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
                UserDao dao1 = DaoFactory.getUserDao();
                dao1.updateTeamID(userEmail, originTeam);
                InvitationDao dao = DaoFactory.getInvitationDao();
                dao.delete(userEmail);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamName.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
                decline.setVisibility(View.INVISIBLE);
                InvitationDao dao = DaoFactory.getInvitationDao();
                dao.delete(userEmail);
            }
        });
        teamName.setText(teamID);
        teamName.setVisibility(View.VISIBLE);
        accept.setVisibility(View.VISIBLE);
        decline.setVisibility(View.VISIBLE);
    }
}