package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()==null)
                    startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                else {
                    if (mAuth.getCurrentUser() != null && sharedPreferences.getString(AppConfig.PROFILE_STATE, "com.vatsal.kesarwani.theraphy.PROFILE_STATE").equals("com.vatsal.kesarwani.theraphy.PROFILE_STATE")) {
                        startActivity(new Intent(getApplicationContext(), Editprofile.class));
                    } else if (mAuth.getCurrentUser() != null) {
                        startActivity(new Intent(getApplicationContext(), MainScreen.class));
                    }
                }
            }
        },2000);
    }
}