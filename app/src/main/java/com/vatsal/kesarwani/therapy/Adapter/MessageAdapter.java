package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.vatsal.kesarwani.therapy.Model.MessageModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    ArrayList<MessageModel> list;
    final int YOU=1;
    final int OTHER =2;

    public MessageAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == YOU) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
            return new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_other, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mssg.setText(list.get(position).getMssg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mssg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mssg=itemView.findViewById(R.id.mssg);

        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        if(list.get(position).getUser().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())){
            return YOU;
        }
        else return OTHER;
    }
}
