package com.scholars.doctor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.scholars.doctor.notification.PrescriptionUpdateNotification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        FirebaseMessaging messagin = FirebaseMessaging.getInstance();
//        Log.d("hello", remoteMessage.getData().get("ola"));

        PrescriptionUpdateNotification.notify(getApplicationContext(), "asdasd", 33);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(), remoteMessage.getData().get("message"), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
