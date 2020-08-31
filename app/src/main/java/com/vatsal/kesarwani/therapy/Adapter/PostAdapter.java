package com.vatsal.kesarwani.therapy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Activity.CureProfile;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.PostModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    ArrayList<PostModel> list;
    final int LIKE = 1;
    final int DISLIKE = 0;

    public PostAdapter(Context context, ArrayList<PostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DISLIKE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list, parent, false);
            return new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_like, parent, false);
            return new ViewHolder(v);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.message.setText(list.get(position).getMessage());
        final int[] x = {list.get(position).getLikes()};
        holder.likes.setText(x[0] +" Likes");
        final StorageReference sr= FirebaseStorage.getInstance().getReference();
        final String[] name = new String[2];
        name[0]=list.get(position).getName();
        name[1]= list.get(position).getUid();
        holder.by.setText(name[0]);

        if(!list.get(position).getProfile_display().isEmpty()) {
            try {
                sr.child(list.get(position).getProfile_display())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context.getApplicationContext())
                                        .load(uri)
                                        .into(holder.post_profile_dp);
                            }
                        });
            }
            finally {

            }
        }
        else if(list.get(position).getProfile_display().length()<5){
            holder.post_profile_dp.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male));
        }

        sr.child(list.get(position).getUri())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context.getApplicationContext()).load(uri)
                                .into(holder.postImage);
                    }
                });

        holder.by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, CureProfile.class);
                intent.putExtra("mail",list.get(position).getBy());
                intent.putExtra("name",name[0]);
                intent.putExtra("uid",name[1]); //todo when user delete account remember to set visible to false
                context.startActivity(intent);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x[0]++;

                FirebaseFirestore.getInstance().collection("Posts")
                        .document(list.get(position).getId())
                        .update(AppConfig.LIKES,x[0])
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    holder.likes.setText(x[0] +" Likes");
                                    Log.d("ClickRecycler",list.get(position).getBy());
                                    list.get(position).setClicked(true);
                                    list.get(position).setLikes(x[0]);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage ,like ,liked ;
        private TextView by,message,likes;
        private CircleImageView post_profile_dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage=itemView.findViewById(R.id.postImage);
            likes=itemView.findViewById(R.id.likes);
            by=itemView.findViewById(R.id.postBy);
            message=itemView.findViewById(R.id.postMessage);
            like=itemView.findViewById(R.id.like);
            liked=itemView.findViewById(R.id.liked);
            post_profile_dp=itemView.findViewById(R.id.post_profile_dp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isClicked()){
            return LIKE;
        }
        else return DISLIKE;
    }
}
