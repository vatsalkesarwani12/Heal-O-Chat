package com.vatsal.kesarwani.therapy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vatsal.kesarwani.therapy.Adapter.MessageAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.CureModel;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChatActivity extends AppCompatActivity {
    private Intent intent;
    private String mail, name, mssg;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, String> map;
    private Map<String, Object> map1;
    private Button send;
    private EditText text;
    private RecyclerView chats;
    private MessageAdapter adapter;
    private ArrayList<MessageModel> list;
    private Map<String,Object> map2=new HashMap<>();
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .collection(mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                map1 = document.getData();
                                list.add(new MessageModel(
                                        Objects.requireNonNull(map1.get("user")).toString(),
                                        Objects.requireNonNull(map1.get("mssg")).toString()
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        db.collection("User")
                .document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()))
                .collection("Chat")
                .document(mail)
                .set(map2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

        db.collection("User")
                .document(mail)
                .collection("Chat")
                .document(mAuth.getCurrentUser().getEmail())
                .set(map2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    return;
                }
                text.setText("");
                map.put("user", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                map.put("mssg", mssg);
                update();
                adapterUpdate();
            }
        });

    }

    private void adapterUpdate() {

        list.clear();
        adapter.notifyDataSetChanged();

        //TODO need modification
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .collection(mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                map1 = document.getData();
                                list.add(new MessageModel(
                                        Objects.requireNonNull(map1.get("user")).toString(),
                                        Objects.requireNonNull(map1.get("mssg")).toString()
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private boolean check() {
        mssg = text.getText().toString();
        return mssg.length() >= 1;
    }

    private void update() {

        list.add(new MessageModel(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(), mssg));
        adapter.notifyDataSetChanged();

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .collection(mail)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toasty.success(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("User")
                .document(mail)
                .collection("Chat")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toasty.success(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void init() {
        intent = getIntent();
        map=new HashMap<>();
        mail = intent.getStringExtra("mail");
        name = intent.getStringExtra("name");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        send = findViewById(R.id.send);
        text = findViewById(R.id.text_to_send);
        chats = findViewById(R.id.chats);
        list = new ArrayList<>();
        adapter = new MessageAdapter(this, list);
        chats.setAdapter(adapter);
        map2.put("first",1);
    }
}