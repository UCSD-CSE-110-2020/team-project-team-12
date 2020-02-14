package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class RouteInfoActivity extends AppCompatActivity {

    /* Constants */
    private static final String TAG = "RouteInfoActivity";
    final String LOOP = "Loop";
    final String OUT_N_BACK = "Out-and-Back";
    final String FLAT = "Flat";
    final String HILLY = "Hilly";
    final String STREET = "Street";
    final String TRAIL = "Trail";
    final String EVEN = "Even Surface";
    final String UNEVEN = "Uneven Surface";
    final String NONETYPE = "";

    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        getSupportActionBar().setTitle("Route Information");

        // Set spinners
        final Spinner pathSpinner = findViewById(R.id.path_spinner);
        final String[] pathItems = new String[]{NONETYPE, LOOP, OUT_N_BACK};
        ArrayAdapter<String> path_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pathItems);
        pathSpinner.setAdapter(path_adapter);

        final Spinner inclineSpinner = findViewById(R.id.incline_spinner);
        final String[] inclineItems = new String[]{NONETYPE, FLAT, HILLY};
        ArrayAdapter<String> incline_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, inclineItems);
        inclineSpinner.setAdapter(incline_adapter);

        final Spinner terrainSpinner = findViewById(R.id.terrain_spinner);
        final String[] terrainItems = new String[]{NONETYPE, STREET, TRAIL};
        ArrayAdapter<String> terrain_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, terrainItems);
        terrainSpinner.setAdapter(terrain_adapter);

        final Spinner textureSpinner = findViewById(R.id.texture_spinner);
        final String[] textureItems = new String[]{NONETYPE, EVEN, UNEVEN};
        ArrayAdapter<String> texture_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textureItems);
        textureSpinner.setAdapter(texture_adapter);

        Log.d(TAG, "onCreate: Page is now set up");
        // Favorite button
        CheckBox favoriteBtn = findViewById(R.id.favoriteCheckBtn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( favoriteBtn.isChecked() ) {
                    isFavorite = true;
                    Log.d(TAG, "onClick: isFavorite: " + isFavorite);
                } else {
                    isFavorite = false;
                    Log.d(TAG, "onClick: isFavorite: " + isFavorite);
                }
//                    favoriteBtn.setImageResource(R.drawable.);
//                System.out.println("Tag: " + favoriteBtn.getTag());
//                System.out.println(favoriteBtn.getDrawable());
////                if ( favoriteBtn.getTag() == "@android:drawable/star_big_off") {
////                    favoriteBtn.setTag("@android:drawable/star_big_on");
////                    isFavorite = true;
////                } else {
////                    favoriteBtn.setTag("@android:drawable/star_big_off");
////                    isFavorite = false;
////                }
            }
        });

        Button cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

/**
 // Set up the dropdown menus for height
 final Spinner foot_dropdown = findViewById(R.id.feet_spinner);
 final Integer[] foot_items = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
 ArrayAdapter<Integer> feet_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, foot_items);
 foot_dropdown.setAdapter(feet_adapter);
 foot_dropdown.setSelection(0);

 final Spinner inch_dropdown = findViewById(R.id.inch_spinner);
 final Integer[] inch_items = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
 ArrayAdapter<Integer> inch_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, inch_items);
 inch_dropdown.setAdapter(inch_adapter);
 inch_dropdown.setSelection(0);

 */