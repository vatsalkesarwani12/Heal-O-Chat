package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Activity.ChatActivity;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.ChatModel;
import com.vatsal.kesarwani.therapy.Model.ChatModelDetails;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<ChatModelDetails> list;
    private View v;

    public ChatAdapter(Context context, ArrayList<ChatModelDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_offline,parent,false);
                return new OFF_ViewHolder(v);
            case 1:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_online,parent,false);
                return new ON_ViewHolder(v);

            default:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_offline,parent,false);
                return new OFF_ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final String sname = list.get(position).getName();
        final String uid = list.get(position).getUid();
        final String sex = list.get(position).getSex();
        final String dpLink = list.get(position).getDp();

        final String mail = list.get(position).getMail();
        final boolean online = list.get(position).isOnline();

        if(online){
            ((ON_ViewHolder) holder).profname.setText(sname);

            if (dpLink.length() > 1) {

                StorageReference sr= FirebaseStorage.getInstance().getReference();
                try {
                    sr.child(dpLink)
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into(((ON_ViewHolder) holder).dp);
                                }
                            });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
//                try {
//
//                    Glide.with(context.getApplicationContext())
//                            .load(dpLink)
//                            .into(((ON_ViewHolder) holder).dp);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else {
                if (Objects.equals(sex, "Male")) {
                    Glide.with(context.getApplicationContext())
                            .load(R.drawable.ic_male)
                            .into(((ON_ViewHolder) holder).dp);
                } else {
                    Glide.with(context.getApplicationContext())
                            .load(R.drawable.ic_female)
                            .into(((ON_ViewHolder) holder).dp);
                }
            }
        }else{
            ((OFF_ViewHolder) holder).profname.setText(sname);

            if (dpLink.length() > 1) {

                StorageReference sr= FirebaseStorage.getInstance().getReference();
                try {
                    sr.child(dpLink)
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into(((OFF_ViewHolder) holder).dp);
                                }
                            });
                }
                catch (Exception e){
                    e.printStackTrace();
                }

//                try {
//                    Glide.with(context.getApplicationContext())
//                            .load(dpLink)
//                            .into(((OFF_ViewHolder) holder).dp);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else {
                if (Objects.equals(sex, "Male")) {
                    Glide.with(context.getApplicationContext())
                            .load(R.drawable.ic_male)
                            .into(((OFF_ViewHolder) holder).dp);
                } else {
                    Glide.with(context.getApplicationContext())
                            .load(R.drawable.ic_female)
                            .into(((OFF_ViewHolder) holder).dp);
                }
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("name", sname);
                intent.putExtra("uid",uid);
                intent.putExtra("dp",dpLink);
                intent.putExtra("online",online);
                intent.putExtra("sex",sex);
                final boolean[] status = new boolean[1];
                FirebaseFirestore.getInstance().collection("User")
                        .document(mail)
                        .collection("Chat")
                        .document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document= task.getResult();
                                assert document != null;
                                status[0] = (boolean) document.get("Block");
                            }
                        });
                if(!status[0])
                    context.startActivity(intent);
                else
                    Snackbar.make(v,"You cannot message the person",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ON_ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView dp;
        private TextView profname;
        public ON_ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.chat_profile_dp);
            profname=itemView.findViewById(R.id.chat_profile_name);
        }
    }

    public static class OFF_ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView dp;
        private TextView profname;
        public OFF_ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.chat_profile_dp);
            profname=itemView.findViewById(R.id.chat_profile_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isOnline()){
            return 1;
        }else{
            return 0;
        }
    }

}
