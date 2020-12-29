package com.vatsal.kesarwani.therapy.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vatsal.kesarwani.therapy.Adapter.ChatAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.ChatModel;
import com.vatsal.kesarwani.therapy.Model.ChatModelDetails;
import com.vatsal.kesarwani.therapy.R;
import com.vatsal.kesarwani.therapy.Utility.ViewDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    private ArrayList<ChatModelDetails> list2;
    private ChatAdapter adapter;
    private Map<String, Object> map;
    private static final String TAG = "ChatFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView warn;
    private ViewDialog dialog;
    private View root;


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
        root = inflater.inflate(R.layout.fragment_chat, container, false);

        init(root);

        //fetchData(root);
        Log.e("this", "oncreate");
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
        Log.e("this", "resume");
        dialog = new ViewDialog(getActivity());
        fetchData(root);
    }

    private void fetchData(final View root) {
        dialog.showDialog();
        list.clear();
        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("Chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                map = document.getData();
                                Log.d("IDCollection", document.getId());
                                if (!(boolean) map.get("Block")) {
                                    long time = 0;
                                    if (map.containsKey("time")) {
                                        time = Long.parseLong(String.valueOf(map.get("time")));
                                    }
                                    if (map.containsKey("chats")) {
                                        if (map.get("chats").equals(true)) {
                                            list.add(new ChatModel(
                                                    document.getId(), time
                                            ));
                                        }
                                    } else {
                                        list.add(new ChatModel(
                                                document.getId(), time
                                        ));
                                    }
                                }
                            }
                            //swipeRefreshLayout.setRefreshing(false);
                            if (list.size() == 0) {
                                warn.setVisibility(View.VISIBLE);
                            } else {
                                warn.setVisibility(View.GONE);
                            }

                            Collections.sort(list, new Comparator<ChatModel>() {
                                @Override
                                public int compare(ChatModel o1, ChatModel o2) {
                                    return Long.compare(o2.getTime(), o1.getTime());
                                }
                            });

                            dialog.hideDialog();

                            list2.clear();
                            fetch2();
                        } else {
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

    private void fetch2(){
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i = 0; i < list.size(); i++){
            tasks.add(db.collection("User").document(list.get(i).getMail()).get());
        }

        Task<List<DocumentSnapshot>> finalTask = Tasks.whenAllSuccess(tasks);
        finalTask.addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                int j = 0;
                for (DocumentSnapshot snapshot : documentSnapshots) {
                    if (snapshot.exists()) {
                        Map<String, Object> map = snapshot.getData();
                        assert map != null;

                        String sname = Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                        String uid = Objects.requireNonNull(map.get(AppConfig.UID)).toString();
                        String sex = Objects.requireNonNull(map.get(AppConfig.SEX).toString());
                        final String[] dpLink = {""};
                        String mail2 = list.get(j).getMail();
                        j++;
                        boolean online = (boolean) map.get(AppConfig.STATUS);

                        StorageReference sr = FirebaseStorage.getInstance().getReference();
                        String sdp = Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString();
//                        if (sdp.length() > 5) {
//                            try {
//                                sr.child(sdp)
//                                        .getDownloadUrl()
//                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                dpLink[0] = String.valueOf(uri);
//                                            }
//                                        });
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            dpLink[0] = "";
//                        }

                        list2.add(new ChatModelDetails(sname, uid, sex, sdp, mail2, online));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void init(View root) {
        chatRecycle = root.findViewById(R.id.chat_recycler);
        mAuth = FirebaseAuth.getInstance();
        map = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        list.clear();
        list2 = new ArrayList<>();
        list2.clear();
        adapter = new ChatAdapter(getContext(), list2);
        chatRecycle.setAdapter(adapter);
        //swipeRefreshLayout=root.findViewById(R.id.refreshChat);
        warn = root.findViewById(R.id.warn_chat);
        if (list.size() == 0) {
            warn.setVisibility(View.VISIBLE);
        }
    }
}