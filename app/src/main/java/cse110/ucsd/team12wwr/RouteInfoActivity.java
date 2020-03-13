package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Route;
import cse110.ucsd.team12wwr.firebase.RouteDao;
import cse110.ucsd.team12wwr.firebase.Walk;

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

    /* Setting spinners and textfields */
    String routeTitle, startPosition, endLocation, totalDistance, totalTime;
    Boolean isHilly, isStreet, isEven, isLoop;

    /* Difficulty */
    boolean isEasy = false;
    boolean isModerate = false;
    boolean isHard = false;

    /* New or old route */
    boolean isNewRoute = true;

    Drawable defaultColor;
    String currRouteName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        SharedPreferences emailPref = getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = emailPref.getString("EMAIL_ID", null);

        getSupportActionBar().setTitle("Route Information");

        resetFields();

        EditText titleField = findViewById(R.id.title_text);
        EditText startPoint = findViewById(R.id.start_text);
        EditText endPoint = findViewById(R.id.stop_text);
        EditText notesEntry = findViewById(R.id.notes_entry);
        TextView totalDistText = findViewById(R.id.dist_textEdit);
        TextView totalTimeText = findViewById(R.id.totalTime_textEdit);
        CheckBox favoriteBtn = findViewById(R.id.favoriteCheckBtn);
        Button cancelBtn = findViewById(R.id.cancel_btn);
        Button saveBtn = findViewById(R.id.save_btn);
        Button easyBtn = findViewById(R.id.easy_btn);
        Button moderateBtn = findViewById(R.id.moderate_btn);
        Button hardBtn = findViewById(R.id.hard_btn);


        // TODO: Retrieve passed in object for route() through intent
        if ( getIntent().hasExtra("ROUTE_TITLE")) {
            currRouteName = getIntent().getExtras().getString("ROUTE_TITLE");
            Log.d(TAG, "onCreate: currentRouteName: " + currRouteName);
        }
        if ( getIntent().hasExtra("distance") ) {
            totalDistance = getIntent().getExtras().getString("distance");
            Log.d(TAG, "onCreate: totalDistance passed in: " + totalDistance);
            totalDistText.setText(totalDistance);
        }
        if (getIntent().hasExtra("duration") ) {
            totalTime = (String) getIntent().getExtras().getString("duration");
            totalTimeText.setText(totalTime);
            Log.d(TAG, "onCreate: totalTime passed in: " + totalTime);
        }

        // TODO: Remove Route Title
        if (currRouteName != null ) { // && !currRouteName.equals("Route Title")) {
            isNewRoute = false;
            Log.d(TAG, "onCreate: This is not a new route we will be creating");
        }

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

        Log.d(TAG, "onCreate: Populating fields when isNewRoute: " + isNewRoute );
        if ( !isNewRoute ) {
            RouteDao routeDao = DaoFactory.getRouteDao();
            routeDao.findName(currRouteName, task -> {
                if (task.isSuccessful()) {
                    Route newRoute = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (newRoute == null) {
                            newRoute = document.toObject(Route.class);
                        } else {
                            Log.w(TAG, "There is a duplicate route entry in the database!");
                        }
                    }

                    if (newRoute != null) {
                        if (newRoute.name != null) {
                            titleField.setText(newRoute.name);
                        }

                        if (newRoute.startingPoint != null) {
                            startPoint.setText(newRoute.startingPoint);
                        }

                        if (newRoute.endingPoint != null) {
                            endPoint.setText(newRoute.endingPoint);
                        }

                        if (newRoute.difficulty != null) {
                            if (newRoute.difficulty == Route.Difficulty.EASY) {
                                setEasyButton(easyBtn, moderateBtn, hardBtn);
                            } else if (newRoute.difficulty == Route.Difficulty.MODERATE) {
                                setModerateButton(easyBtn, moderateBtn, hardBtn);
                            } else {
                                setHardButton(easyBtn, moderateBtn, hardBtn);
                            }
                        }

                        if (newRoute.evenness != null) {
                            if (newRoute.evenness == Route.Evenness.EVEN_SURFACE) {
                                textureSpinner.setSelection(1);
                            } else if (newRoute.evenness == Route.Evenness.UNEVEN_SURFACE) {
                                textureSpinner.setSelection(2);
                            }
                        }

                        if (newRoute.hilliness != null) {
                            if (newRoute.hilliness == Route.Hilliness.FLAT) {
                                inclineSpinner.setSelection(1);
                            } else if (newRoute.hilliness == Route.Hilliness.HILLY) {
                                inclineSpinner.setSelection(2);
                            }
                        }

                        if (newRoute.routeType != null) {
                            if (newRoute.routeType == Route.RouteType.LOOP) {
                                pathSpinner.setSelection(1);
                            } else if (newRoute.routeType == Route.RouteType.OUT_AND_BACK) {
                                pathSpinner.setSelection(2);
                            }
                        }

                        if (newRoute.surfaceType != null) {
                            if (newRoute.surfaceType == Route.SurfaceType.STREETS) {
                                terrainSpinner.setSelection(1);
                            } else if (newRoute.surfaceType == Route.SurfaceType.TRAIL) {
                                terrainSpinner.setSelection(2);
                            }
                        }

                        if ( newRoute.favorite != null ) {
                            if ( newRoute.favorite == Route.Favorite.FAVORITE) {
                                favoriteBtn.performClick();
                            }
                        }

                        if ( newRoute.notes != null ) {
                            notesEntry.setText(newRoute.notes);
                        }

                        if ( totalTime != null ) {
                            totalTimeText.setText(totalTime);
                        }

                        if ( totalDistance != null ) {
                            totalDistText.setText(totalDistance);
                        }
                    }
                }
            });
        }

        Log.d(TAG, "onCreate: Page is now set up");

        // Favorite button
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteBtn.isChecked()) {
                    isFavorite = true;
                    Log.d(TAG, "onClick: isFavorite: " + isFavorite);
                } else {
                    isFavorite = false;
                    Log.d(TAG, "onClick: isFavorite: " + isFavorite);
                }
            }
        });

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

        // Cancel Button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        // Save Button
        saveBtn.setOnClickListener(view -> {
            Log.d(TAG, "onClick: Save Button is clicked");
            // Make sure it is not null
            if (TextUtils.isEmpty(titleField.getText())) {
                Log.d(TAG, "onClick: Title field is null");
                titleField.setError("You must enter a title for your route!");
                return;
            }
            
            final boolean[] dupeTitle = {false};
            Log.d(TAG, "onClick: isNewRoute:" + isNewRoute);

            RouteDao dao = DaoFactory.getRouteDao();
            dao.findName(currRouteName, task -> {
                if (task.isSuccessful()) {
                    Route newEntry = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (newEntry == null) {
                            newEntry = document.toObject(Route.class);
                        }
                    }

                    if (isNewRoute) {
                        newEntry = new Route();
                    }
                    String oldEntryName = newEntry.name;

                    newEntry.name = titleField.getText().toString();
                    newEntry.startingPoint = startPoint.getText().toString();
                    newEntry.endingPoint = endPoint.getText().toString();
                    setFavorite(newEntry, isFavorite);
                    setRouteType(newEntry, pathSpinner);
                    setHilliness(newEntry, inclineSpinner);
                    setSurfaceType(newEntry, terrainSpinner);
                    setEvenness(newEntry, textureSpinner);
                    setDifficulty(newEntry);
                    setNotes(newEntry, notesEntry.getText().toString());
                    newEntry.userID = userEmail;

                    if (isNewRoute) {
                        try {
                            dao.insertAll(newEntry);
                            Log.d(TAG, "onClick: added entry");
                        } catch (SQLiteConstraintException e) {
                            Log.d(TAG, "onClick: Title already in use");
                            dupeTitle[0] = true;
                            return;
                        }
                    } else {
                        dao.delete(oldEntryName);
                        dao.insertAll(newEntry);
                        Log.d(TAG, "onClick: Updated route information for old route");
                    }
                }
            });

            Intent resultIntent = new Intent();
            resultIntent.putExtra("routeTitle", titleField.getText().toString());
            Log.d(TAG, "onClick: resultIntent has: " + resultIntent.hasExtra("routeTitle"));
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    public void setFavorite( Route route, Boolean isFavorite ) {
        if ( isFavorite ) {
            route.favorite = Route.Favorite.FAVORITE;
        } else {
            route.favorite = Route.Favorite.NOT_FAVORITE;
        }
    }

    public void setRouteType( Route route, Spinner spinner ) {
        if ( spinner.getSelectedItem() == LOOP ) {
            route.routeType = Route.RouteType.LOOP;
            Log.d(TAG, "setRouteType: loop");
        } else if ( spinner.getSelectedItem() == OUT_N_BACK ) {
            route.routeType = Route.RouteType.OUT_AND_BACK;
            Log.d(TAG, "setRouteType: out and back");
        } else {
            Log.d(TAG, "setRouteType: None set");
        }
    }

    public void setHilliness( Route route, Spinner spinner ) {
        if ( spinner.getSelectedItem() == FLAT ) {
            route.hilliness = Route.Hilliness.FLAT;
            Log.d(TAG, "setHilliness: flat");
        } else if ( spinner.getSelectedItem() == HILLY ) {
            route.hilliness = Route.Hilliness.HILLY;
            Log.d(TAG, "setHilliness: hilly");
        } else {
            Log.d(TAG, "setHilliness: None set");
        }
    }

    public void setSurfaceType(Route route, Spinner spinner) {
        if ( spinner.getSelectedItem() == STREET ) {
            route.surfaceType = Route.SurfaceType.STREETS;
            Log.d(TAG, "setSurfaceType: streets");
        } else if ( spinner.getSelectedItem() == TRAIL ) {
            route.surfaceType = Route.SurfaceType.TRAIL;
            Log.d(TAG, "setSurfaceType: trail");
        } else {
            Log.d(TAG, "setSurfaceType: None set");
        }
    }

    public void setEvenness(Route route, Spinner spinner) {
        if ( spinner.getSelectedItem() == EVEN ) {
            route.evenness = Route.Evenness.EVEN_SURFACE;
            Log.d(TAG, "setEvenness: even");
        } else if ( spinner.getSelectedItem() == UNEVEN ) {
            route.evenness = Route.Evenness.UNEVEN_SURFACE;
            Log.d(TAG, "setEvenness: uneven");
        } else {
            Log.d(TAG, "setEvenness: None set");
        }
    }

    public void setDifficulty(Route route) {
        if ( isEasy ) {
            route.difficulty = Route.Difficulty.EASY;
            Log.d(TAG, "setDifficulty: easy");
        } else if ( isModerate ) {
            route.difficulty = Route.Difficulty.MODERATE;
            Log.d(TAG, "setDifficulty: moderate");
        } else if ( isHard ) {
            route.difficulty = Route.Difficulty.DIFFICULT;
            Log.d(TAG, "setDifficulty: hard");
        } else {
            Log.d(TAG, "setDifficulty: None set");
        }
    }

    public void setNotes(Route route, String msg) {
        if ( msg != null ) {
            route.notes = msg;
        } else {
            route.notes = "";
        }
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

    public void resetFields() {
        isNewRoute = true;
        isHilly = null;
        isStreet = null;
        isLoop = null;
        isEven = null;
        isFavorite = false;
        isModerate = false;
        isEasy = false;
        isHard = false;
        routeTitle = null;
        startPosition = null;
        endLocation = null;
        totalDistance = null;
        totalTime = null;
    }
}