package edu.cis.ia_project_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class allSubActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_all_sub);
        recview = findViewById(R.id.recviewOne);
        mAuth = FirebaseAuth.getInstance();
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
//                                    System.out.println(currList);

                                    break;
                                }
                            }

                            createRecview();

                        }



                        else
                        {

                        }
                    }
                });
        System.out.println(mAuth.getUid() + "YEEEEEEWEWEEEEEE");
        System.out.println(payments);

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
                        startActivity(new Intent(getApplicationContext(), TypeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        return true;
                }
                return false;
            }
        });
    }

    public void createRecview()
    {

        recViewAdapterOne newAdapter = new recViewAdapterOne(payments, this);

        recview.setAdapter(newAdapter);
        recview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateArrayList(ArrayList<Payments> newOne)
    {
        if(newOne == null)
        {
            payments = new ArrayList<>();
        }
        else
        {
            payments = newOne;
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

}