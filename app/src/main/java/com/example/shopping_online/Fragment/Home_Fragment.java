package com.example.shopping_online.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_online.Adapter.ProductHindAdapter;
import com.example.shopping_online.DTO.ProductModel;
import com.example.shopping_online.Layout.Acount_Layout;
import com.example.shopping_online.Layout.AllProduct_Layout;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProductHindAdapter adapter;
    private ArrayList<ProductModel> list;
    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();

    private TextView txtAvatarMenu, txt_goto_productall_layout, txtSearchMenu;
    private RecyclerView RecyclerViewProductType, RecyclerViewProductHind;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // Ánh xạ
        firestore = FirebaseFirestore.getInstance();

        txt_goto_productall_layout = view.findViewById(R.id.txt_goto_productall_layout);
        txtAvatarMenu = view.findViewById(R.id.txtAvatarMenu);
        RecyclerViewProductHind = view.findViewById(R.id.RecyclerViewProductHind);
        txtSearchMenu = view.findViewById(R.id.txtSearchMenu);

        txtSearchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllProduct_Layout.class);
                intent.putExtra("value", "1");
                startActivity(intent);
            }
        });

        txt_goto_productall_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AllProduct_Layout.class));
            }
        });

        RecyclerViewProductHind.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        adapter = new ProductHindAdapter(getContext(), list);
        RecyclerViewProductHind.setAdapter(adapter);

        txtAvatarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Acount_Layout.class));
            }
        });

//        loadDataFromFireStore();

    }

    private void loadDataFromFireStore() {
        list.clear();
        firestore.collection("users").document(userId).collection("product").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Duyệt qua tất cả các tài liệu trả về từ Firestore và thêm vào list
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    ProductModel productModel = queryDocumentSnapshot.toObject(ProductModel.class);
                    productModel.setId(queryDocumentSnapshot.getId());
                    list.add(productModel);
                }
                // Cập nhật dữ liệu cho adapter
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Load dữ liệu lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment", "onResume called");
        loadDataFromFireStore();
    }
}