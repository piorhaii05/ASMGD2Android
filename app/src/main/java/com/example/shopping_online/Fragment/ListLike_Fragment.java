package com.example.shopping_online.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_online.Adapter.ProductListLikeAdapter;
import com.example.shopping_online.DTO.ListLikeModel;
import com.example.shopping_online.Layout.Cart_Layout;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListLike_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListLike_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView RecyclerViewProductListLike;

    private ArrayList<ListLikeModel> list;
    private ProductListLikeAdapter adapter;
    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView cart_from_list_like;
    String userId = firebaseAuth.getCurrentUser().getUid();


    public ListLike_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListLike_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListLike_Fragment newInstance(String param1, String param2) {
        ListLike_Fragment fragment = new ListLike_Fragment();
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
        return inflater.inflate(R.layout.fragment_list_like_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        firestore = FirebaseFirestore.getInstance();

        RecyclerViewProductListLike = view.findViewById(R.id.RecyclerViewProductListLike);
        cart_from_list_like = view.findViewById(R.id.cart_from_list_like);

        cart_from_list_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Cart_Layout.class));
            }
        });

        RecyclerViewProductListLike.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new ProductListLikeAdapter(getContext(), list);
        RecyclerViewProductListLike.setAdapter(adapter);

        loadDataFromFireStore();

    }

    private void loadDataFromFireStore() {
        firestore.collection("users").document(userId).collection("listlike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        ListLikeModel listLikeModel = queryDocumentSnapshot.toObject(ListLikeModel.class);
                        listLikeModel.setId(queryDocumentSnapshot.getId());
                        list.add(listLikeModel);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Load dữ liệu lỗi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromFireStore();
    }
}