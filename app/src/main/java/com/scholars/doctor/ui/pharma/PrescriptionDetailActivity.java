package com.scholars.doctor.ui.pharma;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.managers.PrescriptionManager;
import com.scholars.doctor.service.FcmManagerService;
import com.scholars.doctor.ui.BaseActivity;

import java.util.Arrays;

public class PrescriptionDetailActivity extends BaseActivity {

    TextInputEditText amountText, prescriptionText;
    TextView titleText;

    Spinner statusSpinner;
    FloatingActionButton updateButton;
    Prescription currentPrescription;

    public static final int RESULT_CODE_UPDATED = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_detail);

        amountText = (TextInputEditText) findViewById(R.id.amount);
        statusSpinner = (Spinner) findViewById(R.id.status_spiner);
        updateButton = (FloatingActionButton) findViewById(R.id.update_prescription);
        titleText = (TextView) findViewById(R.id.titleText);
        prescriptionText = (TextInputEditText) findViewById(R.id.medicine);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        String[] options = {"PENDING", "SHIPPED", "DELIVERED"};
        spinnerAdapter.addAll(Arrays.asList(options));
        statusSpinner.setAdapter(spinnerAdapter);


        currentPrescription = (Prescription) getIntent().getSerializableExtra("prescription");
        if (currentPrescription != null) {
            amountText.setText(currentPrescription.getTotalAmount());
            prescriptionText.setText(currentPrescription.getMedicines());
            titleText.setText(currentPrescription.getTitle());
            int position = 0;
            switch (currentPrescription.getStatus()) {
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getString(R.string.updating_prescription));
                String status = (String) statusSpinner.getSelectedItem();
                currentPrescription.setTotalAmount(amountText.getText().toString());
                currentPrescription.setStatus(status);

                PrescriptionManager.modifyPrescription(currentPrescription.getId(), currentPrescription, new PrescriptionManager.CallBacks() {
                    @Override
                    public void onGetChild(Object p) {
                        FcmManagerService.sendUpdateNotification(PrescriptionDetailActivity.this, (Prescription) p);
                        hideProgressDialog();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("prescription", (Prescription) p);
                        setResult(RESULT_CODE_UPDATED, resultIntent);
                        Toast.makeText(PrescriptionDetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        finishAfterTransition();
                    }

                    @Override
                    public void onChildChanged(Object p) {

                    }
                });
            }
        });
    }
}
