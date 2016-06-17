package com.scholars.doctor.model.managers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scholars.doctor.model.Prescription;

import java.util.HashMap;


/**
 * Created by I311636 on 6/8/2016.
 */
public class PrescriptionManager {

    private static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference() ;

    public interface CallBacks{
        void onGetChild(Object p);

        void onChildChanged(Object p);
    }

    public static void createPrescription(Prescription p, CallBacks cb ){
        String k = myRef.child("prescription").push().getKey();
        Log.d(k,k);
        myRef.child("prescription").child(k).setValue(p);
        cb.onGetChild(p);
    }


    public static void listPrescriptions(final CallBacks cb){
        Query userQuery = myRef.child("prescription");
        userQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                String prescKey = snapshot.getKey();
                Log.d("asd",snapshot.getValue().toString());

                HashMap<String,String> vals = new HashMap<String, String>();


                Prescription p = snapshot.getValue(Prescription.class);
                p.setId(prescKey);
                /*vals.put("id",prescKey);
                vals.put("status",p.getStatus());
                vals.put("title",p.getTitle());
                vals.put("prescription",p.getMedicines());
                vals.put("patientId",p.getPatientId());
                vals.put("doctorId",p.getDoctorId());
                vals.put("totalAmount",p.getTotalAmount());*/


                cb.onGetChild(p);

                // Log.d("asda",newPresc.getPatientId());

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
                String prescKey = snapshot.getKey();
                Prescription p = snapshot.getValue(Prescription.class);
                cb.onChildChanged(p);
            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }
            public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

    }



    public static void getPrescriptionDetails(String prescriptionId, final CallBacks callback){
        Query userQuery = myRef.child("prescription").child(prescriptionId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Prescription v = snapshot.getValue(Prescription.class);
                callback.onGetChild(v);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static void modifyPrescription(String prescriptionId, final Prescription prescription, final CallBacks callback){
        myRef.child("prescription").child(prescriptionId).setValue(prescription)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (callback != null) {
                        callback.onGetChild(prescription);
                    }
                }
            });

//        callback.onGetChild(prescription);
    }


    public static void modifyPrescriptionColumn(String prescriptionId,final String columnName,final String columnValue, final CallBacks callback){

        myRef.child("prescription").child(prescriptionId).child(columnName).setValue(columnValue);
        getPrescriptionDetails(prescriptionId,callback);
    }
}
