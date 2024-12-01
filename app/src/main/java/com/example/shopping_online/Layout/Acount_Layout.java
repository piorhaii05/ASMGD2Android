package com.example.shopping_online.Layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopping_online.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Acount_Layout extends AppCompatActivity {

    private Toolbar toolbarAcountLayout;
    private TextView txt_Email_Acount, txt_LogOut;
    private LinearLayout History_Acount, My_Rating_Acount, My_Location_Acount, My_Card_Acount;
    ImageView imageAvatar_select;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acount_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_LogOut = findViewById(R.id.txt_LogOut);
        txt_Email_Acount = findViewById(R.id.txt_Email_Acount);
        imageAvatar_select = findViewById(R.id.imageAvatar_select);
        toolbarAcountLayout = findViewById(R.id.toolbarAcountLayout);
        My_Rating_Acount = findViewById(R.id.My_Rating_Acount);
        My_Card_Acount = findViewById(R.id.My_Card_Acount);
        History_Acount = findViewById(R.id.History_Acount);
        My_Location_Acount = findViewById(R.id.My_Location_Acount);

        SharedPreferences sharedPreferences = getSharedPreferences("Acount", MODE_PRIVATE);
        String nameuser = sharedPreferences.getString("nameacount", "");
        String phoneuser = sharedPreferences.getString("phoneacount", "");

        txt_Email_Acount.setText(nameuser);

        My_Location_Acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acount_Layout.this, LocationTakeProduct_Layout.class));
            }
        });

        My_Card_Acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acount_Layout.this, MyCard_Layout.class));
            }
        });

        My_Rating_Acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acount_Layout.this, MyReview_Layout.class));
            }
        });

        History_Acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acount_Layout.this, HistoryPayment_Layout.class));
            }
        });

        setEmailForAcount();

        setSupportActionBar(toolbarAcountLayout);
        getSupportActionBar().setTitle("Hồ sơ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acount_Layout.this, Login_Layout.class));
            }
        });
        imageAvatar_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromLibrary();
            }
        });
    }

    private void selectImageFromLibrary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void setEmailForAcount() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        txt_Email_Acount.setText(username);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(Acount_Layout.this, Menu_Layout.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tiến hành tải ảnh lên Firebase Storage
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Lấy tham chiếu tới Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // Tạo tên tệp ảnh (có thể sử dụng UUID để tạo tên duy nhất)
        String imageName = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageReference.child(imageName);

        // Tải ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh sau khi tải lên thành công
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Lưu đường dẫn URL của ảnh vào Firestore
                        saveImageUrlToFirestore(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Acount_Layout.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToFirestore(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Lưu đường dẫn ảnh vào Firestore, ví dụ lưu vào một collection "users"
        Map<String, Object> user = new HashMap<>();
        user.put("imageUrl", imageUrl);

        // Thêm hoặc cập nhật dữ liệu vào Firestore
        db.collection("users")
                .document("user_id_here")  // Chọn document để lưu
                .set(user, SetOptions.merge())  // Dùng merge nếu muốn thêm hoặc cập nhật các trường dữ liệu
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Image URL saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to save image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}