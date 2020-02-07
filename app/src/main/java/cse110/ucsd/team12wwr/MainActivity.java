package cse110.ucsd.team12wwr;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    SharedPreferences spf;
    int totalHeight;
    final int HEIGHT_FACTOR = 12;
    final double STRIDE_CONVERSION = 0.413;
    final int MILE_FACTOR = 63360;
    double strideLength;
    int steps;

    TextView dist;
    TextView step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        dist = (TextView) findViewById(R.id.num_miles);
        step = (TextView) findViewById(R.id.num_steps);
        Button takeSteps = (Button) findViewById(R.id.button);
        setSupportActionBar(toolbar);
        closeOptionsMenu();

        // Collect the height from the previous page
        spf = getSharedPreferences("height", MODE_PRIVATE);
        int feet = spf.getInt("feet", 0);
        int inches = spf.getInt("inches", 0);

        System.out.println("feet: " + feet + " inches: "  + inches);

        totalHeight = inches + ( HEIGHT_FACTOR * feet );

        //System.out.println("YOU'RE " + totalHeight + " INCHES TALL WOWOWOWOWOWOWWOWOWO");

        strideLength = totalHeight * STRIDE_CONVERSION;

        steps = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        dist.setText(df.format((strideLength / MILE_FACTOR) * steps));

        takeSteps.setOnClickListener((view) -> {
            steps += 100;
            dist.setText(df.format((strideLength / MILE_FACTOR) * steps));
            step.setText(""+steps);

        });

       // System.out.println("YOUR AVERAGE STRIDE LENGTH IS " + df.format(strideLength) + "QIWIWIWIWIQWIWIWHJRUAEISBFIUAEB");
        //System.out.println(df.format(MILE_FACTOR/strideLength));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
