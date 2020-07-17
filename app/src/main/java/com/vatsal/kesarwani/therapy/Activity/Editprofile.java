package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

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
                Toast.makeText(Editprofile.this, "Data accepted", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit()
                        .putString(AppConfig.PROFILE_STATE,count+"")
                        .apply();
                syncData();
            }
        });

    }

    private void syncData() {

    }

    private boolean check() {
        //sfn,sa,sc,sabout,sdes,ss
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
        if (sc.length()<10){
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
}