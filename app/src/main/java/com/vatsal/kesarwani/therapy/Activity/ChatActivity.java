package com.vatsal.kesarwani.therapy.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vatsal.kesarwani.therapy.Adapter.MessageAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.Util;
import com.vatsal.kesarwani.therapy.Utility.ViewDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChatActivity extends AppCompatActivity {
    private static final int CODE = 125;
    private Intent intent;
    private String mail, name, mssg,uid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, String> map;
    private Map<String, Object> map1;
    private ImageButton send;
    private EditText text;
    private RecyclerView chats;
    private MessageAdapter adapter;
    private ArrayList<MessageModel> list;
    private Map<String,Object> map2=new HashMap<>();
    private Map<String,Object> map3=new HashMap<>();
    private static final String TAG = "ChatActivity";
    private FirebaseDatabase db1;
    private DatabaseReference dr;
    private ChildEventListener listener;
    private ValueEventListener valueEventListener;
    private boolean status;
    private View v;
    private String filePath;
    private File file;
    private Uri uri;
    private Intent intent2;
    private String sc;
    private boolean canCall= false;
    private ViewDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dialog = new ViewDialog(this);
        init();
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    return;
                }
                text.setText("");
                map.put("user", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                map.put("mssg", mssg);

                map.put("img","");
                map.put("time",getTime());
                map.put("date",getDate());

                refrehStatus();

            }
        });

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshott : snapshot.getChildren()){
                    MessageModel model1=snapshott.getValue(MessageModel.class);
                    model1.setNodeKey(snapshott.getKey());
                    list.add(model1);
                }
                chats.scrollToPosition(list.size() - 1);
                adapter.notifyDataSetChanged();
                dialog.hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dialog.showDialog();
        dr.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(uid).addValueEventListener(valueEventListener);

    }

    private void refrehStatus(){
        db.collection("User")
                .document(mail)
                .collection("Chat")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document= task.getResult();
                        assert document != null;
                        Map<String,Object> map =document.getData();
                        assert map != null;
                        try {
                            status = (boolean) map.get("Block");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            status=false;
                        }
                        Log.d(TAG, status+"");
                        if(!status) {
                            addUserToChatList();
                            post();
                        }
                        else{
                            Snackbar.make(v,"You cannot message the user",Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private void addUserToChatList() {
        map2.put("time", Calendar.getInstance().getTimeInMillis());
        //add user to chat list
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .set(map2);
        //add user to chat list
        db.collection("User")
                .document(mail)
                .collection("Chat")
                .document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()))
                .set(map2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.block) {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Wanna Block "+name);
            builder.setCancelable(true);
            builder.setPositiveButton("Block", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    blockUser();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog= builder.create();

            dialog.show();
            item.setVisible(false);
            return true;
        }
        else if (item.getItemId() == android.R.id.home){   //override the back button on the app bar
            onBackPressed();
            return true;
        }
        else if(item.getItemId() == R.id.attach){
            attachPicture();
            return true;
        }
        else if(item.getItemId() == R.id.profile){
            Intent intent = new Intent(getApplicationContext(),CureProfile.class);
            intent.putExtra("mail",mail);
            intent.putExtra("name",name);
            intent.putExtra("uid",uid);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.call){
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("Call");
            builder.setCancelable(true);
            builder.setMessage("Do you want to call "+name);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callUser();
                }
            });
            AlertDialog dialog= builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void callUser(){
        if(canCall) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Call " + name);
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
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            Toast.makeText(this, name+" don't allow call", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(ChatActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[] { permission }, requestCode);
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
                Toast.makeText(ChatActivity.this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
            }
            else {
                Toast.makeText(ChatActivity.this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attachPicture(){
        map.put("user", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        map.put("mssg", "");

        ImagePicker.Companion.with(ChatActivity.this)
                .crop()
                .compress(1024)			                //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }

    private String getDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate= formatter.format(currentTime);
        return strDate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            final Uri fileUri = data.getData();

            assert fileUri != null;
            StorageReference sr= FirebaseStorage.getInstance().getReference();
            sr.child("CHAT/"+Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()+"/"+mail+"/"+fileUri.getLastPathSegment())
                    .putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            map.put("img","CHAT/"+Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()+"/"+mail+"/"+fileUri.getLastPathSegment());
                            Log.d(TAG, "image sent");
                            map.put("time",getTime());
                            map.put("date",getDate());
                            refrehStatus();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this,"Try again later",Toast.LENGTH_SHORT).show();
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

    private String getTime(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        String strDate= formatter.format(currentTime);
        return strDate;
    }

    private void blockUser(){
        //remove user from chat list
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .set(map3)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //update track
                            Map<String,Object> m = new HashMap<>();
                            m.put(AppConfig.TIME,getDate());
                            m.put(AppConfig.TRACKNAME,"Blocked "+name);
                            new Util().track(m);
                        }
                    }
                });

        Toast.makeText(this,name+" blocked", Toast.LENGTH_SHORT).show();
    }

    private void post(){

        dr.child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()))
                .child(uid)
                .push()
                .setValue(map);

        dr.child(uid)
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .push()
                .setValue(map);
    }


    private boolean check() {
        mssg = text.getText().toString();
        return mssg.length() >= 1;
    }

    private void init() {
        intent = getIntent();
        map=new HashMap<>();
        map1=new HashMap<>();
        mail = intent.getStringExtra("mail");
        name = intent.getStringExtra("name");
        uid = intent.getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        send = findViewById(R.id.send);
        text = findViewById(R.id.text_to_send);
        chats = findViewById(R.id.chats);
        chats.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MessageAdapter(this, list, uid, mail);
        chats.setAdapter(adapter);
        map2.put("first",1);
        map2.put("Block",false);
        map2.put("chats", true);
        map3.put("first",1);
        map3.put("Block",true);
        map3.put("chats", true);
        db1=FirebaseDatabase.getInstance();
        dr=db1.getReference();
        status= false;
        v= findViewById(android.R.id.content);

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mail)))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Map<String,Object> mm= Objects.requireNonNull(task.getResult()).getData();
                            assert mm != null;
                            canCall= (boolean) mm.get(AppConfig.CAN_CALL);
                            sc="tel:"+Objects.requireNonNull(mm.get(AppConfig.NUMBER)).toString();
                        }
                    }
                });
    }
}