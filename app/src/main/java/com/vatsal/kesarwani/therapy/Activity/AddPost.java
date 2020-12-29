package com.vatsal.kesarwani.therapy.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import es.dmoral.toasty.Toasty;

public class AddPost extends AppCompatActivity {

    private StorageReference sr;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputEditText desc;
    private Button post;
    private ImageView postView;
    private String sdes;
    private String filePath;
    private File file;
    private Uri uri;
    private String name, uid, pf_display;
    private SharedPreferences sharedPreferences;
    private Switch postLater;
    private TextView info;
    private View rootview;
    private boolean visibility_post = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        init();

        postLater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    visibility_post = false;
                    info.setVisibility(View.VISIBLE);
                }else{
                    visibility_post = true;
                    info.setVisibility(View.GONE);
                }
            }
        });

        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });
        post.setEnabled(true);

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
                                name = Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                                pf_display = Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString();
                                uid = Objects.requireNonNull(map.get(AppConfig.UID)).toString();
                            }
                        } else {
                            Snackbar.make(rootview, "Error Fetching Data " + task.getException(), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file == null) {
                    Snackbar.make(post, "Select a Image to Post", Snackbar.LENGTH_LONG).show();
                    return;
                }
                post.setEnabled(false);
                dataUpload();
            }
        });
    }

    private void imagePicker() {
        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddPost.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
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
            Uri fileUri = data.getData();
            postView.setImageURI(fileUri);

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

    private void dataUpload() {
        sdes = desc.getText().toString();
        uri = Uri.fromFile(file);
        final Map<String, Object> map = new HashMap<>();
        map.put(AppConfig.POST_DESCRIPTION, sdes);
        map.put(AppConfig.POST_BY, Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        map.put(AppConfig.LIKES, 0);
        map.put(AppConfig.VISIBLE, visibility_post);
        map.put(AppConfig.NAME, name);
        map.put(AppConfig.UID, uid);
        map.put(AppConfig.REPORT, 0);
        map.put(AppConfig.PROFILE_DISPLAY, pf_display);
        map.put(AppConfig.TIME, Calendar.getInstance().getTimeInMillis());
        sr.child("Images/" + name + "/" + uri.getLastPathSegment())
                .putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        map.put(AppConfig.POST_IMAGE, "Images/" + name + "/" + uri.getLastPathSegment());
                        db.collection("Posts")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //update track
                                        Map<String, Object> m = new HashMap<>();
                                        m.put(AppConfig.TIME, getDate());
                                        m.put(AppConfig.TRACKNAME, "You Added post");
                                        new Util().track(m);

                                        Toasty.success(AddPost.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainScreen.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(post, "Unable to post", Snackbar.LENGTH_LONG)
                                                .setAction("Try Again", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dataUpload();
                                                    }
                                                })
                                                .show();
                                        post.setEnabled(true);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(post, "Unable to upload", Snackbar.LENGTH_LONG)
                                .setAction("Try Again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dataUpload();
                                    }
                                })
                                .show();
                        post.setEnabled(true);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        post.setEnabled(true);
    }

    private String getDate() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = formatter.format(currentTime);
        return strDate;
    }

    private void init() {
        rootview = findViewById(R.id.rootview);
        sr = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        desc = findViewById(R.id.post_desc);
        post = findViewById(R.id.post);
        postView = findViewById(R.id.image);
        sharedPreferences = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        postLater = findViewById(R.id.postLater);
        info = findViewById(R.id.info);
    }
}