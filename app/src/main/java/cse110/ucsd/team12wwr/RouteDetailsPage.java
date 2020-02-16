package cse110.ucsd.team12wwr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RouteDetailsPage extends AppCompatActivity {

    String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details_page);
        Intent intent = getIntent();
        routeName = intent.getStringExtra("name");
        System.err.print("Route Name: ");
        System.err.println(routeName);
        setContentView(R.layout.activity_route_details_page);
        if (routeName != null) {
            TextView textView = (TextView) findViewById(R.id.textView2);
            textView.setText(routeName);
//            db.routeDao.findName(routeName);

        }
//        if ()
    }
}
