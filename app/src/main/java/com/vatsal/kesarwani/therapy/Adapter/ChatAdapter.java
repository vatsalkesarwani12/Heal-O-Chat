package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Activity.ChatActivity;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.ChatModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> list;

    public ChatAdapter(Context context, ArrayList<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, final int position) {

        final String[] sname = new String[1];
        final String[] sdp = new String[1];
        final String[] uid = new String[1];

        //holder.profname.setText(list.get(position).getMail());

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("User")
                .document(list.get(position).getMail())
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
                                sname[0] = Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                                sdp[0] = Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString();
                                uid[0] = Objects.requireNonNull(map.get(AppConfig.UID)).toString();
                                holder.profname.setText(sname[0]);

                                StorageReference sr= FirebaseStorage.getInstance().getReference();
                                if (sdp[0].length()>5) {
                                    try {
                                        sr.child(sdp[0])
                                                .getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Glide.with(context.getApplicationContext())
                                                                .load(uri)
                                                                .into(holder.dp);
                                                    }
                                                });
                                    }
                                    finally {

                                    }
                                }
                                else
                                {
                                    if (map.get(AppConfig.SEX).equals("Male")){
                                        Glide.with(context.getApplicationContext())
                                                .load(R.drawable.ic_male)
                                                .into(holder.dp);
                                    }
                                    else
                                    {
                                        Glide.with(context.getApplicationContext())
                                                .load(R.drawable.ic_female)
                                                .into(holder.dp);
                                    }
                                }
                            }
                        }
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("mail",list.get(position).getMail());
                intent.putExtra("name", sname[0]);
                intent.putExtra("uid",uid[0]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView dp;
        private TextView profname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.chat_profile_dp);
            profname=itemView.findViewById(R.id.chat_profile_name);
        }
    }
}
