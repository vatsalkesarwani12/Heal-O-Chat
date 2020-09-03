package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vatsal.kesarwani.therapy.Model.TrackModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context context;
    private List<TrackModel> list;

    public TrackAdapter(Context context, List<TrackModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {
        holder.time.setText(list.get(position).getTime());
        holder.track.setText(list.get(position).getTrackName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time,track;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            track=itemView.findViewById(R.id.task_name);
        }
    }
}
