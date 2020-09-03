package com.vatsal.kesarwani.therapy.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class CureProfile extends AppCompatActivity {

    private TextView name, age, sex, about, description;
    private ImageButton contact, message;
    private String sn, sa, ss, sc, sabout, sdes, uid,mail;
    private CircleImageView profile;
    private ImageView cover;
    private ProgressBar progressBar;
    private LinearLayout profileData;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Intent intent,intent2;
    private int CODE =1;
    private static final String TAG = "CureProfile";
    private boolean status=false;  /**block status*/
    private RecyclerView bottomRecycle;
    private BotttomAdapter adapter;
    private List<PostModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_profile);

        init();
        progressBar.setVisibility(View.VISIBLE);
        profile.setVisibility(View.GONE);
        profileData.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(CureProfile.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Call "+sn);
                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent2 = new Intent(Intent.ACTION_CALL);
                        intent2.setData(Uri.parse(sc));
                        checkPermission(Manifest.permission.CALL_PHONE,
                                CODE);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialog= builder.create();
                dialog.show();

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(getApplicationContext(), ChatActivity.class);
                intent1.putExtra("mail",intent.getStringExtra("mail"));
                intent1.putExtra("name",intent.getStringExtra("name"));
                intent1.putExtra("uid",intent.getStringExtra("uid"));
                if(!status)
                    startActivity(intent1);
                else
                    Snackbar.make(v,"You cannot message the person",Snackbar.LENGTH_LONG).show();
            }
        });

        synData();
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(CureProfile.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CureProfile.this, new String[] { permission }, requestCode);
        }
        else{
            startActivity(intent2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        if (requestCode == CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CureProfile.this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
            }
            else {
                Toast.makeText(CureProfile.this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
                                sn=Objects.requireNonNull(map.get(AppConfig.NAME)).toString();

                                age.setText("Age: "+Objects.requireNonNull(map.get(AppConfig.AGE)).toString());

                                sex.setText("Sex: "+Objects.requireNonNull(map.get(AppConfig.SEX)).toString());

                                sc="tel:"+Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString();
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

                                                    Glide.with(CureProfile.this)
                                                            .load(uri)
                                                            .into(cover);
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

        //postData
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
                                list.add(new PostModel(Objects.requireNonNull(map.get(AppConfig.POST_IMAGE)).toString(),
                                        Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.LIKES)).toString()),
                                        Objects.requireNonNull(map.get(AppConfig.POST_DESCRIPTION)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.POST_BY)).toString(),
                                        document.getId(),
                                        false,
                                        Objects.requireNonNull(map.get(AppConfig.NAME)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString(),
                                        Objects.requireNonNull(map.get(AppConfig.UID)).toString(),
                                        Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.REPORT)).toString())));
                            }
                            adapter.notifyDataSetChanged();
                        }
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
        message=findViewById(R.id.message_profile);
        cover =findViewById(R.id.profile_cover);
        mail= intent.getStringExtra("mail");
        bottomRecycle=findViewById(R.id.postRecycler);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        bottomRecycle.setLayoutManager(manager);
        list=new ArrayList<>();
        adapter=new BotttomAdapter(this,list);
        bottomRecycle.setAdapter(adapter);

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try{
                            DocumentSnapshot document= task.getResult();
                            assert document != null;
                            Map<String,Object> m=document.getData();
                            assert m != null;
                            status= (boolean) m.get("Block");
                        } catch (Exception e){
                            status=false;
                        }
                    }
                });
    }
}