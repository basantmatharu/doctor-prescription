package com.scholars.doctor.ui.pharma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.PrescriptionManager;
import com.scholars.doctor.ui.BaseActivity;
import com.scholars.doctor.ui.RecyclerItemClickListener;
import com.scholars.doctor.ui.patient.PrescriptionAdapter;

import java.util.Arrays;

public class PharmaHomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    PrescriptionAdapter adapter;

    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_home);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String currentUsername = prefs.getString("username", "");

        emptyView = findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.pharmaPrescriptionList);
        adapter = new PrescriptionAdapter(this);
        recyclerView.setAdapter(adapter);

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
                startDetailActivity(p);
            }
        }));

//        showProgressDialog(getString(R.string.retrieving_presc));
        PrescriptionManager.listPrescriptions(new PrescriptionManager.CallBacks() {
            @Override
            public void onSuccess(Object p) {
//                hideProgressDialog();
                Prescription prescription = (Prescription) p;
                if (currentUsername.equals(prescription.getPharmacistId())) {
                    adapter.addItem(prescription);
                }
            }
        });

    }

    private void startDetailActivity(Prescription presc) {
        Intent intent = new Intent(this, PrescriptionDetailActivity.class);
        intent.putExtra("prescription", presc);

    }
}
