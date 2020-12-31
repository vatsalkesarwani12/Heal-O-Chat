package com.vatsal.kesarwani.therapy.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Adapter.BotttomAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.PostModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Profile extends AppCompatActivity {

    private Button editProfile;
    private TextView name,age,sex,contact,about,description;
    private String sn,sa,ss,sc,sabout,sdes,mail;
    private ImageView cover;
    private CircleImageView profile;
    private ProgressBar progressBar;
    private LinearLayout profileData;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "Profile";
    private SharedPreferences sharedPreferences,pref;
    private Intent intent;
    private RecyclerView bottomRecycle;
    private BotttomAdapter adapter;
    private List<PostModel> list;
    private ImageButton back;
    private ImageView expanded;
    private View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        progressBar.setVisibility(View.VISIBLE);
        profile.setVisibility(View.GONE);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded.setVisibility(View.VISIBLE);
                expanded.requestFocus();
                String channel = (pref.getString("DP", ""));
                assert channel != null;
                if (channel.equals("1")){
                    Glide.with(Profile.this)
                            .load(R.drawable.ic_male)
                            .into(expanded);}
                else if (channel.equals("2")){
                    Glide.with(Profile.this)
                            .load(R.drawable.ic_female)
                            .into(expanded);}
                else {
                    Glide.with(Profile.this)
                            .load(channel)
                            .into(expanded);
                }


            }
        });
        expanded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expanded.getVisibility()==View.VISIBLE){
                    expanded.setVisibility(View.INVISIBLE);}



                }
            });



        profileData.setVisibility(View.GONE);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mail= Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()){
                                Map<String,Object> map=document.getData();
                                assert map != null;

                                name.setText(Objects.requireNonNull(map.get(AppConfig.NAME)).toString());
                                sn=Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                                intent.putExtra(AppConfig.NAME,sn);

                                age.setText("Age: "+Objects.requireNonNull(map.get(AppConfig.AGE)).toString());
                                sa=Objects.requireNonNull(map.get(AppConfig.AGE)).toString();
                                intent.putExtra(AppConfig.AGE,sa);

                                sex.setText("Sex: "+Objects.requireNonNull(map.get(AppConfig.SEX)).toString());
                                ss=Objects.requireNonNull(map.get(AppConfig.SEX)).toString();
                                intent.putExtra(AppConfig.SEX,ss);

                                contact.setText("Contact: "+Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString());
                                sc=Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString();
                                intent.putExtra(AppConfig.NUMBER,sc);

                                about.setText(Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString());
                                sabout=Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString();
                                if(sabout.length()==0){
                                    about.setVisibility(View.GONE);
                                }
                                intent.putExtra(AppConfig.ABOUT,sabout);

                                description.setText(Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString());
                                sdes=Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString();
                                if (sdes.length()==0){
                                    description.setVisibility(View.GONE);
                                }
                                intent.putExtra(AppConfig.DESCRIPTION,sdes);

                                if (!sharedPreferences.getString(AppConfig.PROFILE_DP,"com.vatsal.kesarwani.theraphy.PROFILE_DP").equals("com.vatsal.kesarwani.theraphy.PROFILE_DP") &&
                                Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString().length()>5){
                                    StorageReference sr= FirebaseStorage.getInstance().getReference();
                                    sr.child(Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Glide.with(Profile.this)
                                                            .load(uri)
                                                            .into(profile);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("DP", String.valueOf(uri));
                                                    editor.commit();
                                                    editor.apply();

                                                    Glide.with(Profile.this)
                                                            .load(uri)
                                                            .into(cover);
                                                }
                                            });
                                    intent.putExtra(AppConfig.POST_IMAGE, Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString());
                                }
                                else {
                                    if(Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Male")){
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("DP", "1");
                                        editor.commit();
                                        editor.apply();
                                    }
                                    else if (Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Female")){
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("DP", "2");
                                        editor.commit();
                                        editor.apply();
                                    }
                                    intent.putExtra(AppConfig.POST_IMAGE,"");
                                }
                                progressBar.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);
                                profileData.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Snackbar.make(rootview, "Error Fetching Data " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

        postData();
    }

    private void postData(){
        list.clear();
        db.collection("Posts")
                .whereEqualTo("POST_BY", mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String,Object> map=document.getData();
                                long timeinmilli = 0;
                                if(map.containsKey(AppConfig.TIME)){
                                    timeinmilli = Long.parseLong(Objects.requireNonNull(map.get(AppConfig.TIME)).toString());
                                }
                                list.add(new PostModel(Objects.requireNonNull(map.get(AppConfig.POST_IMAGE)).toString(),
                                        Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.LIKES)).toString()),
                                        Objects.requireNonNull(map.get(AppConfig.POST_DESCRIPTION)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.POST_BY)).toString(),
                                        document.getId(),
                                        false,
                                        Objects.requireNonNull(map.get(AppConfig.NAME)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.UID)).toString(),
                                        Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.REPORT)).toString()),
                                        timeinmilli));
                            }
                            Collections.sort(list, new Comparator<PostModel>() {
                                @Override
                                public int compare(PostModel o1, PostModel o2) {
                                    return Long.compare(o2.getTime(), o1.getTime());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void init() {
        rootview = findViewById(R.id.rootview);
        editProfile=findViewById(R.id.edit_profile);
        name=findViewById(R.id.fullname);
        age=findViewById(R.id.age);
        sex=findViewById(R.id.sex);
        cover=findViewById(R.id.profile_cover);
        contact=findViewById(R.id.contact);
        about=findViewById(R.id.about);
        description=findViewById(R.id.description);
        profile=findViewById(R.id.profile_dp);
        progressBar=findViewById(R.id.progressBar);
        profileData=findViewById(R.id.profile_data);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        pref = getSharedPreferences("DP", Context.MODE_PRIVATE);
        intent=new Intent(getApplicationContext(),Editprofile.class);
        bottomRecycle=findViewById(R.id.postRecycler);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        bottomRecycle.setLayoutManager(manager);
        list=new ArrayList<>();
        adapter=new BotttomAdapter(this,list);
        bottomRecycle.setAdapter(adapter);
        back=findViewById(R.id.imBack);
        expanded = findViewById(R.id.expanded_image);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainScreen.class));
    }
}