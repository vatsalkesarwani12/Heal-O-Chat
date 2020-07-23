package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private String[] a ={
            "Tomorrow is another chance",
            "I'm Not Sad Anymore",
            "No darkness last forever. and even there, there are stars",
            "It is never too late to be what you might have been"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        TextView quote = findViewById(R.id.quote);
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        int z=Integer.parseInt(sharedPreferences.getString(AppConfig.SPLASH,0+""));
        quote.setText(a[z]);
        z=z+1;
        if(z>3){
            z=0;
        }
        sharedPreferences.edit()
                .putString(AppConfig.SPLASH,z+"")
                .apply();

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