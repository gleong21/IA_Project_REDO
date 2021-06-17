package edu.cis.ia_project_test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddSubActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseFirestore firestoreRef;
    int year;
    int date;
    int month;
    TextView yearView;
    EditText name;
    EditText amount;
    Boolean x = false;
    String yearOne;
    String monthOne;
    String dateOne;
    ArrayList<Payments> secondArrayPay;
    ArrayList<Payments> paymentsList;
    int curNum = 0;
    String currentDate;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);
        notificationManager = NotificationManagerCompat.from(this);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        firestoreRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView botView = findViewById(R.id.bottomNav);
        botView.setSelectedItemId(R.id.add);
        TextView textView = findViewById(R.id.textView4);


        botView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add:
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(),
                                allSubActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        Button button = findViewById(R.id.chooseDate);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment datePicker = new calendar();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        firestoreRef.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document :
                                    task.getResult())
                            {
                                ArrayList<Payments> currList =
                                        new ArrayList<>();

                                User v = document.toObject(User.class);
                                if (checkUser(v.getUid()))
                                {
                                    System.out.println(v.getUid());

                                    currList = v.getPayments();
                                    updateArrayList(currList);

                                    break;
                                }
                            }


                        }
                        else
                        {

                        }
                    }
                });


        if (monthOne != null)
        {

            yearView.setText(monthOne);
        }

    }


    public void goDate(View v)
    {
        x = true;
        Intent intent = new Intent(this, DateActivity.class);

        startActivity(intent);

    }

    public void gotoBills(View V)
    {
        Intent intent = new Intent(this, AllBillActivity.class);

        startActivity(intent);
    }

    public void writeData(View v)
    {

        if (yearOne == null || name.getText().toString() == null || amount.getText().toString() == null)//If the user has not filled in the fields correctly
        {
            Toast messageToUser = Toast.makeText(this, "Please fill in fields correctly",
                    Toast.LENGTH_LONG);
            messageToUser.show();
        }

        else
        {
            //Create payments object that will be added into the arrayList.  Since not all fields are decided from
            // here, there are listed as null for now
            Payments paymentOne = new Payments("payment" + curNum,
                    name.getText().toString(),
                    amount.getText().toString(), yearOne, monthOne, dateOne,
                    false, null, null, 0
                    , 0, 0);
            curNum++;

            if (paymentsList == null) //If the user has yet to add any Subs, create an empty arrayList
            {
                paymentsList = new ArrayList<>();
                firestoreRef.collection("Users").document(mAuth.getUid()).update("payments",
                        paymentsList);

                Toast messageToUser = Toast.makeText(this, "Success",
                        Toast.LENGTH_LONG);
                messageToUser.show();
                gotoAll();

            }
            else
            {

                paymentsList.add(paymentOne); //If the user already has subs from before, add it on the arrayList and
                // update the field
                firestoreRef.collection("Users").document(mAuth.getUid()).update("payments",
                        paymentsList);
                Toast messageToUser = Toast.makeText(this, "Success",
                        Toast.LENGTH_LONG);
                messageToUser.show();
                gotoAll();

            }

        }


    }

    public boolean checkUser(String id)
    {
        if (id.equals(mAuth.getUid()))
        {
            return true;
        }
        return false;
    }

    public void gotoAll()
    {
        Intent intent = new Intent(this, allSubActivity.class);

        startActivity(intent);
    }

    public void updateArrayList(ArrayList<Payments> newOne)
    {
        if (newOne == null)
        {
            paymentsList = new ArrayList<>();
        }
        else
        {
            paymentsList = newOne;
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        Calendar calOne = Calendar.getInstance();
        calOne.set(Calendar.YEAR, i);
        calOne.set(Calendar.MONTH, i1);
        calOne.set(Calendar.DAY_OF_MONTH, i2);
        String year = String.valueOf(i);
        yearOne = year;
        String month = String.valueOf(i1);
        monthOne = month;
        String date = String.valueOf(i2);
        dateOne = date;
        String currentDay =
                DateFormat.getDateInstance(DateFormat.FULL).format(calOne.getTime());
        TextView textView = findViewById(R.id.textView4);
        textView.setText(currentDay);
        notifTest(calOne);
    }

    public void notifTest(Calendar calendar)
    {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1,
                intent, 0);
        if (calendar.before(Calendar.getInstance()))
        {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);
    }



}