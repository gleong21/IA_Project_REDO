package edu.cis.ia_project_test;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    public boolean testing = false;
    RecyclerView recview;
    FirebaseFirestore firebaseRef;
    ArrayList<User> userList;
    ArrayList<Payments> payments;
    Button location;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView botView = findViewById(R.id.bottomNav);
        botView.setSelectedItemId(R.id.add);
        textView1 = findViewById(R.id.testingOne);
        textView2 = findViewById(R.id.testingTwo);
        textView3 = findViewById(R.id.testingThree);
        textView4 = findViewById(R.id.testingFour);
        textView5 = findViewById(R.id.testingFive);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), allSubActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>()
            {
                @Override
                public void onComplete(@NonNull Task<Location> task)
                {
                    Location location = task.getResult();
                    if (location != null)
                    {
                        System.out.println("YEET");

                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try
                        {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            System.out.println(addresses.get(0).getCountryName());



                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }




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
                                if (checkUser(v.getUid()))
                                {
                                    currList = v.getPayments();
                                    updateArrayList(currList);
                                    break;
                                }
                            }
                            calc();
                            if (!testing)
                            {
                                notifLocation();
                            }
                        }
                        else
                        {

                        }
                    }

                });


    }


    public void updateArrayList(ArrayList<Payments> newOne)
    {
        payments = new ArrayList<>();
        payments = newOne;
    }

    public boolean checkUser(String id)
    {
        //check if the current user matches the id provided in the arrayList
        if (id.equals(mAuth.getUid()))
        {
            return true;
        }
        return false;
    }

    public void calc()
    {
        int total = 0;
        TextView text = findViewById(R.id.textView5);

        try
        {
            for (int num = 0; num < payments.size(); num++)
            {
                //add the amount to the total
                total += Integer.parseInt(payments.get(num).getAmount());
            }


            if(total ==0)
            {
                text.setText("No Payments");
            }
            else
            {
                text.setText("Amount Due: " + String.valueOf(total)); //set the textView to the total
            }

        }
        catch (Exception e)
        {

        }

    }

    public void signout(View v)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), authActivity.class));

    }

    public void notifLocation()
    {
        LocationCallback locationCallBack = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null && !testing)
                {
                    double lat = locationResult.getLastLocation().getLatitude();
                    double longTwo = locationResult.getLastLocation().getLongitude();

                    if (loctest(longTwo, lat))
                    {
                        testing = true;

                    }
                    if (testing)
                    {
                        stopIt();
                    }
//                    System.out.println(lat);
                    System.out.println(longTwo + "testing");
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationRequest test = new LocationRequest();
            test.setInterval(4000);
            test.setFastestInterval(2000);
            test.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(test,
                    locationCallBack, Looper.getMainLooper());


        }
    }

    public boolean loctest(Double longit, Double lat)
    {
        int numtest = 0;
        for (int num = 0; num < payments.size(); num++)
        {
            if (payments.get(num).getLatitude() != null || payments.get(num).getLongitude() != null)
            {
                //Test if the current location is within a 0.001 range of the one stored in the database
                if ((payments.get(num).getLongitude() <= (longit + 0.001) && payments.get(num).getLongitude()
                        >= (longit - 0.001)) && (payments.get(num).getLatitude() <= (lat + 0.001)
                        && payments.get(num).getLatitude() >= (lat - 0.001)))
                {
                    //To make sure the program only sends the
                    if (numtest == 0)
                    {
                        AlarmManager alarmManager =
                                (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(this,
                                AlertReceiver.class);
                        PendingIntent pendingIntent =
                                PendingIntent.getBroadcast(this, 1,
                                        intent, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                0, pendingIntent);
                        numtest++;
                        return true;

                    }


                }
            }
            else
            {

            }

        }
        return false;
    }



    public void stopIt()
    {
        LocationCallback locationCallBack = new LocationCallback();


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationRequest test = new LocationRequest();
            test.setInterval(4000);
            test.setFastestInterval(2000);
            test.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallBack);


        }
    }


}