package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private FloatingActionButton editProfile;
    private TextView name,age,sex,contact,about,description;
    private CircleImageView profile;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Editprofile.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void init() {
        editProfile=findViewById(R.id.edit_profile);
        name=findViewById(R.id.fullname);
        age=findViewById(R.id.age);
        sex=findViewById(R.id.sex);
        contact=findViewById(R.id.contact);
        about=findViewById(R.id.about);
        description=findViewById(R.id.description);
        profile=findViewById(R.id.profile_dp);
        db=FirebaseFirestore.getInstance();
    }
}