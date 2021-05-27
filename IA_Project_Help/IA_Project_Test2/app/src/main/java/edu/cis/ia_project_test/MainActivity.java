package edu.cis.ia_project_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView botView = findViewById(R.id.bottomNav);
        botView.setSelectedItemId(R.id.add);

        botView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddSubActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), allSubActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        Button buttonTimePicker = findViewById(R.id.button_timepicker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        TextView mTextView = findViewById(R.id.textView);
        mTextView.setText(timeText);
    }


}