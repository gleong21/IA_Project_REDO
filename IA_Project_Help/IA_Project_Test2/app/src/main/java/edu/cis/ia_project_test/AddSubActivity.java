package edu.cis.ia_project_test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import java.util.ArrayList;
import java.util.Calendar;

public class AddSubActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

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
    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseFirestore firestoreRef;
    ArrayList<Payments> secondArrayPay;
    ArrayList<Payments> paymentsList;


    int curNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        firestoreRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), allSubActivity.class));
                        overridePendingTransition(0,0);
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
//                                    System.out.println(currList);

                                    break;
                                }
                            }



                        }



                        else
                        {

                        }
                    }
                });








        if(monthOne != null)
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
        if(yearOne == null)
        {
            Toast messageToUser = Toast.makeText(this, "Please choose a date",
                Toast.LENGTH_LONG);
            messageToUser.show();
        }

        else
        {
            Payments paymentOne = new Payments("payment" + curNum, name.getText().toString(), amount.getText().toString(), yearOne, monthOne, dateOne);
            curNum++;

            paymentsList.add(paymentOne);
            firestoreRef.collection("Users").document(mAuth.getUid()).update("payments", paymentsList);
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

    public void gotoAll(View v)
    {
        Intent intent = new Intent(this, allSubActivity.class);

        startActivity(intent);
    }

    public void updateArrayList(ArrayList<Payments> newOne)
    {
        paymentsList = new ArrayList<>();
        paymentsList = newOne;
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
        String currentDay = DateFormat.getDateInstance(DateFormat.FULL).format(calOne.getTime());
        TextView textView = findViewById(R.id.textView4);
        textView.setText(currentDay);
    }

    public void notifTest(Calendar calendar)
    {
        AlarmManager alarm = new
    }
}