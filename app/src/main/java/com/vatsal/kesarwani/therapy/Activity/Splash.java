package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private String[] a ={
            "You matter\nYou are important!\nYour presence on this earth makes a difference!",
            "Storms Don't Last Forever",
            "Be Kinder To Yourself.\nAnd then let your kindness flood the world",
            "Hope make a good breakfast eat plenty of it",
            "Adopt the pace of nature,\nHer secret is Patience",
            "When you focus on the good,\nThe good gets better",
            "The best view comes after the hardest climb",
            "Everyone is struggling mentally daily,\nYou are not alone.\nBetter things lie ahead.\nTake Care and be Kind",
            "The pain you feel today will be the strength you feel tomorrow",
            "You are more precious to the world than you'll ever know",
            "It is often in the darkest skies we see the brightest stars",
            "The calm of your mind is strong enough to handle noise of the world",
            "Tomorrow is another chance",
            "No darkness last forever. and even there, there are stars",
            "Silence teaches us lesson that words never will!!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(500);

        mAuth=FirebaseAuth.getInstance();
        TextView quote = findViewById(R.id.quote);
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        int z=Integer.parseInt(sharedPreferences.getString(AppConfig.SPLASH,0+""));
        quote.setText(a[z]);
        z=z+1;
        if(z>=a.length){
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