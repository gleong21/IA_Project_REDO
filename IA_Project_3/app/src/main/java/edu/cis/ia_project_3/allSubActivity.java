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

public class allSubActivity extends AppCompatActivity
{

    RecyclerView recview;
    FirebaseFirestore firebaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ArrayList<edu.cis.ia_project_new.User> userList;
    ArrayList<edu.cis.ia_project_new.Payments> payments;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sub);
        recview = findViewById(R.id.recviewOne);
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
                                ArrayList<edu.cis.ia_project_new.Payments> currList = new ArrayList<>();

                                edu.cis.ia_project_new.User v = document.toObject(edu.cis.ia_project_new.User.class);
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

        edu.cis.ia_project_new.recViewAdapterOne newAdapter = new edu.cis.ia_project_new.recViewAdapterOne(payments, this);

        recview.setAdapter(newAdapter);
        recview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateArrayList(ArrayList<edu.cis.ia_project_new.Payments> newOne)
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