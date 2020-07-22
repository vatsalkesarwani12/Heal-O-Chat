package com.vatsal.kesarwani.therapy.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vatsal.kesarwani.therapy.Adapter.CureAdapter;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.Model.CureModel;
import com.vatsal.kesarwani.therapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CureFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CureAdapter adapter;
    private Map<String ,Object> map;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "CureFragment";
    private ArrayList<CureModel> list;
    private SharedPreferences sharedPreferences;

    public CureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CureFragment newInstance(String param1, String param2) {
        CureFragment fragment = new CureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        final View root = inflater.inflate(R.layout.fragment_cure, container, false);
        init(root);


        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                map=document.getData();
                                Log.d(TAG, Objects.requireNonNull(map.get(AppConfig.NAME)).toString());
                                if((boolean)map.get(AppConfig.VISIBLE)) {
                                    if(!Objects.requireNonNull(map.get(AppConfig.NAME)).toString().equals(sharedPreferences.getString(AppConfig.USERNAME,""))) {
                                        list.add(new CureModel(
                                                Objects.requireNonNull(map.get(AppConfig.NAME)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.SEX)).toString(),
                                                document.getId(),
                                                Objects.requireNonNull(map.get(AppConfig.PROFILE_DISPLAY)).toString(),
                                                Objects.requireNonNull(map.get(AppConfig.UID)).toString())
                                        );
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toasty.error(root.getContext(),"Error in fetching data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return root;
    }

    private void init(View root) {
        recyclerView=root.findViewById(R.id.cureRecycler);
        map=new HashMap<>();
        list=new ArrayList<>();
        /*map.put(AppConfig.NAME,"Vatsal");
        map.put(AppConfig.SEX,"Male");
        map.put(AppConfig.AGE,"12");
        map.put(AppConfig.NUMBER,"9696115598");
        map.put(AppConfig.ABOUT,"Hello frands");
        map.put(AppConfig.DESCRIPTION,"Chai pe lo frnds");*/
        adapter=new CureAdapter(root.getContext(),list);
        recyclerView.setAdapter(adapter);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        sharedPreferences=root.getContext().getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
    }

}