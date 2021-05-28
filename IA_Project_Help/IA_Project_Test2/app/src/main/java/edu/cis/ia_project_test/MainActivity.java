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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
{

    RecyclerView recview;
    FirebaseFirestore firebaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ArrayList<User> userList;
    ArrayList<Payments> payments;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView botView = findViewById(R.id.bottomNav);
        botView.setSelectedItemId(R.id.add);
        mAuth = FirebaseAuth.getInstance();


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
                        startActivity(new Intent(getApplicationContext(), TypeActivity.class));
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

        firebaseRef = FirebaseFirestore.getInstance();
        payments = new ArrayList<>();
        firebaseRef.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ArrayList<Payments> currList = new ArrayList<>();

                                User v = document.toObject(User.class);
//                                System.out.println("HELOO");
//                                System.out.println(v.getUid());
                                if (checkUser(v.getUid()))
                                {
                                    System.out.println(v.getUid());

                                    currList = v.getPayments();
                                    updateArrayList(currList);
                                    System.out.println(currList);

                                    break;
                                }
                            }

                            calc();


                        }


                        else
                        {

                        }
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

    public void updateArrayList(ArrayList<Payments> newOne)
    {
        payments = new ArrayList<>();
        payments = newOne;
    }

    public boolean checkUser(String id)
    {
        if (id.equals(mAuth.getUid()))
        {
            return true;
        }
        return false;
    }

    public void calc()
    {
        int total = 0;

        try
        {
            for(int num =0; num<payments.size(); num++)
            {
                total += Integer.parseInt(payments.get(num).getAmount());
                System.out.println(total);

            }

            TextView text = findViewById(R.id.textView5);
            text.setText("Amount Due: " + String.valueOf(total));
        }
        catch (Exception e)
        {

        }

    }


}