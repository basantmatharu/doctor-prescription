package com.scholars.doctor.ui.patient;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.managers.PrescriptionManager;
import com.scholars.doctor.ui.PrescriptionAdapter;

public class PatientHomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PrescriptionAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        recyclerView = (RecyclerView) findViewById(R.id.prescriptionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescriptionAdapter(this);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = prefs.getString("username", "");
        PrescriptionManager.listPrescriptions(new PrescriptionManager.CallBacks() {
            @Override
            public void onGetChild(Object o) {
                Prescription p = (Prescription) o;
                if (username.equals(p.getPatientId())) {
                    adapter.addItem(p);
                }
            }

            @Override
            public void onChildChanged(Object p) {
                Prescription prescription = (Prescription) p;
                adapter.updateItem(prescription);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
