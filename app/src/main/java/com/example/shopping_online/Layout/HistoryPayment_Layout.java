package com.example.shopping_online.Layout;

import android.content.Intent;
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

import com.example.shopping_online.Adapter.HistoryPaymentAdapter;
import com.example.shopping_online.DTO.HistoryPaymentModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryPayment_Layout extends AppCompatActivity {

    private Toolbar toolbarHistoryLayout;
    private RecyclerView RecyclerViewHistory;
    private ArrayList<HistoryPaymentModel> list;
    private HistoryPaymentAdapter adapter;
    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_payment_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();

        toolbarHistoryLayout = findViewById(R.id.toolbarHistoryLayout);
        RecyclerViewHistory = findViewById(R.id.RecyclerViewHistory);

        setSupportActionBar(toolbarHistoryLayout);
        getSupportActionBar().setTitle("Lịch sử đặt hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new HistoryPaymentAdapter(this, list);
        RecyclerViewHistory.setAdapter(adapter);

        loadDataHistoryFromFirebase();

    }

    private void loadDataHistoryFromFirebase() {
        firestore.collection("users").document(userId).collection("historypayment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                    HistoryPaymentModel historyPaymentModel = queryDocumentSnapshot.toObject(HistoryPaymentModel.class);
                    historyPaymentModel.setId(queryDocumentSnapshot.getId());
                    list.add(historyPaymentModel);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HistoryPayment_Layout.this, "Load lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(HistoryPayment_Layout.this, Acount_Layout.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}