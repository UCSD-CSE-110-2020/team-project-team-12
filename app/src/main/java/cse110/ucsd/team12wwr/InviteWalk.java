package cse110.ucsd.team12wwr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.logging.Logger;

public class InviteWalk extends AppCompatActivity {

    TextView selectDate, selectTime, routeName;
    String routeNameText;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    private static final String TAG = "InviteWalk";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_walk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        routeNameText = intent.getStringExtra("ROUTE_TITLE");

        routeName = findViewById(R.id.route_title_invite_field);
        routeName.setText(routeNameText);


        Button cancelButton = findViewById(R.id.cancel_invite_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectDate = findViewById(R.id.date_invite_field);
        selectDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                    InviteWalk.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year );

                String date = month + "/" + day + "/" + year;
                selectDate.setText(date);
            }
        };

        selectTime = findViewById(R.id.time_select_field);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                 // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        InviteWalk.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                    selectTime.setText(hourOfDay + " : " + minute + " " + AM_PM );
                                } else {
                                    AM_PM = "PM";
                                    selectTime.setText((hourOfDay-12) + " : " + minute + " " + AM_PM );
                                }

                            }
                        }, hour, minute, false);
                timePickerDialog.show();

            }
        });
    }

}
