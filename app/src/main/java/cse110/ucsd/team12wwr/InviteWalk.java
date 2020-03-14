package cse110.ucsd.team12wwr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Schedule;
import cse110.ucsd.team12wwr.firebase.ScheduleDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class InviteWalk extends AppCompatActivity {

    TextView selectDate, selectTime, routeName;
    String routeNameText;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    String AM_PM;
    int year, month, day;
    String timeText, dateText;

    private static final String TAG = "InviteWalk";

    SharedPreferences email;
    String userEmail, teamID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_walk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = getSharedPreferences("USER_ID", MODE_PRIVATE);
        userEmail = email.getString("EMAIL_ID", null);

        UserDao userDao = DaoFactory.getUserDao();
        userDao.findUserByID(userEmail,task -> {
            if (task.isSuccessful()) {
                final User[] u = {null};
                for (QueryDocumentSnapshot document : task.getResult()) {
                    u[0] = document.toObject(User.class);
                    Log.i(TAG, "onResume: Found user");
                }
                if (u[0] != null) {
                    teamID = u[0].teamID;
                }
            }
        });

        Intent intent = getIntent();
        routeNameText = intent.getStringExtra("ROUTE_TITLE");

        timeText = "";
        dateText = "";

        routeName = findViewById(R.id.route_title_invite_field);
        routeName.setText(routeNameText);

        selectDate = findViewById(R.id.date_invite_field);
        selectDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

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
                dateText = selectDate.getText().toString();
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
                                    if ( minute < 10 ) {
                                        selectTime.setText(hourOfDay + " : 0" + minute + " " + AM_PM );
                                    } else {
                                        selectTime.setText(hourOfDay + " : " + minute + " " + AM_PM);
                                    }
                                    timeText = selectTime.getText().toString();
                                } else {
                                    AM_PM = "PM";
                                    if ( minute < 10 ) {
                                        selectTime.setText((hourOfDay-12) + " : 0" + minute + " " + AM_PM );
                                    } else {
                                        selectTime.setText((hourOfDay - 12) + " : " + minute + " " + AM_PM);
                                    }
                                    timeText = selectTime.getText().toString();
                                }

                            }
                        }, hour, minute, false);
                timePickerDialog.show();

            }
        });

        Button cancelButton = findViewById(R.id.cancel_invite_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button inviteButton = findViewById(R.id.invite_btn);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( dateText.length() <= 0 && selectTime.length() <= 0 ) {
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter all the information above!!!!!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    Log.d(TAG, "onClick: Toasty shown!");
                    toast.show();
                } else {
                    Log.d(TAG, "onClick: Now inserting the schedule into the database ");
                    ScheduleDao sDao = DaoFactory.getScheduleDao();
                    Schedule schedule = new Schedule();
                    schedule.date = dateText;
                    Log.d(TAG, "onClick: scheduled walk's date is: " + dateText);
                    schedule.proposerUserID = userEmail;
                    Log.d(TAG, "onClick: scheduled walk's proposer is: " + userEmail);
                    schedule.routeName = routeNameText;
                    Log.d(TAG, "onClick: scheduled walk's route name: " + routeNameText);
                    schedule.time = timeText;
                    Log.d(TAG, "onClick: scheduled walk's time is: " + timeText);
                    schedule.teamID = teamID;
                    Log.d(TAG, "onClick: scheduled walk's team ID is: " + teamID);
                    schedule.isScheduled = false;
                    Log.d(TAG, "onClick: isScheduled = false since it's proposed");
                    Map<String, Schedule.Vote> votes = new HashMap<>();

                    UserDao uDao = DaoFactory.getUserDao();
                    uDao.findUsersByTeam(userEmail, task -> {
                        if ( task.isSuccessful() ) {
                            ArrayList<User> userList = new ArrayList<>();
                            for ( QueryDocumentSnapshot document : task.getResult() ) {
                                userList.add(document.toObject(User.class));
                            }

                            for ( User user : userList ) {
                                votes.put(user.userID, Schedule.Vote.ABSTAINED);
                                Log.d(TAG, "onClick: inserted [" + user.userID + "] with the vote: " + Schedule.Vote.ABSTAINED);
                            }
                        }
                    });
                    
                    schedule.userVoteMap = votes;
                    sDao.insertAll(schedule);

                    finish();
                }
            }
        });
    }

}
