package com.example.bhoomi.pushnotificationexample.service;

import android.content.SharedPreferences;
import android.graphics.Shader;
import android.util.Log;

import com.example.bhoomi.pushnotificationexample.app.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPref(refreshToken);
        sendRegistrationToServer(refreshToken);
    }

    private void sendRegistrationToServer(String refreshToken) {
        Log.e(TAG, "sendRegistrationToServer: " + refreshToken);
    }

    private void storeRegIdInPref(String refreshToken) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("regId",refreshToken);
        editor.commit();
    }
}
