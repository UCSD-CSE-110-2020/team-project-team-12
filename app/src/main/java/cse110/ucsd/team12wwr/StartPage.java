package cse110.ucsd.team12wwr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the dropdown menus for height
        final Spinner foot_dropdown = findViewById(R.id.feet_spinner);
        final Integer[] foot_items = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        ArrayAdapter<Integer> feet_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, foot_items);
        foot_dropdown.setAdapter(feet_adapter);

        final Spinner inch_dropdown = findViewById(R.id.inch_spinner);
        final Integer[] inch_items = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        ArrayAdapter<Integer> inch_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, inch_items);
        inch_dropdown.setAdapter(inch_adapter);

        // Button to launch new page
        Button launchMainPage = (Button) findViewById(R.id.enter_button);

        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int feetIndex = foot_dropdown.getSelectedItemPosition();
                int feetValue = Integer.valueOf(foot_items[feetIndex]);

                int inchIndex = inch_dropdown.getSelectedItemPosition();
                int inchValue = Integer.valueOf(inch_items[inchIndex]);

                SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("feet", feetValue);
                editor.putInt("inches", inchValue);

                editor.apply();

                launchMainActivity(sharedPreferences);
            }
        });
    }

    public void launchMainActivity(SharedPreferences spf) {

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("feet", spf.getString("feet", "0"));
        //intent.putExtra("inches", spf.getString("inches", "0"));
        startActivity(intent);
    }

}
