package com.example.shopping_online.Layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
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

import com.example.shopping_online.Adapter.ProductCartAdapter;
import com.example.shopping_online.DTO.CartModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cart_Layout extends AppCompatActivity {

    private Toolbar toolbarCartLayout;
    private CheckBox checkboxSelectAll;
    private RecyclerView RecyclerViewProductCart;
    private ArrayList<CartModel> list;
    private ProductCartAdapter adapter;
    private FirebaseFirestore firestore;
    private boolean isSelectAllChanging = false;
    private TextView txtPayProductOnCart;
    private SharedPreferences sharedPreferences;
    private TextView txtAllPriceCart;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbarCartLayout = findViewById(R.id.toolbarCartLayout);
        RecyclerViewProductCart = findViewById(R.id.RecyclerViewProductCart);
        checkboxSelectAll = findViewById(R.id.checkboxSelectAll);
        txtPayProductOnCart = findViewById(R.id.txtPayProductOnCart);
        txtAllPriceCart = findViewById(R.id.txtAllPriceCart);

        txtPayProductOnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart_Layout.this, Payment_Layout.class));
                getInformationTotalPrice();
            }
        });

        firestore = FirebaseFirestore.getInstance();

        RecyclerViewProductCart.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ProductCartAdapter(this, list);
        RecyclerViewProductCart.setAdapter(adapter);

        checkboxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSelectAllChanging) { // Chỉ xử lý nếu không phải thay đổi tự động
                adapter.selectAllItem(isChecked);
            }
//            for (CartModel cartModel : list) {
//                cartModel.setSelected(isChecked);
//            }
//            adapter.selectAllItem(isChecked);
//            adapter.notifyDataSetChanged();z
//            if (isChecked) {
//                adapter.calculateTotalPrice();
//                adapter.selectAllItem(true);
//            } else {
//                adapter.resetTotalPrice();
//                adapter.selectAllItem(false);
//            }
        });

        loadDataCartFromFireStore();

        setSupportActionBar(toolbarCartLayout);
        getSupportActionBar().setTitle("Giỏ hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getInformationTotalPrice() {
        String totalPriceShare = txtAllPriceCart.getText().toString().trim();

        sharedPreferences = getSharedPreferences("TotalPrice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("price", totalPriceShare);
        editor.apply();
        Log.d("logshare", totalPriceShare);
    }

    private void loadDataCartFromFireStore() {
        firestore.collection("users").document(userId).collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    CartModel cartModel = queryDocumentSnapshot.toObject(CartModel.class);
                    cartModel.setId(queryDocumentSnapshot.getId());
                    list.add(cartModel);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cart_Layout.this, "Load dữ liệu lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateTotalPriceUI(double totalPrice) {
         // Đảm bảo ID TextView chính xác
        if (txtAllPriceCart != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            txtAllPriceCart.setText(String.valueOf(decimalFormat.format(totalPrice)));
        }
    }

    public void updateSelectAllState(boolean isAllSelected) {
        isSelectAllChanging = true;
        checkboxSelectAll.setChecked(isAllSelected);
        isSelectAllChanging = false;
    }

}