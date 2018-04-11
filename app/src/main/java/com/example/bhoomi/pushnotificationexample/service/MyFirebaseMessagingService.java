package com.example.bhoomi.pushnotificationexample.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.bhoomi.pushnotificationexample.activity.MainActivity;
import com.example.bhoomi.pushnotificationexample.app.Config;
import com.example.bhoomi.pushnotificationexample.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null)
        {
            handleNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size()>0)
        {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDataMessage(JSONObject jsonObject) {

        Log.e(TAG, "push json: " + jsonObject.toString());

        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

            String title = jsonObject1.getString("title");
            String message = jsonObject1.getString("message");
            String image = jsonObject1.getString("image");
            String timestamp = jsonObject1.getString("timestamp");
            String payload = jsonObject1.getString("payload");
            boolean isBackgroung = jsonObject1.getBoolean("is_background");

            if (NotificationUtils.isAppIsInBackground(getApplicationContext()))
            {
                Intent intent = new Intent(Config.PUSH_NOTIFICATION);
                intent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

            }else
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("message",message);

                if (TextUtils.isEmpty(image))
                {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, intent);
                }else
                {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, intent, image);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showNotificationMessageWithBigImage(Context applicationContext, String title, String message, String timestamp, Intent intent, String image) {
        notificationUtils = new NotificationUtils(applicationContext);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timestamp,intent,image);
    }

    private void showNotificationMessage(Context applicationContext, String title, String message, String timestamp, Intent intent) {
        notificationUtils = new NotificationUtils(applicationContext);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timestamp,intent);
    }

    private void handleNotification(String body) {

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {
            Intent intent = new Intent(Config.PUSH_NOTIFICATION);
            intent.putExtra("message", body);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
        else
        {
        }
    }
}
