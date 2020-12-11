package com.vatsal.kesarwani.therapy.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Activity.MainScreen;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> list;
    final int YOU = 1;
    final int OTHER = 2;
    final int YOU_IMAGE = 11;
    final int OTHER_IMAGE = 22;
    private View v;
    private String uid, mail;
    private ArrayList<MessageModel> list2 = new ArrayList<>();

    public MessageAdapter(Context context, ArrayList<MessageModel> list, String uid, String mail) {
        this.context = context;
        this.list = list;
        this.uid = uid;
        this.mail = mail;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case OTHER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other, parent, false);
                return new TextViewHolder(v);

            case YOU_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_you_image, parent, false);
                return new ImageViewHolder(v);

            case OTHER_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other_image, parent, false);
                return new ImageViewHolder(v);

            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
                return new TextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (list.get(position).getMssg().length() > 1) {
            ((TextViewHolder) holder).mssg.setText(list.get(position).getMssg().trim() + "        ");  //8 spaces
            ((TextViewHolder) holder).time.setText(list.get(position).getTime());

            ((TextViewHolder) holder).mssg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Select One")
                            .setCancelable(true)
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, "Cancelled!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Delete for Me", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(uid).child(list.get(position).getNodeKey());
                                    dr.removeValue();
                                    Log.e("this", String.valueOf(list.size()));
                                    if (list.size() == 1) {
                                        deleteEmail(1);
                                    }

                                    Toast.makeText(context, "Text Deleted from your chat.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Delete for Everyone", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchChats(0, position);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }
            });
        } else {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_image_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            ((ImageViewHolder) holder).time.setText(list.get(position).getTime());

            StorageReference sr = FirebaseStorage.getInstance().getReference();
            try {
                sr.child(list.get(position).getImg())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into(((ImageViewHolder) holder).image);

                                    Glide.with(context.getApplicationContext())
                                            .load(uri)
                                            .into((ImageView) dialog.findViewById(R.id.img));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Select One")
                            .setCancelable(true)
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, "Cancelled!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Delete for Me", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //removing node from realtime database
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(uid).child(list.get(position).getNodeKey());
                                    dr.removeValue();
                                    //no need to remove image in case of delete for me
                                    if (list.size() == 1) {
                                        deleteEmail(1);
                                    }
                                    Toast.makeText(context, "Image Deleted from your chat.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Delete for Everyone", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchChats(1, position);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
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
            mssg = itemView.findViewById(R.id.mssg);
            time = itemView.findViewById(R.id.time);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);

            this.time = itemView.findViewById(R.id.time);
            this.image = itemView.findViewById(R.id.image);
        }
    }

    private void fetchChats(final int isImage, final int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(uid).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                for (DataSnapshot snapshott : snapshot.getChildren()) {
                    MessageModel model1 = snapshott.getValue(MessageModel.class);
                    model1.setNodeKey(snapshott.getKey());
                    list2.add(model1);
                }

                if (isImage == 1) {
                    //removing node from realtime database from current user
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(uid).child(list.get(position).getNodeKey());
                    dr.removeValue();
                    //removing image from storage
                    StorageReference sr = FirebaseStorage.getInstance().getReference().child(list.get(position).getImg());
                    sr.delete();
                    //matching messages
                    int position2 = 0;
                    for (int i = 0; i < list2.size(); i++) {
                        if (list2.get(i).getImg().equals(list.get(position).getImg())) {
                            position2 = i;
                            break;
                        }
                    }
                    //removing node from realtime database from other user
                    dr = FirebaseDatabase.getInstance().getReference().child(uid).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(list2.get(position2).getNodeKey());
                    dr.removeValue();
                } else {
                    //remove current users message
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(uid).child(list.get(position).getNodeKey());
                    dr.removeValue();
                    //matching messages
                    int position2 = 0;
                    for (int i = 0; i < list2.size(); i++) {
                        if (list2.get(i).getMssg().equals(list.get(position).getMssg())) {
                            position2 = i;
                            break;
                        }
                    }
                    //removing from other users message
                    dr = FirebaseDatabase.getInstance().getReference().child(uid).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(list2.get(position2).getNodeKey());
                    dr.removeValue();
                }
                if (list.size() == 1 && list2.size() == 1) {
                    deleteEmail(2);
                }else if(list.size() == 1){
                    deleteEmail(3);
                }else if(list2.size() == 2){
                    deleteEmail(4);
                }
                Toast.makeText(context, "Deleted for everyone!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteEmail(int i) {
        Log.e("this", String.valueOf(list.size()));
        Log.e("this2", String.valueOf(list2.size()));
        if (i == 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("first",1);
            map.put("Block",false);
            map.put("chats", false);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .collection("Chat")
                    .document(mail)
                    .set(map);
            Intent intent = new Intent(context, MainScreen.class);
            context.startActivity(intent);
        }else if(i == 2){
            Map<String, Object> map = new HashMap<>();
            map.put("first",1);
            map.put("Block",false);
            map.put("chats", false);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //first user
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .collection("Chat")
                    .document(mail)
                    .set(map);
            //second user
            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            db1.collection("User")
                    .document(mail)
                    .collection("Chat")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .set(map);
            Intent intent = new Intent(context, MainScreen.class);
            context.startActivity(intent);
        }else if(i == 3){
            Map<String, Object> map = new HashMap<>();
            map.put("first",1);
            map.put("Block",false);
            map.put("chats", false);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //first user
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .collection("Chat")
                    .document(mail)
                    .set(map);
            Intent intent = new Intent(context, MainScreen.class);
            context.startActivity(intent);
        }else if(i == 4){
            Map<String, Object> map = new HashMap<>();
            map.put("first",1);
            map.put("Block",false);
            map.put("chats", false);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //second user
            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            db1.collection("User")
                    .document(mail)
                    .collection("Chat")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .set(map);
            Intent intent = new Intent(context, MainScreen.class);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (list.get(position).getUser().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())) {
            if (list.get(position).getImg().length() > 1) {
                return YOU_IMAGE;
            } else return YOU;
        } else {
            if (list.get(position).getImg().length() > 1) {
                return OTHER_IMAGE;
            } else return OTHER;
        }
    }
}
