package com.vatsal.kesarwani.therapy.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Activity.AddPost;
import com.vatsal.kesarwani.therapy.Adapter.PostAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.PostModel;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FloatingActionButton addpost;
    private PostAdapter adapter;
    private RecyclerView postRecycler;
    private ArrayList<PostModel> list;
    private FirebaseAuth mAuth;
    private StorageReference sr;
    private FirebaseFirestore db;
    private Map<String ,Object> map;
    private static final String TAG = "PostFragment";
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_post, container, false);
        init(root);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPost.class));
            }
        });

        fetchData(root);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(root);

            }
        });

        return root;
    }

    private void fetchData(final View root){
        list.clear();
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                map=document.getData();
                                if ((boolean)map.get(AppConfig.VISIBLE)){
                                    Map<String ,Object> mm= new HashMap<>();
                                    mm.put(AppConfig.VISIBLE,false);
                                    if(Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.REPORT)).toString()) >=25 ){
                                        db.collection("Posts")
                                                .document(document.getId())
                                                .update(mm);
                                    }else {
                                        list.add(new PostModel(Objects.requireNonNull(map.get(AppConfig.POST_IMAGE)).toString(),
                                                Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.LIKES)).toString()),
                                                Objects.requireNonNull(map.get(AppConfig.POST_DESCRIPTION)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.POST_BY)).toString(),
                                                document.getId(),
                                                false,
                                                Objects.requireNonNull(map.get(AppConfig.NAME)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.UID)).toString(),
                                                Integer.parseInt(Objects.requireNonNull(map.get(AppConfig.REPORT)).toString())));
                                    }
                                }
                            }
                            Collections.shuffle(list);
                            swipeRefreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toasty.error(root.getContext(),"Error in fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void init(View root){
        addpost=root.findViewById(R.id.add_post);
        list=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        sr=FirebaseStorage.getInstance().getReference();
        db=FirebaseFirestore.getInstance();
        postRecycler=root.findViewById(R.id.post_recycler);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(postRecycler);
        adapter=new PostAdapter(getContext(),list);
        postRecycler.setAdapter(adapter);
        swipeRefreshLayout=root.findViewById(R.id.refreshPost);
    }
}