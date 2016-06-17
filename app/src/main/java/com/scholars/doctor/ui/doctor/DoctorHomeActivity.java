package com.scholars.doctor.ui.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.managers.PrescriptionManager;
import com.scholars.doctor.model.User;
import com.scholars.doctor.model.managers.UserManager;
import com.scholars.doctor.service.FcmManagerService;
import com.scholars.doctor.ui.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DoctorHomeActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PATIENT = 5;

    FloatingActionButton selectPatient;

    Button submitPrescription;

    TextInputEditText usernameText;

    TextInputEditText prescriptionText;

    View rootView;

    private User currentPatient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);


        rootView = findViewById(R.id.container);
        selectPatient = (FloatingActionButton) findViewById(R.id.select_user);
        submitPrescription = (Button) findViewById(R.id.submitPrescriptin);
        usernameText = (TextInputEditText) findViewById(R.id.username);
        prescriptionText = (TextInputEditText) findViewById(R.id.prescription);

        selectPatient.setOnClickListener(this);
        submitPrescription.setOnClickListener(this);

    }

    private void openSelectPatientActivity(View v) {
        Intent intent = new Intent(this, SelectPatientActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, selectPatient, "fab_transition_dest");
        startActivityForResult(intent, REQUEST_CODE_PATIENT, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATIENT
                && resultCode == RESULT_OK) {
            this.currentPatient = (User) data.getSerializableExtra(SelectPatientActivity.RESULT_SELECTED_USER);
            usernameText.setText(this.currentPatient.getUsername());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_user:
                openSelectPatientActivity(v);
                break;
            case R.id.submitPrescriptin:
                submitNewPrescription();
                break;
        }
    }

    private void submitNewPrescription() {
        showProgressDialog(getString(R.string.submitting_prescription));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Prescription p = new Prescription(
                prescriptionText.getText().toString(),
                null,
                usernameText.getText().toString(),
                prefs.getString("username", ""),
                "pharma",
                "PENDING",
                "Prescription: " + format.format(calendar.getTime())
                );

        PrescriptionManager.createPrescription(p, new PrescriptionManager.CallBacks() {
            @Override
            public void onGetChild(Object p) {
                hideProgressDialog();
                FcmManagerService.sendNewPrescriptionNotification(DoctorHomeActivity.this, DoctorHomeActivity.this.currentPatient, (Prescription) p);
                clearFields();
                Snackbar.make(rootView, getString(R.string.presc_submitted), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(Object p) {

            }
        });

    }

    private void clearFields() {
        usernameText.setText("");
        prescriptionText.setText("");
    }

}
