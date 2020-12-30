package com.vatsal.kesarwani.therapy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vatsal.kesarwani.therapy.Adapter.MyAdapter;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.Util;

import java.util.ArrayList;
import java.util.Objects;

public class MainScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyAdapter adapter;
    private ConstraintLayout mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<String> lis = new ArrayList<>();

        init();
        tabLayout.addTab(tabLayout.newTab().setText("Post"));
        tabLayout.addTab(tabLayout.newTab().setText("Cure"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel chatModel = snapshot.getValue(MessageModel.class);
                    assert chatModel != null;
                    try {
                        if (chatModel.getReceiver().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()) && !chatModel.isIsseen() && !lis.contains(chatModel.getSender())) {
                            unread++;
                            lis.add(chatModel.getSender());
                            Log.d("MAINSCREEN", "onDataChangeLIST: " + lis);

                        } else {
                            lis.clear();
                        }
                    } catch (Exception e) {
                        Log.d("MAINSCREEN", "onDataChange: " + e);
                    }

                }

                if (unread == 0) {
                    Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Chat");
                } else {
                    Objects.requireNonNull(tabLayout.getTabAt(2)).setText("[" + unread + "]  Chat");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
        mainlayout = findViewById(R.id.mainlayout);
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


        Snackbar.make(mainlayout, "Do you want to exit Heal-O-Chat?", Snackbar.LENGTH_LONG)
                .setAction("Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Util().setOffline();
                        finishAffinity();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .setDuration(5000)
                .show();

    }
}