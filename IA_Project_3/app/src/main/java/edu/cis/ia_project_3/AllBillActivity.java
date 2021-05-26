package edu.cis.ia_project_3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllBillActivity extends AppCompatActivity
{
    RecyclerView recviewOne;
    FirebaseFirestore firebaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ArrayList<User> userList;
    ArrayList<Payments> payments;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bill);
        recviewOne = findViewById(R.id.recyclerViewOne);
        mAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseFirestore.getInstance();

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
                                    //currList = v.getPayments();
                                    updateArrayList(currList);

                                    break;
                                }
                            }

                            createRecview();
                        } else
                        {

                        }
                    }
                });
    }

    public void createRecview()
    {


       recViewAdapterOne newAdapterOne = new recViewAdapterOne(payments, this);

        recviewOne.setAdapter(newAdapterOne);
        recviewOne.setLayoutManager(new LinearLayoutManager(this));
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
}