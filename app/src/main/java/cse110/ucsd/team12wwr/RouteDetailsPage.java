package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class RouteDetailsPage extends AppCompatActivity {

    // Public constants for string intents
    public static final String TITLE = "ROUTE_TITLE";
    public static final String START = "START_POINT";
    public static final String END = "END_POINT";
    public static final String TIME = "TOTAL_TIME";
    public static final String DIST = "TOTAL_DISTANCE";
    public static final String PATH = "PATH_TYPE";
    public static final String TERRAIN = "TERRAIN_TYPE";
    public static final String INCLINE = "INCLINE_TYPE";
    public static final String SURFACE = "SURFACE_TYPE";
    public static final String DIFFICULTY = "SELECTED_DIFFICULTY";
    public static final String NOTES = "NOTES_CONTENT";

    private static final String TAG = "RouteDetailsPage";

    // Header
    TextView routeTitle = findViewById(R.id.route_title_detail);
    TextView startPoint = findViewById(R.id.start_textview);
    TextView endPoint = findViewById(R.id.end_textview);

    // Metrics
    TextView totalTime = findViewById(R.id.total_time_detail);
    TextView totalDist = findViewById(R.id.dist_details);

    // Spinner info
    TextView path = findViewById(R.id.path_details);
    TextView terrain = findViewById(R.id.terrain_deets);
    TextView incline = findViewById(R.id.incline_deets);
    TextView surface = findViewById(R.id.texture_details);

    // Extra info
    TextView difficulty = findViewById(R.id.diff_detail);
    TextView notes = findViewById(R.id.notes_content);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);

        Button back = findViewById(R.id.back_button);
        Button edit = findViewById(R.id.edit_route);
        Button start = findViewById(R.id.start_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRouteInfoActivity();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntentionalActivity();
            }
        });

    }

    public void launchRouteInfoActivity() {
        Log.d(TAG, "launchRouteInfoActivity: launching the route information page");
        Intent intent = new Intent(this, RouteInfoActivity.class);

        startActivity(intent);
    }

    public void launchIntentionalActivity() {
        Log.d(TAG, "launchActivity: launching the walking activity");
        Intent intent = new Intent(this, IntentionalWalkActivity.class);
        passHeaderInfo(intent, routeTitle, startPoint, endPoint);
        passMetrics(intent, totalTime, totalDist);
        startActivity(intent);
    }

    public void passHeaderInfo(Intent intent, TextView routeTitle, TextView startPoint, TextView endPoint) {
        intent.putExtra(TITLE, extractString(routeTitle));
        intent.putExtra(START, extractString(startPoint));
        intent.putExtra(END, extractString(endPoint));
    }

    public void passMetrics(Intent intent, TextView totalTime, TextView totalDist) {
        intent.putExtra(TIME, extractString(totalTime));
        intent.putExtra(DIST, extractString(totalDist));
    }

    public void passSpinnerInfo(Intent intent, TextView path, TextView incline, TextView terrain, TextView surface) {

    }

    public void passExtraInfo(Intent intent, TextView difficulty, TextView notes) {

    }

    public String extractString (TextView textView) {
        return textView.getText().toString();
    }


}
