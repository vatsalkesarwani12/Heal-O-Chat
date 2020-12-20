package com.vatsal.kesarwani.therapy.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vatsal.kesarwani.therapy.Adapter.ChatAdapter;
import com.vatsal.kesarwani.therapy.Model.ChatModel;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.ViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView chatRecycle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<ChatModel> list;
    private ChatAdapter adapter;
    private Map<String,Object> map;
    private static final String TAG = "ChatFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView warn;
    private ViewDialog dialog;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        dialog = new ViewDialog(getActivity());
        init(root);

        fetchData(root);

        /*swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);    //cause recycling error
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });*/

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        //fetchData();
    }

    private void fetchData(final View root){
        dialog.showDialog();
        list.clear();
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            list.clear();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                map=document.getData();
                                Log.d("IDCollection", document.getId());
                                if(!(boolean)map.get("Block")) {
                                    if(map.containsKey("chats")){
                                        if(map.get("chats").equals(true)){
                                            list.add(new ChatModel(
                                                    document.getId()
                                            ));
                                        }
                                    }else{
                                        list.add(new ChatModel(
                                                document.getId()
                                        ));
                                    }
                                }
                            }
                            //swipeRefreshLayout.setRefreshing(false);
                            if(list.size() == 0){
                                warn.setVisibility(View.VISIBLE);
                            }
                            else {
                                warn.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.hideDialog();
                        }
                        else{
                            dialog.hideDialog();
                            Snackbar.make(root, "Error Fetching Data", Snackbar.LENGTH_LONG)
                                    .setAction("Try Again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            fetchData(root);
                                        }
                                    })
                                    .show();
                        }
                    }
                });
    }

    private void init(View root) {
        chatRecycle=root.findViewById(R.id.chat_recycler);
        mAuth=FirebaseAuth.getInstance();
        map=new HashMap<>();
        db=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        list.clear();
        adapter=new ChatAdapter(getContext(),list);
        chatRecycle.setAdapter(adapter);
        //swipeRefreshLayout=root.findViewById(R.id.refreshChat);
        warn=root.findViewById(R.id.warn_chat);
        if(list.size() == 0){
            warn.setVisibility(View.VISIBLE);
        }
    }
}