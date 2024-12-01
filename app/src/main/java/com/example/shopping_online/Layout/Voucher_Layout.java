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

import com.example.shopping_online.Adapter.VoucherAdapter;
import com.example.shopping_online.DTO.VoucherModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Voucher_Layout extends AppCompatActivity {
    private Toolbar toolbarVoucherLayout;
    private RecyclerView RecyclerViewVoucher;
    private ArrayList<VoucherModel> list;
    private VoucherAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();
        toolbarVoucherLayout = findViewById(R.id.toolbarVoucherLayout);
        RecyclerViewVoucher = findViewById(R.id.RecyclerViewVoucher);

        list = new ArrayList<>();
        adapter = new VoucherAdapter(this, list);
        RecyclerViewVoucher.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewVoucher.setAdapter(adapter);

        loadDataVoucherFromFirebase();

        setSupportActionBar(toolbarVoucherLayout);
        getSupportActionBar().setTitle("Voucher khuyến mãi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadDataVoucherFromFirebase() {
        firestore.collection("voucher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                        VoucherModel voucherModel = queryDocumentSnapshot.toObject(VoucherModel.class);
                        voucherModel.setId(queryDocumentSnapshot.getId());
                        list.add(voucherModel);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(Voucher_Layout.this, "Load Lỗi", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Voucher_Layout.this, "Load Lỗi", Toast.LENGTH_LONG).show();
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