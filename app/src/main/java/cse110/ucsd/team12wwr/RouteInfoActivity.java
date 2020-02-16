package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import cse110.ucsd.team12wwr.database.Route;
import cse110.ucsd.team12wwr.database.RouteDao;
import cse110.ucsd.team12wwr.database.WWRDatabase;

import android.app.Instrumentation;
import android.database.sqlite.SQLiteConstraintException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static android.os.Process.setThreadPriority;


/**
 * If you need to save a route, you first create a Route object
 *
 * Route thingToInsert = new Route();
 * thingToInsert.name = name of the route;
 * thingToInsert.startingPoint = blabla;
 * etcetc (you can look at what fields are available in Route.java)
 *
 * Then, you call the RouteDao from the database. Dao is the Data Access Object. Should be something like
 * RouteDao dao = db.routeDao();
 *
 * And then, I think it's
 * dao.insertAll(thingToInsert);
 *
 *
 * 1. Route is what the database looks like. Every single variable there represents a "column" in the table
 * 2. You use the RouteDao to interact with the database. When you try to get stuff from the database, you call
 *    the methods inside RouteDao (e.g. retrieveAllRoutes), and you'll receive a Route object. All the informations
 *    you need will be in the Route object.
 * 3. Similarly, if you use the RouteDao object to insert stuff into the database. You first create a new Route
 *    object, and then fill out the variables with the information you want to insert. Then, you just call insertAll
 *    in the RouteDao.
 * 4. You CANNOT access the database in the UI thread, because it'll throw an exception. Use an
 *    ExecutorService instead. You should look at my code in MainActivity for getting stuff from the DB,
 *    or IntentionalWalkActivity for putting stuff into the DB
 *
 * i think the method to find the routes by name is RouteDao.findName(String name) or something lke that
 *
 *         stopButton.setOnClickListener((view) -> {
 *             ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
 *             databaseWriteExecutor.execute(() -> {
 *                 WWRDatabase walkDb = WWRDatabase.getInstance(this);
 *                 WalkDao dao = walkDb.walkDao();
 *
 *                 Walk newEntry = new Walk();
 *                 newEntry.time = System.currentTimeMillis();
 *                 newEntry.duration = stopwatchText.getText().toString();
 *                 newEntry.steps = stepsText.getText().toString();
 *                 newEntry.distance = distanceText.getText().toString();
 *
 *                 dao.insertAll(newEntry);
 *             });
 *
 *             finish();
 *         });
 *
 *         Route newEntry = new Route();
 *         newEntry.name = "Mission Hills Tour";
 *         newEntry.startingPoint = "Kufuerstendamm & Friedrichstrasse";
 *         newEntry.routeType = Route.RouteType.LOOP;
 *         newEntry.hilliness = Route.Hilliness.FLAT;
 *         newEntry.surfaceType = Route.SurfaceType.STREETS;
 *         newEntry.evenness = Route.Evenness.EVEN_SURFACE;
 *         newEntry.difficulty = Route.Difficulty.MODERATE;
 *         newEntry.notes = "This is a pretty dope route wanna do it again";
 *         db.routeDao().insertAll(newEntry);
 */

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
    final int threadID = android.os.Process.getThreadPriority(android.os.Process.myTid());
    final Thread currThread = Thread.currentThread();
    /* Favorite button */
    boolean isFavorite = false;

    /* Setting spinners and textfields */
    String routeTitle, startPosition, endLocation, notesField;
    Boolean isHilly, isStreet, isEven, isLoop;

    /* Difficulty */
    boolean isEasy = false;
    boolean isModerate = false;
    boolean isHard = false;

    /* New or old route */
    boolean isNewRoute = true;

    Drawable defaultColor;
    String currRouteName;
//    Route currRoute;

    /* Database */
    WWRDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        getSupportActionBar().setTitle("Route Information");

        resetFields();
        isNewRoute = true;

        // Init database
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        db = Room.inMemoryDatabaseBuilder(context, WWRDatabase.class).build();
        // TODO: Retrieve passed in object for route() through intent
//        String currRouteName = (String) getIntent().getExtras().getString("route_data");
//        currRouteName = "Fun Route";
        //System.out.println(currRouteName);
        if (currRouteName != null) {
            isNewRoute = false;
            Log.d(TAG, "onCreate: This is a new route we will be creating");
        }

        EditText titleField = findViewById(R.id.title_text);
        EditText startPoint = findViewById(R.id.start_text);
        EditText endPoint = findViewById(R.id.stop_text);
        CheckBox favoriteBtn = findViewById(R.id.favoriteCheckBtn);
        Button cancelBtn = findViewById(R.id.cancel_btn);
        Button saveBtn = findViewById(R.id.save_btn);
        Button easyBtn = findViewById(R.id.easy_btn);
        Button moderateBtn = findViewById(R.id.moderate_btn);
        Button hardBtn = findViewById(R.id.hard_btn);

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
            ExecutorService dbWriteExecutor = Executors.newSingleThreadExecutor();//Executors.newFixedThreadPool(1);
            dbWriteExecutor.execute(() -> {
                WWRDatabase routeDb = WWRDatabase.getInstance(RouteInfoActivity.this);
                RouteDao dao = routeDb.routeDao();

                Route currEntry = dao.findName(currRouteName);

                routeTitle = currEntry.name;
                startPosition = currEntry.startingPoint;
                endLocation = currEntry.endingPoint;
                notesField = currEntry.notes;
                if ( currEntry.routeType != null ) {
                    Log.d(TAG, "onCreate: setting routeType to: " + currEntry.routeType);
                    if (currEntry.routeType == Route.RouteType.LOOP) {
                        isLoop = true;
                    } else {
                        isLoop = false;
                    }
                }
                if ( currEntry.hilliness != null ) {
                    if ( currEntry.hilliness == Route.Hilliness.HILLY ) {
                        isHilly = true;
                    } else {
                        isHilly = false;
                    }
                }
                if ( currEntry.surfaceType != null ) {
                    if ( currEntry.surfaceType == Route.SurfaceType.STREETS ) {
                        isStreet = true;
                    } else {
                        isStreet = false;
                    }
                }
                if (currEntry.evenness != null ) {
                    if ( currEntry.evenness == Route.Evenness.EVEN_SURFACE ) {
                        isEven = true;
                    } else {
                        isEven = false;
                    }
                }
                if ( currEntry.difficulty != null ) {
                    if ( currEntry.difficulty == Route.Difficulty.EASY ) {
                        easyBtn.performClick();
                    } else if ( currEntry.difficulty == Route.Difficulty.MODERATE ) {
                        moderateBtn.performClick();
                    } else {
                        hardBtn.performClick();
                    }
                }
                if ( currEntry.favorite != null ) {
                    if ( currEntry.favorite == Route.Favorite.FAVORITE ) {
                        favoriteBtn.performClick();
                    }
                }

            });

            while( routeTitle == null );
        }

//        routeTitle = currRoute.name;
//        startPosition = currRoute.startingPoint;
//        System.out.println(startPosition);
//        // TODO: get the ending point
//        notesField = currRoute.notes;
//        if ( currRoute.routeType != null ) {
//            Log.d(TAG, "onCreate: setting routeType to: " + currRoute.routeType);
//            if (currRoute.routeType == Route.RouteType.LOOP) {
//                isLoop = true;
//            } else {
//                isLoop = false;
//                System.out.println("not loop");
//            }
//        }
//        if ( currRoute.hilliness != null ) {
//            if ( currRoute.hilliness == Route.Hilliness.HILLY ) {
//                isHilly = true;
//            } else {
//                isHilly = false;
//            }
//        }
//        if ( currRoute.surfaceType != null ) {
//            if ( currRoute.surfaceType == Route.SurfaceType.STREETS ) {
//                isStreet = true;
//            } else {
//                isStreet = false;
//            }
//        }
//        if (currRoute.evenness != null ) {
//            if ( currRoute.evenness == Route.Evenness.EVEN_SURFACE ) {
//                isEven = true;
//            } else {
//                isEven = false;
//            }
//        }
//        if ( currRoute.difficulty != null ) {
//            if ( currRoute.difficulty == Route.Difficulty.EASY ) {
//                isEasy = true;
//            } else if ( currRoute.difficulty == Route.Difficulty.MODERATE ) {
//                isModerate = true;
//            } else {
//                isHard = true;
//            }
//        }

        Log.d(TAG, "onCreate: Page is now set up");

        Log.d(TAG, "onCreate: routeTitle: " + routeTitle);
        Log.d(TAG, "onCreate: startPosition: " + startPosition);
        Log.d(TAG, "onCreate: isLoop: " + isLoop);
        Log.d(TAG, "onCreate: notesField: " + notesField);
        
        // TODO: Check if the certain fields have something, if not, then set everything to nonetype
        if ( !isNewRoute ) {
            titleField.setText(routeTitle);
            if (startPosition != null) {
                startPoint.setText(startPosition);
            }
            if ( endLocation != null ) {
                endPoint.setText(endLocation);
            }
            if ( isLoop != null ) {
                if ( isLoop ) {
                    pathSpinner.setSelection(1);
                } else {
                    pathSpinner.setSelection(2);
                }
            }
            if ( isHilly != null ) {
                if ( isHilly ) {
                    inclineSpinner.setSelection(2);
                } else {
                    inclineSpinner.setSelection(1);
                }
            }
            if ( isStreet != null ) {
                if ( isStreet ) {
                    terrainSpinner.setSelection(1);
                } else {
                    terrainSpinner.setSelection(2);
                }
            }
            if ( isEven != null ) {
                if ( isEven ) {
                    textureSpinner.setSelection(1);
                } else {
                    textureSpinner.setSelection(2);
                }
            }
        }

        // TODO: Set up the favorite button to be whatever favorite is passed in
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
                finish();
            }
        });

        // TODO: Save button: if object is null/no object passed, create new object to store in db
        // TODO: If object exists, just update the object's fields

        // Save Button
        saveBtn.setOnClickListener(new View.OnClickListener() {

            WWRDatabase routeDb = WWRDatabase.getInstance(RouteInfoActivity.this);
            RouteDao dao = routeDb.routeDao();

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Save Button is clicked");
                // Make sure it is not null
                if (TextUtils.isEmpty(titleField.getText())) {
                    Log.d(TAG, "onClick: Title field is null");
                    titleField.setError("You must enter a title for your route!");
                } else {
                    final boolean[] dupeTitle = {false};
                    Log.d(TAG, "onClick: isNewRoute:" + isNewRoute);
                    if (isNewRoute) {
                        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
                        databaseWriteExecutor.execute(() -> {

                            WWRDatabase routeDb = WWRDatabase.getInstance(RouteInfoActivity.this);
                            RouteDao dao = routeDb.routeDao();

                            Route newEntry = new Route();
                            newEntry.name = titleField.getText().toString();
                            newEntry.startingPoint = startPoint.getText().toString();
                            newEntry.endingPoint = endPoint.getText().toString();
                            setFavorite(newEntry, isFavorite);
                            setRouteType(newEntry, pathSpinner);
                            setHilliness(newEntry, inclineSpinner);
                            setSurfaceType(newEntry, terrainSpinner);
                            setEvenness(newEntry, textureSpinner);
                            setDifficulty(newEntry);
                            // TODO: Set notes
                            setNotes(newEntry, "");

                            try {
                                dao.insertAll(newEntry);
                                Log.d(TAG, "onClick: added entry");
                            } catch (SQLiteConstraintException e) {
//                                titleField.setError("Route already exists, use another name!");
                                Log.d(TAG, "onClick: Title already in use");
                                dupeTitle[0] = true;
//                                return;
                            }
                        });

                        if ( dupeTitle[0] ) {
                          titleField.setError("Route already exists, use another name!");
                            Log.d(TAG, "onClick: did not insert route into database");
//                          return;
                        } else {
                            Log.d(TAG, "onClick: inserted a route into database");
                        }
                    } else {
                        ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
                        databaseWriteExecutor.execute(() -> {

                            WWRDatabase routeDb = WWRDatabase.getInstance(RouteInfoActivity.this);
                            RouteDao dao = routeDb.routeDao();

                            Route newEntry = dao.findName(currRouteName);
                            newEntry.name = titleField.getText().toString();
                            newEntry.startingPoint = startPoint.getText().toString();
                            newEntry.endingPoint = endPoint.getText().toString();
                            setFavorite(newEntry, isFavorite);
                            setRouteType(newEntry, pathSpinner);
                            setHilliness(newEntry, inclineSpinner);
                            setSurfaceType(newEntry, terrainSpinner);
                            setEvenness(newEntry, textureSpinner);
                            setDifficulty(newEntry);
                            // TODO: Fix notes
                            setNotes(newEntry, "");
                            dao.update(newEntry);
                            Log.d(TAG, "onClick: Updated route information for old route");

                        });
                    } // End inner else ( if not a new route ) 
                } // End else
            } // End onClick()
        }); // End setOnClickListener()
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
        route.notes = msg;
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
        notesField = null;
    }

}
