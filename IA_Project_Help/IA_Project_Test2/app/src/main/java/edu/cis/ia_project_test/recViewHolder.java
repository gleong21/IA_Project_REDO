package edu.cis.ia_project_test;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class recViewHolder extends RecyclerView.ViewHolder
{
    protected TextView carName;
    protected TextView seatsLeft;
    private ConstraintLayout layout;
    protected TextView rating;
    private CheckBox check;
    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseFirestore firestoreRef;
    ArrayList<Payments> paymentsList;


    public recViewHolder(@NonNull View itemView)
    {
        super(itemView);
        firestoreRef = FirebaseFirestore.getInstance();
        carName = itemView.findViewById(R.id.carName);
        seatsLeft = itemView.findViewById(R.id.seatsLeft);
        rating = itemView.findViewById(R.id.plateNum);
        check = itemView.findViewById(R.id.checkBox);
        this.layout = itemView.findViewById(R.id.parentLayout);
        mAuth = FirebaseAuth.getInstance();




        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(check.isChecked())
                {
                    String checker = (String) carName.getText();

                    System.out.println(checker + "00000000");
                    for(int num =0; num<paymentsList.size(); num++)
                    {
                        if(paymentsList.get(num).getName().equals(checker))
                        {
                            paymentsList.get(num).setPaid(true);
                        }

                    }

                    firestoreRef.collection("Users").document(mAuth.getUid()).update("payments", paymentsList);
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
                            for(int num =0; num<paymentsList.size(); num++)
                            {
                                String checker = (String) carName.getText();

                                System.out.println(paymentsList.get(num).getName());
                                System.out.println(paymentsList.get(num).isPaid());

                                if(paymentsList.get(num).isPaid() && paymentsList.get(num).getName().equals(checker))
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

    public ConstraintLayout getLayout()
    {
        return layout;
    }

    public void updateArrayList(ArrayList<Payments> newOne)
    {
        if(newOne == null)
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
}
