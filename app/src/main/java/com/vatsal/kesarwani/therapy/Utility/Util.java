package com.vatsal.kesarwani.therapy.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.Model.AppConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Util {
    private DatabaseReference dr;
    private FirebaseAuth mAuth;

    public void setOffline(){
        Map<String,Object> m=new HashMap<>();
        m.put(AppConfig.STATUS,false);

        FirebaseFirestore.getInstance().collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                .update(m);
    }

    public void setOnline(){
        Map<String,Object> m=new HashMap<>();
        m.put(AppConfig.STATUS,true);

        FirebaseFirestore.getInstance().collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                .update(m);
    }

    public void track(Map<String,Object> map){
        dr= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        dr.child("Track")
                .child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()))
                .push()
                .setValue(map);
    }
}
