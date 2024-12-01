package com.example.shopping_online.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping_online.DTO.SettingModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout txtNameSetting, txtGenderSetting, txtPhoneNumberSetting, txtChangePassWordSetting;
    private TextView txtEmailSetting, txtPhoneNumberUser, txtNameUser, txtGenderUser;

    private ArrayList<SettingModel> list;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private SettingModel settingModel;

    public Setting_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting_Fragment newInstance(String param1, String param2) {
        Setting_Fragment fragment = new Setting_Fragment();
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
        return inflater.inflate(R.layout.fragment_setting_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNameSetting = view.findViewById(R.id.txtNameSetting);
        txtEmailSetting = view.findViewById(R.id.txtEmailSetting);
        txtGenderSetting = view.findViewById(R.id.txtGenderSetting);
        txtPhoneNumberSetting = view.findViewById(R.id.txtPhoneNumberSetting);
        txtChangePassWordSetting = view.findViewById(R.id.txtChangePassWordSetting);
        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtPhoneNumberUser = view.findViewById(R.id.txtPhoneNumberUser);
        txtGenderUser = view.findViewById(R.id.txtGenderUser);

        list = new ArrayList<>();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Acount", getContext().MODE_PRIVATE);
        String nameuser = sharedPreferences.getString("nameacount", "");
        String phoneuser = sharedPreferences.getString("phoneacount", "");

//        txtNameUser.setText(nameuser);
//        txtPhoneNumberUser.setText(phoneuser);
        loadDataFormFireBase();
        setEmailForAcount();

    }

    private void loadDataFormFireBase() {
        firestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                    SettingModel settingModelAdd = queryDocumentSnapshot.toObject(SettingModel.class);
                    settingModelAdd.setId(queryDocumentSnapshot.getId());
                    list.add(settingModelAdd);
                }
                if(!list.isEmpty()){
                    settingModel = list.get(0);
                    updateUI();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Load lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI() {
        txtNameUser.setText(settingModel.getNameuser());
        txtPhoneNumberUser.setText(settingModel.getPhonenumber());
        txtEmailSetting.setText(settingModel.getUsername());

        txtGenderUser.setText(settingModel.isGender() ? "Nữ" : "Nam");
    }

    private void setEmailForAcount() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
//        txtEmailSetting.setText(username);
    }
}