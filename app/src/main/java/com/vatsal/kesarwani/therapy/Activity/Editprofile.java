package com.vatsal.kesarwani.therapy.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Map<String ,Object> userData;
    private static final String TAG = "Editprofile";
    private Intent intent;
    private int state=0;
    private String filePath;
    private File file;
    private Uri uri;
    private View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        init();
        userData.put(AppConfig.PROFILE_DISPLAY,"");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check()){
                    return;
                }
                sharedPreferences.edit()
                        .putString(AppConfig.PROFILE_STATE,count+"")
                        .apply();
                if (Objects.requireNonNull(userData.get(AppConfig.PROFILE_DISPLAY)).toString().length()<5) {
                    userData.put(AppConfig.PROFILE_DISPLAY, "");
                }
                userData.put(AppConfig.NAME,sfn);
                userData.put(AppConfig.AGE,Integer.parseInt(sa)+"");
                userData.put(AppConfig.SEX,ss);
                userData.put(AppConfig.NUMBER,sc);
                userData.put(AppConfig.ABOUT,sabout);
                userData.put(AppConfig.DESCRIPTION,sdes);
                userData.put(AppConfig.CAN_CALL,true);
                userData.put(AppConfig.VISIBLE,true);
                userData.put(AppConfig.UID, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                userData.put(AppConfig.STATUS,true);  //true= online

                sharedPreferences.edit()
                        .putString(AppConfig.USERNAME,sfn)
                        .apply();

                syncData();
            }
        });

        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Editprofile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            final Uri fileUri = data.getData();
            profiledp.setImageURI(fileUri);

            assert fileUri != null;
            StorageReference sr=FirebaseStorage.getInstance().getReference();
            sr.child("PROFILES/"+  Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            userData.put(AppConfig.PROFILE_DISPLAY,"PROFILES/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            sharedPreferences.edit()
                                    .putString(AppConfig.PROFILE_DP,"Set")
                                    .apply();
                            Toasty.success(Editprofile.this,"Display Profile Updated",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(rootview, "Unable to Post", Snackbar.LENGTH_LONG).show();
                        }
                    });

            //You can get File object from intent
            file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            filePath = ImagePicker.Companion.getFilePath(data);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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
            if (Objects.equals(intent.getStringExtra(AppConfig.SEX), "Male")) {
                state = 1;
            }
            if (Objects.equals(intent.getStringExtra(AppConfig.SEX), "Female")) {
                state = 2;
            }
            sex.setSelection(state);

            if (Objects.requireNonNull(intent.getStringExtra(AppConfig.POST_IMAGE)).length()>5) {
                StorageReference sr = FirebaseStorage.getInstance().getReference();
                userData.put(AppConfig.PROFILE_DISPLAY,intent.getStringExtra(AppConfig.POST_IMAGE));
                sr.child(Objects.requireNonNull(intent.getStringExtra(AppConfig.POST_IMAGE)))
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(Editprofile.this)
                                        .load(uri)
                                        .into(profiledp);
                            }
                        });
            }
        }
    }

    private void syncData() {
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //update track
                        Map<String,Object> m = new HashMap<>();
                        m.put(AppConfig.TIME,getDate());
                        m.put(AppConfig.TRACKNAME,"Updated Profile");
                        new Util().track(m);

                        Toasty.success(Editprofile.this,"Data Updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Snackbar.make(save, "Error Connecting to Server", Snackbar.LENGTH_LONG)
                                .setAction("Try Again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        syncData();
                                    }
                                })
                                .show();
                    }
                });

    }

    private String getDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate= formatter.format(currentTime);
        return strDate;
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
        rootview = findViewById(R.id.rootview);
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