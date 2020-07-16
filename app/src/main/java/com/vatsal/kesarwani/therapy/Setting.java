package com.vatsal.kesarwani.therapy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Setting extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Switch prof_visib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

        prof_visib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit()
                            .putString(AppConfig.PROFILE_VISIBILITY,"true")
                            .apply();
                }
            }
        });
    }

    private void init() {
        prof_visib=findViewById(R.id.profile_visibility_setting);
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
    }
}