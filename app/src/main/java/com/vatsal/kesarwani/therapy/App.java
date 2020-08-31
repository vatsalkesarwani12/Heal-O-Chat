package com.vatsal.kesarwani.therapy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Activity.Editprofile;
import com.vatsal.kesarwani.therapy.Activity.MainScreen;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Utility.Util;

public class App extends Application {
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    private static final String TAG = "App123";
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth= FirebaseAuth.getInstance();
        sharedPreferences= getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);

        if (mAuth.getCurrentUser() != null && sharedPreferences.getString(AppConfig.PROFILE_STATE, "com.vatsal.kesarwani.theraphy.PROFILE_STATE").equals("com.vatsal.kesarwani.theraphy.PROFILE_STATE")) {
            Log.d(TAG, "onAppGotoForeground: ");
        } else if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: ");
            AppVisibilityDetector.init(App.this, new AppVisibilityDetector.AppVisibilityCallback() {
                @Override
                public void onAppGotoForeground() {
                    //app is from background to foreground
                    new Util().setOnline();
                }
                @Override
                public void onAppGotoBackground() {
                    //app is from foreground to background
                    new Util().setOffline();
                }
            });
        }
    }


}
