package edu.cis.ia_project_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class TypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    String billType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        Spinner spinner = findViewById(R.id.spinner); //initialize the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type,
                android.R.layout.simple_spinner_item); //retrieve the strings for display from "values"
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        //get the position which the user is on in the spinner
        String text = adapterView.getItemAtPosition(i).toString();

        billType = text;

        //check which type of payment is selected and lead them to the corresponding page
        if (billType.equals("Subscription"))
        {
            Intent intent = new Intent(this, AddSubActivity.class);

            startActivity(intent);
        }
        if (billType.equals("Bills"))
        {
            Intent intent = new Intent(this, AddBillActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
}