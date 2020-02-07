package cse110.ucsd.team12wwr;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.NumberFormat;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        NumberPicker np = findViewById(R.id.numberPicker);

        np.setMinValue(0);
        np.setMaxValue(96);

        //np.setOnValueChangedListener(onValueChangeListener);
        np.setFormatter(formatter);
    }

    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
        @Override
        public String format(int i) {
            return i + " inches";
        }
    };

}
