package com.vatsal.kesarwani.therapy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Editprofile extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private CircleImageView profiledp;
    private EditText fullname,age,contact,about,description;
    private Spinner sex;
    private Button save;
    private FirebaseFirestore db;
    private String sfn,sa,sc,sabout,sdes,ss="0";
    private String[] list={"Sex","Male","Female"};
    private int count=4;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private Map<String ,String> userData;
    private static final String TAG = "Editprofile";
    private Intent intent;
    private int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        init();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check()){
                    return;
                }
                //Toast.makeText(Editprofile.this, "Data accepted", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit()
                        .putString(AppConfig.PROFILE_STATE,count+"")
                        .apply();

                userData.put(AppConfig.NAME,sfn);
                userData.put(AppConfig.AGE,Integer.parseInt(sa)+"");
                userData.put(AppConfig.SEX,ss);
                userData.put(AppConfig.NUMBER,sc);
                userData.put(AppConfig.ABOUT,sabout);
                userData.put(AppConfig.DESCRIPTION,sdes);

                syncData();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPreferences.getString(AppConfig.PROFILE_STATE,"com.vatsal.kesarwani.theraphy.PROFILE_STATE").equals("com.vatsal.kesarwani.theraphy.PROFILE_STATE")) {
            fullname.setText(intent.getStringExtra(AppConfig.NAME));
            age.setText(intent.getStringExtra(AppConfig.AGE));
            contact.setText(intent.getStringExtra(AppConfig.NUMBER));
            about.setText(intent.getStringExtra(AppConfig.ABOUT));
            description.setText(intent.getStringExtra(AppConfig.DESCRIPTION));
            if (intent.getStringExtra(AppConfig.SEX).equals("Male")) {
                state = 1;
            }
            if (intent.getStringExtra(AppConfig.SEX).equals("Female")) {
                state = 2;
            }
            sex.setSelection(state);
        }
    }

    private void syncData() {
        db.collection("User")
                .document(mAuth.getCurrentUser().getEmail())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toasty.success(Editprofile.this,"Data Updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toasty.error(Editprofile.this,"Error Connecting to Server",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean check() {
        sfn=fullname.getText().toString();
        sa=age.getText().toString();
        sc=contact.getText().toString();
        sabout=about.getText().toString();
        sdes=description.getText().toString();
        if (sfn.length()<5){
            fullname.setError("Name should be atleast 5 letter");
            return false;
        }
        if (sa.length()==0 || Integer.parseInt(sa)==0){
            age.setError("Enter valid age");
            return false;
        }
        if(!spincheck()){
            Toasty.warning(this,"Select Sex",Toast.LENGTH_LONG).show();
            return false;
        }
        if (sc.length()<10 ){
            contact.setError("Enter valid contact number");
            return false;
        }
        if (sabout.length()>1){
            count++;
        }
        if (sdes.length()>1){
            count++;
        }
        return true;
    }

    private boolean spincheck() {
        return !ss.equals("Sex");
    }

    private void init(){
        profiledp=findViewById(R.id.profile_dp_edit);
        fullname=findViewById(R.id.fullname_edit);
        age=findViewById(R.id.age_edit);
        contact=findViewById(R.id.contact_edit);
        about=findViewById(R.id.about_edit);
        description=findViewById(R.id.description_edit);
        sex=findViewById(R.id.sex_edit);
        sex.setOnItemSelectedListener(this);
        save=findViewById(R.id.save);
        //spinner setup
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();
        userData=new HashMap<>();
        intent=getIntent();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ss=list[position];
        if (sharedPreferences.getString(AppConfig.PROFILE_DP,"com.vatsal.kesarwani.theraphy.PROFILE_DP").equals("com.vatsal.kesarwani.theraphy.PROFILE_DP")){
            if(list[position].equals("Male")){
                profiledp.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
            }
            else if (list[position].equals("Female")){
                profiledp.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sharedPreferences.getString(AppConfig.PROFILE_STATE,"com.vatsal.kesarwani.theraphy.PROFILE_STATE").equals("com.vatsal.kesarwani.theraphy.PROFILE_STATE")){
            finishAffinity();
            finish();
        }
    }
}