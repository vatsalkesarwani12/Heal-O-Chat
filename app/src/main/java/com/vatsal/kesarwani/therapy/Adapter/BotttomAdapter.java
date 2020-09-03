package com.vatsal.kesarwani.therapy.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.PostModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class BotttomAdapter extends RecyclerView.Adapter<BotttomAdapter.ViewHolder> {

    private Context context;
    private List<PostModel> list;

    public BotttomAdapter(Context context, List<PostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BotttomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BotttomAdapter.ViewHolder holder, final int position) {
        holder.like.setText(list.get(position).getLikes()+"");
        final StorageReference sr= FirebaseStorage.getInstance().getReference();
        try {
            sr.child(list.get(position).getUri())
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context.getApplicationContext())
                                    .load(uri)
                                    .into(holder.postImage);
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog builder =new Dialog(context);
                builder.setCancelable(true);
                builder.setContentView(R.layout.dilaog_layout);

                ((TextView)builder.findViewById(R.id.postBy)).setText(list.get(position).getName());
                try {
                    sr.child(list.get(position).getProfile_display())
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into(((CircleImageView)builder.findViewById(R.id.post_profile_dp)));
                                }
                            });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    sr.child(list.get(position).getUri())
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into(((ImageView)builder.findViewById(R.id.postImage)));
                                }
                            });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                ((TextView)builder.findViewById(R.id.likes)).setText(list.get(position).getLikes()+"");
                ((TextView)builder.findViewById(R.id.postMessage)).setText(list.get(position).getMessage());
                if(context.getClass().getSimpleName() .equals("Profile")) {
                    ((ImageView) builder.findViewById(R.id.more)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setMessage("Post visibility");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("Visible", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Post Visible", Toast.LENGTH_SHORT).show();
                                    if (list.get(position).getReport() < 25) {
                                        FirebaseFirestore.getInstance().collection("Posts")
                                                .document(list.get(position).getId())
                                                .update(AppConfig.VISIBLE, true);
                                    }
                                    else Toast.makeText(context, "Post Reported", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder1.setNegativeButton("Hide", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Post Hidden", Toast.LENGTH_SHORT).show();
                                    FirebaseFirestore.getInstance().collection("Posts")
                                            .document(list.get(position).getId())
                                            .update(AppConfig.VISIBLE, false);
                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                        }
                    });
                }

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView like;
        private ImageView postImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            like=itemView.findViewById(R.id.likes);
            postImage=itemView.findViewById(R.id.post_image);
        }
    }
}
