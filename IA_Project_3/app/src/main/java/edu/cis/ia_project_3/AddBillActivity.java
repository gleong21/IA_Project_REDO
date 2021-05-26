package edu.cis.ia_project_3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class AddBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        Button button = findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment datePicker = new calendar();
                datePicker.show(getSupportFragmentManager(), "date pickder");

            }
        });
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        Calendar calOne = Calendar.getInstance();
        calOne.set(Calendar.YEAR, i);
        calOne.set(Calendar.MONTH, i1);
        calOne.set(Calendar.DAY_OF_MONTH, i2);
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i2);


        String currentDay = DateFormat.getDateInstance(DateFormat.FULL).format(calOne.getTime());

        TextView textView = findViewById(R.id.textView3);
        textView.setText(currentDay);
    }
}