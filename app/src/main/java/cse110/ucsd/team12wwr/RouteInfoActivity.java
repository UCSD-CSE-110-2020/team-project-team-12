package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    /* Favorite button */
    boolean isFavorite = false;

    /* Difficult */
    boolean isEasy = false;
    boolean isModerate = false;
    boolean isHard = false;

    Drawable defaultColor;

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
            }
        });

        // Cancel Button
        Button cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Title Field
        EditText titleField = findViewById(R.id.title_text);
        // Save Button
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Save Button is clicked");
                // Make sure it is not null
                if ( TextUtils.isEmpty(titleField.getText()) ) {
                    Log.d(TAG, "onClick: Title field is null, save not finished");
                    titleField.setError("You must enter a title for your route!");
                }
            }
        });

        Button easyBtn = findViewById(R.id.easy_btn);
        Button moderateBtn = findViewById(R.id.moderate_btn);
        Button hardBtn = findViewById(R.id.hard_btn);

        defaultColor = (Drawable) easyBtn.getBackground();

        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Easy Button is clicked");
                //Make sure button hasn't been pressed
                setEasyButton(easyBtn, moderateBtn, hardBtn);
            }
        });

        moderateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Moderate Button is clicked");
                //Make sure button hasn't been pressed'
                setModerateButton(easyBtn, moderateBtn, hardBtn);
            }
        });

        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Hard Button is clicked");
                //Make sure button hasn't been pressed
                setHardButton(easyBtn, moderateBtn, hardBtn);
            }
        });

    }

    public void setEasyButton(Button easyBtn, Button moderateBtn, Button hardBtn) {
        if (isEasy) {
            isEasy = false;
            easyBtn.setBackground(defaultColor);
            easyBtn.setTextColor(Color.BLACK);
        } else  {
            isModerate = false;
            moderateBtn.setBackground(defaultColor);
            moderateBtn.setTextColor(Color.BLACK);
            isHard = false;
            hardBtn.setBackground(defaultColor);
            hardBtn.setTextColor(Color.BLACK);
            isEasy = true;
            easyBtn.setBackgroundColor(Color.DKGRAY);
            easyBtn.setTextColor(Color.WHITE);
        }
        Log.d(TAG, "onClick: isHard: " + isHard );
        Log.d(TAG, "onClick: isModerate: " + isModerate);
        Log.d(TAG, "onClick: isEasy: " + isEasy);
    }

    public void setModerateButton(Button easyBtn, Button moderateBtn, Button hardBtn) {
        if (isModerate) {
            isModerate = false;
            moderateBtn.setBackground(defaultColor);
            moderateBtn.setTextColor(Color.BLACK);
        } else  {
            isEasy = false;
            easyBtn.setBackground(defaultColor);
            easyBtn.setTextColor(Color.BLACK);
            isHard = false;
            hardBtn.setBackground(defaultColor);
            hardBtn.setTextColor(Color.BLACK);
            isModerate = true;
            moderateBtn.setBackgroundColor(Color.DKGRAY);
            moderateBtn.setTextColor(Color.WHITE);
        }
        Log.d(TAG, "onClick: isHard: " + isHard );
        Log.d(TAG, "onClick: isModerate: " + isModerate);
        Log.d(TAG, "onClick: isEasy: " + isEasy);
    }

    public void setHardButton(Button easyBtn, Button moderateBtn, Button hardBtn) {
        if (isHard) {
            isHard = false;
            hardBtn.setBackground(defaultColor);
            hardBtn.setTextColor(Color.BLACK);
        } else {
            isModerate = false;
            moderateBtn.setBackground(defaultColor);
            moderateBtn.setTextColor(Color.BLACK);
            isEasy = false;
            easyBtn.setBackground(defaultColor);
            easyBtn.setTextColor(Color.BLACK);
            isHard = true;
            hardBtn.setBackgroundColor(Color.DKGRAY);
            hardBtn.setTextColor(Color.WHITE);
        }
        Log.d(TAG, "onClick: isHard: " + isHard );
        Log.d(TAG, "onClick: isModerate: " + isModerate);
        Log.d(TAG, "onClick: isEasy: " + isEasy);
    }

}
