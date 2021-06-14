package edu.cis.ia_project_test;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.StateSet;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class recViewHolder extends RecyclerView.ViewHolder
{
    public static final String DATE_FORMAT = "M/d/yyyy";
    public Double longitude;
    public Double latitude;
    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseFirestore firestoreRef;
    protected TextView name;
    protected TextView amount;
    protected TextView date;
    ArrayList<Payments> paymentsList;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ConstraintLayout layout;
    private CheckBox check;

    public recViewHolder(@NonNull View itemView)
    {
        super(itemView);
        firestoreRef = FirebaseFirestore.getInstance();
        name = itemView.findViewById(R.id.carName);
        amount = itemView.findViewById(R.id.seatsLeft);
        date = itemView.findViewById(R.id.plateNum);
        check = itemView.findViewById(R.id.checkBox);
        this.layout = itemView.findViewById(R.id.parentLayout);
        mAuth = FirebaseAuth.getInstance();
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(itemView.getContext());


        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (check.isChecked())
                {
                    String checker = (String) name.getText();

                    for (int num = 0; num < paymentsList.size(); num++)
                    {
                        if (paymentsList.get(num).getName().equals(checker))
                        {
                            if (ActivityCompat.checkSelfPermission(itemView.getContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            {

                                int finalNum = num;
                                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Location> task)
                                    {
                                        Location location = task.getResult();
                                        if (location != null)
                                        {

                                            Geocoder geocoder =
                                                    new Geocoder(itemView.getContext(), Locale.getDefault());
                                            try
                                            {
                                                List<Address> addresses =
                                                        geocoder.getFromLocation(location.getLatitude(),
                                                                location.getLongitude(), 1);

                                                longitude =
                                                        addresses.get(0).getLongitude();
                                                latitude =
                                                        addresses.get(0).getLatitude();

                                                setLongitude();
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

                            }
                            checkTime();
                            notifTest();
                            notifLocation();

                        }
                    }
                }
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
                                    currList = v.getPayments();
                                    updateArrayList(currList);
                                    break;
                                }
                            }
                            for (int num = 0; num < paymentsList.size(); num++)
                            {
                                String checker = (String) name.getText();
                                if (paymentsList.get(num).isPaid() && paymentsList.get(num).getName().equals(checker))
                                {

                                    check.setChecked(true);
                                }

                            }
                        }


                        else
                        {

                        }
                    }
                });
    }

    public static long getDaysBetweenDates(String start, String end)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT,
                Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try
        {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate,
                    TimeUnit.DAYS);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate,
                                            TimeUnit unit)
    {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public ConstraintLayout getLayout()
    {
        return layout;
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

    public boolean checkUser(String id)
    {
        if (id.equals(mAuth.getUid()))
        {
            return true;
        }
        return false;
    }

    public void setLongitude()
    {
        String checker = (String) name.getText();
        for (int numOne = 0; numOne < paymentsList.size(); numOne++)
        {
            if (paymentsList.get(numOne).getName().equals(checker))
            {
                paymentsList.get(numOne).setLatitude(latitude);
                paymentsList.get(numOne).setLongitude(longitude);
                paymentsList.get(numOne).setPaid(true);


                firestoreRef.collection("Users").document(mAuth.getUid()).update("payments",
                        paymentsList);


            }
        }
    }

    public void checkTime()
    {
        String checker = (String) name.getText();
        for (int numOne = 0; numOne < paymentsList.size(); numOne++)
        {
            if (paymentsList.get(numOne).getName().equals(checker))
            {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                String date = (month + 1) + "/" + day + "/" + year;
                int pay =
                        Integer.parseInt(paymentsList.get(numOne).getDate()) + 1;
                System.out.println("hello");

                String dateOne =
                        (pay + "/" + paymentsList.get(numOne).getMonth() +
                                "/" + paymentsList.get(numOne).getYear());
                int days = paymentsList.get(numOne).getDays();
                int all = (int) getDaysBetweenDates(date, dateOne);
                System.out.println((int) getDaysBetweenDates(date, dateOne));
//                System.out.println(days+all);
//                System.out.println((int) getDaysBetweenDates(date, dateOne));


                paymentsList.get(numOne).setDays(days + all);
                findAverage();

            }
        }

    }

    public void findAverage()
    {
        String checker = (String) name.getText();
        for (int numOne = 0; numOne < paymentsList.size(); numOne++)
        {
            if (paymentsList.get(numOne).getName().equals(checker))
            {
                if (paymentsList.get(numOne).getTimesPaid() == 0)
                {
                    paymentsList.get(numOne).setAverage(paymentsList.get(numOne).getDays());
                    paymentsList.get(numOne).setTimesPaid(1);
                }

                else
                {
                    paymentsList.get(numOne).setTimesPaid(paymentsList.get(numOne).getTimesPaid() + 1);
                    int avg =
                            paymentsList.get(numOne).getDays() / paymentsList.get(numOne).getTimesPaid();
                    System.out.println(avg);
                    paymentsList.get(numOne).setAverage(avg);


                }
            }
        }

    }



    public void notifLocation()
    {
        LocationCallback locationCallBack = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null)
                {
                    double lat = locationResult.getLastLocation().getLatitude();
                    double longTwo = locationResult.getLastLocation().getLongitude();
                    System.out.println("loc" + lat + longTwo);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(itemView.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationRequest test = new LocationRequest();
            test.setInterval(4000);
            test.setFastestInterval(2000);
            test.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(itemView.getContext()).requestLocationUpdates(test,
                    locationCallBack, Looper.getMainLooper());
        }

//        String checker = (String) name.getText();
//        for (int numOne = 0; numOne < paymentsList.size(); numOne++)
//        {
//            if (paymentsList.get(numOne).getName().equals(checker))
//            {
//                if(paymentsList.get(numOne).getLatitude() == latitude && paymentsList.get(numOne).getLongitude() == longitudeOne)
//                {
//                    AlarmManager alarmManager =
//                            (AlarmManager) itemView.getContext().getSystemService(Context.ALARM_SERVICE);
//                    Intent intent = new Intent(itemView.getContext(),
//                            AlertReceiver.class);
//                    PendingIntent pendingIntent =
//                            PendingIntent.getBroadcast(itemView.getContext(), 1,
//                                    intent, 0);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//                }
//
//            }
        //}

    }

    public void notifTest()
    {

        String checker = (String) name.getText();
        for (int numOne = 0; numOne < paymentsList.size(); numOne++)
        {
            if (paymentsList.get(numOne).getName().equals(checker))
            {
                int time = (int) TimeUnit.DAYS.toMillis(paymentsList.get(numOne).getAverage());
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int date = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);
                long todayMillis2 = cal.getTimeInMillis();
                AlarmManager alarmManager =
                        (AlarmManager) itemView.getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(itemView.getContext(),
                        AlertReceiver.class);
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(itemView.getContext(), 1,
                                intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        todayMillis2 + time, pendingIntent);
            }
        }
    }
}
