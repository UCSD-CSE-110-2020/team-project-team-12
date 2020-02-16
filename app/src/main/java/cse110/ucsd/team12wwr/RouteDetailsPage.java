package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class RouteDetailsPage extends AppCompatActivity {

    private static final String TAG = "RouteDetailsPage";

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
        startActivity(intent);
    }
}
