package com.vatsal.kesarwani.therapy.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.Model.AppConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Util {

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
}
