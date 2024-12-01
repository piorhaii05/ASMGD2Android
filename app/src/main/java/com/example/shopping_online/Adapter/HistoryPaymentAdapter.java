package com.example.shopping_online.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping_online.DTO.HistoryPaymentModel;
import com.example.shopping_online.DTO.ItemsPaymentModel;
import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryPaymentAdapter extends RecyclerView.Adapter<HistoryPaymentAdapter.ViewHolderHistoryPayment> {

    private final Context context;
    private final ArrayList<HistoryPaymentModel> list;
    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();

    public HistoryPaymentAdapter(Context context, ArrayList<HistoryPaymentModel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolderHistoryPayment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.items_history_payment, parent, false);
        return new ViewHolderHistoryPayment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistoryPayment holder, int position) {
        HistoryPaymentModel historyPaymentModel = list.get(position);

        // Thiết lập thông tin của item
        holder.txtDateHistoryPayment.setText(historyPaymentModel.getDate_history());
        holder.txtInforHistoryPayment.setText(historyPaymentModel.getInfor_payment_history());
        holder.txtPriceHistoryPayment.setText(String.valueOf(historyPaymentModel.getPrice_history()));
        holder.txtDeleteItemsHistoryPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa lịch sử đặt hàng")
                        .setMessage("Bạn có chắc chắn muốn xóa lịch sử đặt hàng này không?")
                        .setPositiveButton("OK", (dialog, which) -> deleteHistoryFromFireBase(historyPaymentModel, position))
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });

        // Tạo adapter chỉ một lần cho RecyclerView con
        if (holder.adapter == null) {
            holder.listitemsPaymentModels = new ArrayList<>();
            holder.adapter = new ItemsPaymentAdapter(context, holder.listitemsPaymentModels);
            holder.RecyclerViewItemsHistoryPayment.setLayoutManager(new LinearLayoutManager(context));
            holder.RecyclerViewItemsHistoryPayment.setAdapter(holder.adapter);
        }

        loadDataItemsHistoryFormFireBase(historyPaymentModel.getId(), holder);
    }

    private void deleteHistoryFromFireBase(HistoryPaymentModel historyPaymentModel, int position) {
        firestore.collection("users").document(userId).collection("historypayment").document(historyPaymentModel.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    list.remove(position);
                    notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Delete lỗi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadDataItemsHistoryFormFireBase(String purchaseId, ViewHolderHistoryPayment holder) {
        firestore.collection("users").document(userId).collection("historypayment")
                .document(purchaseId)
                .collection("itemspayment")
                .get()
                .addOnSuccessListener(queryItemSnapshots -> {
                    holder.listitemsPaymentModels.clear();
                    for (QueryDocumentSnapshot itemDoc : queryItemSnapshots) {
                        ItemsPaymentModel itemsPaymentModel = itemDoc.toObject(ItemsPaymentModel.class);
                        itemsPaymentModel.setId(itemDoc.getId());
                        holder.listitemsPaymentModels.add(itemsPaymentModel);
                    }
                    Log.d("ItemsLoaded", "Loaded " + holder.listitemsPaymentModels.size() + " items for purchaseId: " + purchaseId);
                    holder.adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Load lỗi từ itemspayment", Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderHistoryPayment extends RecyclerView.ViewHolder {
        TextView txtDateHistoryPayment, txtDeleteItemsHistoryPayment, txtInforHistoryPayment, txtPriceHistoryPayment;
        RecyclerView RecyclerViewItemsHistoryPayment;
        ItemsPaymentAdapter adapter;
        ArrayList<ItemsPaymentModel> listitemsPaymentModels;

        public ViewHolderHistoryPayment(@NonNull View itemView) {
            super(itemView);
            RecyclerViewItemsHistoryPayment = itemView.findViewById(R.id.RecyclerViewItemsHistoryPayment);
            txtDateHistoryPayment = itemView.findViewById(R.id.txtDateHistoryPayment);
            txtDeleteItemsHistoryPayment = itemView.findViewById(R.id.txtDeleteItemsHistoryPayment);
            txtInforHistoryPayment = itemView.findViewById(R.id.txtInforHistoryPayment);
            txtPriceHistoryPayment = itemView.findViewById(R.id.txtPriceHistoryPayment);
        }
    }
}
