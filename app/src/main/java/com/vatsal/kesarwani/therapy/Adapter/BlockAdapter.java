package com.vatsal.kesarwani.therapy.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.ViewHolder> {

    private Context context;
    private List<String> mail;

    public BlockAdapter(Context context, List<String> mail) {
        this.context = context;
        this.mail = mail;
    }

    @NonNull
    @Override
    public BlockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_block, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlockAdapter.ViewHolder holder, int position) {
        final StorageReference sr = FirebaseStorage.getInstance().getReference();
        final String[] name = new String[2];

        FirebaseFirestore.getInstance().collection("User")
                .document(mail.get(position))
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
                                name[0] = Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                                name[1]=document.getId();
                                holder.names.setText(Objects.requireNonNull(map.get(AppConfig.NAME)).toString());
                                if (Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString().length() > 5) {
                                    try {
                                        sr.child(Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString())
                                                .getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Glide.with(context.getApplicationContext())
                                                                .load(uri)
                                                                .into(holder.dp);
                                                    }
                                                });
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (map.get(AppConfig.SEX).equals("Male")) {
                                        Glide.with(context.getApplicationContext())
                                                .load(R.drawable.ic_male)
                                                .into(holder.dp);
                                    } else {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setCancelable(true);
                builder.setTitle("Wanna Unblock " + name[0]);

                builder.setPositiveButton("Unblock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unblock(name[1]);
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                notifyDataSetChanged();
            }
        });
    }

    private void unblock(String mail) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> map2=new HashMap<>();
        map2.put("first",1);
        map2.put("Block",false);

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .document(mail)
                .set(map2);

        db.collection("User")
                .document(mail)
                .collection("Chat")
                .document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()))
                .set(map2);

    }

    @Override
    public int getItemCount() {
        return mail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView names;
        private CircleImageView dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.block_name);
            dp = itemView.findViewById(R.id.block_dp);
        }
    }
}
