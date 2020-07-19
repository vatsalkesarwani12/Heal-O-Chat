package com.vatsal.kesarwani.therapy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class CureProfile extends AppCompatActivity {

    private TextView name, age, sex, contact, about, description;
    private String sn, sa, ss, sc, sabout, sdes;
    private CircleImageView profile;
    private ProgressBar progressBar;
    private LinearLayout profileData;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Intent intent;
    private static final String TAG = "CureProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_profile);

        init();
        progressBar.setVisibility(View.VISIBLE);
        profile.setVisibility(View.GONE);
        profileData.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);

        synData();
    }

    private void synData() {
        db.collection("User")
                .document(Objects.requireNonNull(intent.getStringExtra("mail")))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                Map<String, Object> map = document.getData();
                                assert map != null;

                                name.setText(Objects.requireNonNull(map.get(AppConfig.NAME)).toString());

                                age.setText(Objects.requireNonNull(map.get(AppConfig.AGE)).toString());

                                sex.setText(Objects.requireNonNull(map.get(AppConfig.SEX)).toString());

                                contact.setText(Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString());
                                if ((boolean) map.get(AppConfig.CAN_CALL))
                                contact.setVisibility(View.VISIBLE);

                                about.setText(Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString());
                                sabout = Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString();
                                if (sabout.length() == 0) {
                                    about.setVisibility(View.GONE);
                                }

                                description.setText(Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString());
                                sdes = Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString();
                                if (sdes.length() == 0) {
                                    description.setVisibility(View.GONE);
                                }

                                if (Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString().length()<5) {
                                    if (Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Male")) {
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                                    } else if (Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Female")) {
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                                    }
                                }
                                else {
                                    StorageReference sr= FirebaseStorage.getInstance().getReference();
                                    sr.child(Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Glide.with(CureProfile.this)
                                                            .load(uri)
                                                            .into(profile);
                                                }
                                            });
                                }
                                progressBar.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);
                                profileData.setVisibility(View.VISIBLE);
                            }
                        } else
                            Toasty.error(CureProfile.this, "Error Fetching Data " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init() {
        name = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        contact = findViewById(R.id.contact);
        about = findViewById(R.id.about);
        description = findViewById(R.id.description);
        profile = findViewById(R.id.profile_dp);
        progressBar = findViewById(R.id.progressBar);
        profileData = findViewById(R.id.profile_data);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
    }
}