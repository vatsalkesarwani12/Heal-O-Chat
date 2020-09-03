package com.vatsal.kesarwani.therapy.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> list;
    final int YOU=1;
    final int OTHER =2;
    final int YOU_IMAGE=11;
    final int OTHER_IMAGE= 22;
    private View v;

    public MessageAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*if (viewType == YOU) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
            return new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other, parent, false);
            return new ViewHolder(v);
        }*/

        switch (viewType){
            case OTHER :
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other, parent, false);
                return new TextViewHolder(v);

            case YOU_IMAGE:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_you_image,parent,false);
                return new ImageViewHolder(v);

            case OTHER_IMAGE:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other_image, parent,false);
                return new ImageViewHolder(v);

            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
                return new TextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (list.get(position).getMssg().length() >1) {
            ((TextViewHolder) holder).mssg.setText(list.get(position).getMssg() + "        ");  //8 spaces
            ((TextViewHolder) holder).time.setText(list.get(position).getTime());
        }
        else {
            final Dialog dialog =new Dialog(context);
            dialog.setContentView(R.layout.dialog_image_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            ((ImageViewHolder) holder).time.setText(list.get(position).getTime());

            StorageReference sr= FirebaseStorage.getInstance().getReference();
            try {
                sr.child(list.get(position).getImg())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context.getApplicationContext())
                                        .load(uri)
                                        .into(((ImageViewHolder)holder).image);

                                Glide.with(context.getApplicationContext())
                                        .load(uri)
                                        .into((ImageView)dialog.findViewById(R.id.img));
                            }
                        });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView mssg;
        private TextView time;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            mssg=itemView.findViewById(R.id.mssg);
            time= itemView.findViewById(R.id.time);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);

            this.time =  itemView.findViewById(R.id.time);
            this.image =  itemView.findViewById(R.id.image);
        }
    }


    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        if(list.get(position).getUser().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())){
            if(list.get(position).getImg().length() > 1){
                return YOU_IMAGE;
            }
            else return YOU;
        }
        else{
            if (list.get(position).getImg().length() > 1){
                return OTHER_IMAGE;
            }
            else return OTHER;
        }
    }
}
