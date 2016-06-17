package com.scholars.doctor.service;

import android.os.AsyncTask;

import com.scholars.doctor.model.PrescriptionManager;
import com.scholars.doctor.model.User;
import com.scholars.doctor.model.UserManager;
import com.scholars.doctor.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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

    public static void sendNewPrescriptionNotification(BaseActivity activity, User user) {
        SendNotificationTask task = new SendNotificationTask();
        Map map = new HashMap();
        map.put("user", user);
        map.put("message", "hello");
        task.execute(map);

        UserManager.getUser("pharma", new UserManager.UserCallBacks() {
            @Override
            public void onSuccess(User pharma) {
                Map pharmaMap = new HashMap();
                pharmaMap.put("user", pharma);
                pharmaMap.put("message", "adssad");
                SendNotificationTask pharmaTask = new SendNotificationTask();
                pharmaTask.execute(pharmaMap);
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
                dataObject.put("message", (String)props.get("message"));

                requestObject.put("data", dataObject);
                requestObject.put("to", ((User)props.get("user")).getRegistrationId());

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(requestObject.toString());
                outputStream.flush();
                DataInputStream inputStream = new DataInputStream(urlConnection.getInputStream());
                StringBuffer inputLine = new StringBuffer();
                String tmp;
                while ((tmp = inputStream.readLine()) != null) {
                    inputLine.append(tmp);
                    System.out.println(tmp);
                }
                inputStream.close();


                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                }
                return false;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
