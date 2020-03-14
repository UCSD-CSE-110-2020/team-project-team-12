package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;
import cse110.ucsd.team12wwr.firebase.WalkDao;
import cse110.ucsd.team12wwr.recycler.Item;
import cse110.ucsd.team12wwr.recycler.RecycleAdapter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProposedWalkScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static List<Item> itemList;
    private RecycleAdapter recycleAdapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_screen);

        recyclerView = (RecyclerView) findViewById(R.id.vote_view);
        SharedPreferences emailPrefs  = this.getSharedPreferences("USER_ID", MODE_PRIVATE);
        String email = emailPrefs.getString("EMAIL_ID", null);

        UserDao userDao = DaoFactory.getUserDao();
        userDao.findUserByID(email, task1 -> {
            if ( task1.isSuccessful()) {
                User user = null;
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    user = document.toObject(User.class);
                }
                if ( user != null) {
                    userDao.findUsersByTeam(user.teamID, task2 -> {
                        userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            userList.add(document.toObject(User.class));
                        }
                        itemList = new ArrayList<>();

                        for (User u : userList) {
                            Item item = new Item();
                            item.setName(u.firstName + " " + u.lastName);
                            itemList.add(item);
                        }

                        recycleAdapter = new RecycleAdapter(this);

                        recyclerView.setAdapter(recycleAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    });
                }
            }
        });
    }
}
