package com.scholars.doctor.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;
import com.scholars.doctor.model.User;
import com.scholars.doctor.model.managers.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by I311846 on 09-Jun-16.
 */
public class FcmManagerService {

    private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_SERVER_KEY = "AIzaSyDhUoobeXG2i54ezB7rY2GVCUMj_T72pJk";

    public static void sendNewPrescriptionNotification(final Context context, final User user, final Prescription p) {
        SendNotificationTask task = new SendNotificationTask();
        final Map<String, java.io.Serializable> map = new HashMap<String, java.io.Serializable>();
        map.put("for", context.getString(R.string.role_patient));
        map.put("title", "New order");
        map.put("message", p.getTitle());
        map.put("prescriptionId", p.getId());
        map.put("user", user);
        task.execute(map);

        UserManager.getUser("pharma", new UserManager.UserCallBacks() {
            @Override
            public void onSuccess(User pharma) {
                Map<String, java.io.Serializable> pharmaMap = new HashMap<String, java.io.Serializable>();
                pharmaMap.put("for", context.getString(R.string.role_pharmacy));
                pharmaMap.put("title", "New prescription created");
                pharmaMap.put("message", p.getTitle());
                pharmaMap.put("prescriptionId", p.getId());
                pharmaMap.put("user", pharma);
                SendNotificationTask pharmaTask = new SendNotificationTask();
                pharmaTask.execute(pharmaMap);
            }
        });
    }


    public static void sendUpdateNotification(final Context context, final Prescription p) {
        UserManager.getUser(p.getPatientId(), new UserManager.UserCallBacks() {
            @Override
            public void onSuccess(User user) {
                Map<String, java.io.Serializable> map = new HashMap<>();
                map.put("for", context.getString(R.string.role_patient));
                map.put("title", "Prescription Updated");
                map.put("message", p.getTitle());
                map.put("prescriptionId", p.getId());
                map.put("user", user);
                SendNotificationTask task = new SendNotificationTask();
                task.execute(map);
            }
        });
    }


    private static class SendNotificationTask extends AsyncTask<Map, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Map... params) {
            Map props = params[0];

            try {
                URL url = new URL(FCM_ENDPOINT);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "key=" + FCM_SERVER_KEY);
                urlConnection.setUseCaches(false);

                JSONObject requestObject = new JSONObject();
                JSONObject dataObject = new JSONObject();
                dataObject.put("for", props.get("for"));
                dataObject.put("title", props.get("title"));
                dataObject.put("prescriptionId", props.get("prescriptionId"));
                dataObject.put("message", props.get("message"));

                requestObject.put("data", dataObject);
                requestObject.put("to", ((User)props.get("user")).getRegistrationId());

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(requestObject.toString());
                outputStream.flush();
                DataInputStream inputStream = new DataInputStream(urlConnection.getInputStream());
                StringBuilder inputLine = new StringBuilder();
                String tmp;
                while ((tmp = inputStream.readLine()) != null) {
                    inputLine.append(tmp);
                    System.out.println(tmp);
                }
                inputStream.close();


                return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
