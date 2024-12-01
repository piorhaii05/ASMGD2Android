package com.example.shopping_online.Layout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_online.Adapter.CardAdapter;
import com.example.shopping_online.Adapter.UseCardAdapter;
import com.example.shopping_online.DTO.CardModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UseCard_Layout extends AppCompatActivity {
    private Toolbar toolbarUseCardLayout;
    private RecyclerView RecyclerViewUseCardLayout;

    private ArrayList<CardModel> list;
    private UseCardAdapter adapter;

    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_use_card_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();

        toolbarUseCardLayout = findViewById(R.id.toolbarUseCardLayout);
        RecyclerViewUseCardLayout = findViewById(R.id.RecyclerViewUseCardLayout);

        list = new ArrayList<>();
        RecyclerViewUseCardLayout.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UseCardAdapter(this, list);
        RecyclerViewUseCardLayout.setAdapter(adapter);

        loadDataCardFromFireBase();

        setSupportActionBar(toolbarUseCardLayout);
        getSupportActionBar().setTitle("Chọn tài khoản ngân hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadDataCardFromFireBase() {
        firestore.collection("users").document(userId).collection("card").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                    CardModel cardModel = queryDocumentSnapshot.toObject(CardModel.class);
                    cardModel.setId(queryDocumentSnapshot.getId());
                    list.add(cardModel);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UseCard_Layout.this, "Load lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}