package com.vatsal.kesarwani.therapy.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.vatsal.kesarwani.therapy.Adapter.MessageAdapter;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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
                refrehStatus();

            }
        });

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshott : snapshot.getChildren()){
                    MessageModel model1=snapshott.getValue(MessageModel.class);
                    list.add(model1);
                }
                chats.scrollToPosition(list.size() - 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
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
                        status= (boolean) map.get("Block");
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
        return super.onOptionsItemSelected(item);
    }

    private void blockUser(){
        //remove user from chat list
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .set(map3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

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
        adapter = new MessageAdapter(this, list);
        chats.setAdapter(adapter);
        map2.put("first",1);
        map2.put("Block",false);
        map3.put("first",1);
        map3.put("Block",true);
        db1=FirebaseDatabase.getInstance();
        dr=db1.getReference();
        status= false;
        v= findViewById(android.R.id.content);
    }
}