package edu.cis.ia_project_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class recViewAdapterOne extends RecyclerView.Adapter<recViewHolder>
{
    public ArrayList<Payments> listingData = new ArrayList<Payments>();
    private Context context;

    /**
     * This is a constructor method that allows the user to enter the data required for the recView
     * to display.
     *
     * @param data    ArrayList of all of the vehicle information which has been read from the
     *                database
     * @param context The context is simply the screen which the app is on, enables clicking on the
     *                recycler view
     */
    public recViewAdapterOne(ArrayList data, Context context)
    {
        listingData = data;
        this.context = context;
    }

    /**
     * This method creates the holder within the recycler view.
     *
     * @param parent the parent is the screen
     * @return the holder which has been created.
     */
    @NonNull
    @Override
    public recViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recviewholder,
                parent, false);

        recViewHolder holder = new recViewHolder(myView);

        return holder;
    }

    /**
     * This method sets the information within each holder.  This is done by accessing the arrayList
     * of listing information from the constructor. Within this method is the OnClick method.  This
     * method checks if and which cell has been clicked. When clicked, information about each
     * category is saved and can be accessed in the next intent, which is created.
     *
     * @param holder   the holder in the recView, which is from the class marketHolder
     * @param position position within the listingsData arrayList
     */
    @Override
    public void onBindViewHolder(@NonNull recViewHolder holder, final int position)
    {

        holder.name.setText(listingData.get(position).getName());
        holder.amount.setText(listingData.get(position).getAmount());
        holder.date.setText("Date:" + (Integer.parseInt(listingData.get(position).getDate()) + 1) + "/" +
                listingData.get(position).getMonth() + "/" + listingData.get(position).getYear());
        holder.getLayout().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, BillInfoActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return listingData.size();
    }
}

