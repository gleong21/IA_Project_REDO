package edu.cis.ia_project_3;

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
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String text = adapterView.getItemAtPosition(i).toString();

        billType = text;

        if(billType.equals("Subscription"))
        {
            Intent intent = new Intent(this, AddSubActivity.class);

            startActivity(intent);
        }
        if(billType.equals("Bills"))
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