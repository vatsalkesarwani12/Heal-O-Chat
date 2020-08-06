package com.vatsal.kesarwani.therapy.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddPost extends AppCompatActivity {
    
    private StorageReference sr;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText desc;
    private Button post;
    private ImageView postView;
    private String sdes;
    private String filePath;
    private File file;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        
        init();
        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });
        post.setEnabled(true);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file==null) {
                    Toasty.warning(AddPost.this,"Select a Image to Post",Toast.LENGTH_SHORT).show();
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
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
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
        sdes=desc.getText().toString();
        uri=Uri.fromFile(file);
        final Map<String,Object> map=new HashMap<>();
        map.put(AppConfig.POST_DESCRIPTION,sdes);
        map.put(AppConfig.POST_BY,Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        map.put(AppConfig.LIKES,0);
        sr.child("Images/"+uri.getLastPathSegment())
                .putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        map.put(AppConfig.POST_IMAGE,"Images/"+uri.getLastPathSegment());
                        db.collection("Posts")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toasty.success(AddPost.this,"Post Uploaded",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainScreen.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(AddPost.this,"Unable to post",Toast.LENGTH_SHORT).show();
                                        post.setEnabled(true);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(AddPost.this,"Unable to upload",Toast.LENGTH_SHORT).show();
                        post.setEnabled(true);
                    }
                });

        /*Map<String,Object> map=new HashMap<>();
        map.put(AppConfig.POST_DESCRIPTION,sdes);
        map.put(AppConfig.POST_IMAGE,file);*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        post.setEnabled(true);
    }

    private void init(){
        sr=FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        desc=findViewById(R.id.post_desc);
        post=findViewById(R.id.post);
        postView=findViewById(R.id.image);
    }
}