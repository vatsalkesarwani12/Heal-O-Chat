package com.vatsal.kesarwani.therapy.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vatsal.kesarwani.therapy.Adapter.TrackAdapter;
import com.vatsal.kesarwani.therapy.Model.TrackModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrackYourActivity extends AppCompatActivity {

    private RecyclerView trackRecycle;
    private TrackAdapter adapter;
    private List<TrackModel> list;
    private DatabaseReference dr;
    private FirebaseAuth mAuth;
    private ValueEventListener valueEventListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_your);

        init();

        refreshData();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

    }

    private void refreshData(){
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshott : snapshot.getChildren()){
                    TrackModel model=snapshott.getValue(TrackModel.class);
                    list.add(model);
                }
                trackRecycle.scrollToPosition(list.size() -1);
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dr.child("Track").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())).addValueEventListener(valueEventListener);
    }

    private void init(){
        trackRecycle=findViewById(R.id.trackRecycle);
        list=new ArrayList<>();
        adapter=new TrackAdapter(this,list);
        trackRecycle.setAdapter(adapter);
        dr= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        swipeRefreshLayout=findViewById(R.id.refreshTrack);
    }
}