package com.vatsal.kesarwani.therapy.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.vatsal.kesarwani.therapy.Utility.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
            catch (Exception e){
                e.printStackTrace();
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
                intent.putExtra("uid",name[1]);
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

                                    //update track
                                    Map<String,Object> m = new HashMap<>();
                                    m.put(AppConfig.TIME,getDate());
                                    m.put(AppConfig.TRACKNAME,"Liked "+name[0]+" post");
                                    new Util().track(m);
                                }
                            }
                        });
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setMessage("Report "+name[0]+"'s Post");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reportUser(position,list.get(position).getReport(),name[0]);
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });
    }

    private void reportUser(int pos, int rep, final String name){
        Map<String ,Object> map=new HashMap<>();
        map.put(AppConfig.REPORT,rep+1);
        FirebaseFirestore.getInstance().collection("Posts")
                .document(list.get(pos).getId())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            //update track
                            Map<String,Object> m = new HashMap<>();
                            m.put(AppConfig.TIME,getDate());
                            m.put(AppConfig.TRACKNAME,"Reported "+name+" post");
                            new Util().track(m);
                        }
                    }
                });
    }

    private String getDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate= formatter.format(currentTime);
        return strDate;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage ,like ,liked, more ;
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
            more= itemView.findViewById(R.id.more);
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
