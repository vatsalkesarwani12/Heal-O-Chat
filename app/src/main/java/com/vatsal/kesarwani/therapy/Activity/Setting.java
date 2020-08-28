package com.vatsal.kesarwani.therapy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import es.dmoral.toasty.Toasty;

public class Setting extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Switch prof_visib,call;
    private TextView delete, deleteAcc, blockedUser;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "Setting";
    private int state=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
                    db.collection("User")
                            .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                            .update(AppConfig.VISIBLE,true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(Setting.this,"Profile Visible", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.warning(Setting.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    sharedPreferences.edit()
                            .putString(AppConfig.PROFILE_VISIBILITY,"false")
                            .apply();
                    db.collection("User")
                            .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                            .update(AppConfig.VISIBLE,false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(Setting.this,"Profile Hidden", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.warning(Setting.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        
        call.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPreferences.edit()
                            .putString(AppConfig.CALL_STATE,"true")
                            .apply();
                    db.collection("User")
                            .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                            .update(AppConfig.CAN_CALL,true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(Setting.this,"Other can call you", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.warning(Setting.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    sharedPreferences.edit()
                            .putString(AppConfig.CALL_STATE,"false")
                            .apply();
                    db.collection("User")
                            .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                            .update(AppConfig.CAN_CALL,false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(Setting.this,"Other cannot call you", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.warning(Setting.this,"Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        blockedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BlockActivity.class));
            }
        });
        
        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=0;
                deleteProfile();
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });*/
    }

    private void deleteAccount(){
        state=1;
        deleteProfile();
    }

    private void deleteProfile(){
        //delete profile dp
        /*db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String ,Object> map= Objects.requireNonNull(task.getResult()).getData();

                        StorageReference sr=FirebaseStorage.getInstance().getReference();
                        assert map != null;
                        sr.child(Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });*/

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Successfully deleted");
                        sharedPreferences.edit()
                                .putString(AppConfig.PROFILE_STATE,"com.vatsal.kesarwani.theraphy.PROFILE_STATE")
                                .putString(AppConfig.PROFILE_VISIBILITY,"com.vatsal.kesarwani.theraphy.PROFILE_VISIBILITY")
                                .putString(AppConfig.CALL_STATE,"com.vatsal.kesarwani.theraphy.CALL_STATE")
                                .putString(AppConfig.PROFILE_DP,"com.vatsal.kesarwani.theraphy.PROFILE_DP")
                                .apply();
                        if(state!=1) {
                            Intent intent=new Intent(getApplicationContext(), Editprofile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        if (state==1) {
                            Objects.requireNonNull(mAuth.getCurrentUser())
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                                sharedPreferences.edit()
                                                        .putString(AppConfig.PROFILE_STATE, "com.vatsal.kesarwani.theraphy.PROFILE_STATE")
                                                        .putString(AppConfig.PROFILE_VISIBILITY, "com.vatsal.kesarwani.theraphy.PROFILE_VISIBILITY")
                                                        .putString(AppConfig.CALL_STATE, "com.vatsal.kesarwani.theraphy.CALL_STATE")
                                                        .putString(AppConfig.PROFILE_DP, "com.vatsal.kesarwani.theraphy.PROFILE_DP")
                                                        .apply();
                                                Intent intent=new Intent(getApplicationContext(), LoginScreen.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Failed to delete the account");
                    }
                });
    }

    private void init() {
        prof_visib=findViewById(R.id.profile_visibility_setting);
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        call=findViewById(R.id.call_setting);
        delete=findViewById(R.id.delete_profile_setting);
        deleteAcc=findViewById(R.id.delete_setting);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        blockedUser= findViewById(R.id.block_user_setting);

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
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
                                prof_visib.setChecked((boolean) map.get(AppConfig.VISIBLE));
                                call.setChecked((boolean) map.get(AppConfig.CAN_CALL));
                            }
                        }
                    }
                });

    }
}