package edu.cis.ia_project_3;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class recViewHolder extends RecyclerView.ViewHolder
{
    protected TextView carName;
    protected TextView seatsLeft;
    private ConstraintLayout layout;
    protected TextView rating;

    public recViewHolder(@NonNull View itemView)
    {
        super(itemView);
        carName = itemView.findViewById(R.id.carName);
        seatsLeft = itemView.findViewById(R.id.seatsLeft);
        rating = itemView.findViewById(R.id.plateNum);
        this.layout = itemView.findViewById(R.id.parentLayout);

    }

    public ConstraintLayout getLayout()
    {
        return layout;
    }
}
