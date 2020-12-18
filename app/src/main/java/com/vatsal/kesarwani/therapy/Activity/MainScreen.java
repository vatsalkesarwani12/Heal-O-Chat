package com.vatsal.kesarwani.therapy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Adapter.MyAdapter;
import com.vatsal.kesarwani.therapy.Fragment.CureFragment;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.Util;
import com.vatsal.kesarwani.therapy.Utility.ViewDialog;

public class MainScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        tabLayout.addTab(tabLayout.newTab().setText("Post"));
        tabLayout.addTab(tabLayout.newTab().setText("Cure"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        adapter = new MyAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;

                    case 1:
                        viewPager.setCurrentItem(1);
                        //viewPager.getAdapter().notifyDataSetChanged();
                        break;

                    case 2:
                        viewPager.setCurrentItem(2);
                        //viewPager.getAdapter().notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                new Util().setOffline();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                return true;

            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                return true;

            case R.id.setting:
                startActivity(new Intent(getApplicationContext(), Setting.class));
                return true;

            case R.id.aboutus:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}