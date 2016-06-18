package com.scholars.doctor.ui.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.managers.PrescriptionManager;
import com.scholars.doctor.ui.PrescriptionAdapter;
import com.scholars.doctor.ui.RecyclerItemClickListener;
import com.scholars.doctor.ui.pharma.PrescriptionDetailActivity;

public class PatientHomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PrescriptionAdapter adapter;
    View emptyView;
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
        emptyView = findViewById(R.id.empty_view);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: Start detail activity based on position
                Prescription p = adapter.getItem(position);
//                openedItem = position;
                startDetailActivity(p);
            }
        }));
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

    private void startDetailActivity(Prescription p) {
        Intent intent = new Intent(this, PrescriptionDetailActivity.class);
        intent.putExtra("prescription", p);
        intent.putExtra("editable", false);
        startActivity(intent);
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
