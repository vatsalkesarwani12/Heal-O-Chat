package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vatsal.kesarwani.therapy.Activity.CureProfile;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.CureModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CureAdapter extends RecyclerView.Adapter<CureAdapter.ViewHolder> {
    Context context;
    ArrayList<CureModel> list;

    public CureAdapter(Context context, ArrayList<CureModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cure_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CureAdapter.ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.desc.setText(list.get(position).getDesc());
        if (Objects.equals(list.get(position).getSex(), "Male")) {
            Glide.with(context)
                    .load(R.drawable.ic_male)
                    .into(holder.dp);
        }
        else {
            Glide.with(context)
                    .load(R.drawable.ic_female)
                    .into(holder.dp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, CureProfile.class);
                intent.putExtra("mail",list.get(position).getMail());
                Log.d("ID: ",list.get(position).getMail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,desc;
        private CircleImageView dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            desc=itemView.findViewById(R.id.description);
            dp=itemView.findViewById(R.id.dp);
        }
    }
}
