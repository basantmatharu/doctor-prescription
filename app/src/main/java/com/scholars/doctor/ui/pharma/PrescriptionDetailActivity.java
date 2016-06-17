package com.scholars.doctor.ui.pharma;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;

import java.util.Arrays;

public class PrescriptionDetailActivity extends AppCompatActivity {

    TextInputEditText amountText;
    Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_detail);

        amountText = (TextInputEditText) findViewById(R.id.amount);
        statusSpinner = (Spinner) findViewById(R.id.status_spiner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        String[] options = {"PENDING", "SHIPPED", "DELIVERED"};
        spinnerAdapter.addAll(Arrays.asList(options));
        statusSpinner.setAdapter(spinnerAdapter);


        Prescription incomingPresc = (Prescription) getIntent().getSerializableExtra("prescription");
        if (incomingPresc != null) {
            amountText.setText(incomingPresc.getTotalAmount());
            int position = 0;
            switch (incomingPresc.getTotalAmount()) {
                case "PENDING":
                    position = 0;
                    break;
                case "SHIPPED":
                    position = 1;
                    break;
                case "DELIVERED":
                    position = 2;
                    break;
            }
            statusSpinner.setSelection(position);
        }
    }
}
