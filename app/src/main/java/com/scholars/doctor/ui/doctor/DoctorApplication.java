package com.scholars.doctor.ui.doctor;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by I311846 on 06-Jun-16.
 */
public class DoctorApplication extends Application {


    private FirebaseDatabase firebaseDb;

    @Override
    public void onCreate() {
        super.onCreate();
        this.firebaseDb = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getFirebaseDb() {
        return firebaseDb;
    }
}
