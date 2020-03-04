package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class StartPage extends AppCompatActivity {

    /* constants */
    private static final String TAG = "StartPage";

    final String HEIGHT_SPF_NAME = "HEIGHT";
    final String FEET_KEY = "FEET";
    final String INCHES_KEY = "INCHES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        Log.d(TAG, "onCreate: Launched Height Start Page");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Log.d(TAG, "onCreate: Spinners created and set to 0 ft 0 inches");
        // Button to launch new page
        Button launchMainPage = (Button) findViewById(R.id.enter_button);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the spinners
                int feetIndex = foot_dropdown.getSelectedItemPosition();
                int feetValue = Integer.valueOf(foot_items[feetIndex]);
                int inchIndex = inch_dropdown.getSelectedItemPosition();
                int inchValue = Integer.valueOf(inch_items[inchIndex]);

                if ( feetValue == 0 && inchValue == 0 ) {
                    Log.d(TAG, "onClick: Invalid height of 0 feet 0 inches");
                    Toast.makeText(StartPage.this, "You can't be 0 feet and 0 inches! Impossible!", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d(TAG, "onClick: Valid height and now adding to sharedPreferences");

                    SharedPreferences sharedPreferences = getSharedPreferences(HEIGHT_SPF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(FEET_KEY, feetValue);
                    editor.putInt(INCHES_KEY, inchValue);
                    editor.apply();

                    Log.d(TAG, "onClick: Height is: " + sharedPreferences.getInt(FEET_KEY, 0) + " ft " + sharedPreferences.getInt(INCHES_KEY, 0) + " in");
                    Log.d(TAG, "onClick: finishing height activity");
                    finish();
                }
            }
        });


        /*FitnessServiceFactory.put("GOOGLE_FIT", new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });*/
    }

}
