package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cse110.ucsd.team12wwr.recycler.Item;
import cse110.ucsd.team12wwr.recycler.RecycleAdapter;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ProposedWalkScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static List<Item> itemList;
    private RecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_walk_screen);

        recyclerView = (RecyclerView) findViewById(R.id.vote_view);

        itemList = new ArrayList<>();
        Item item = new Item();
        item.setName("Jeffrey");
        itemList.add(item);

        Item item1 = new Item();
        item1.setName("Geoffrey");
        itemList.add(item1);

        Item item2 = new Item();
        item1.setName("Joffrey");
        itemList.add(item2);

        recycleAdapter = new RecycleAdapter(this);

        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
}
