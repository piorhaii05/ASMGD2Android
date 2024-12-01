package com.example.shopping_online.Layout;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopping_online.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Banner_Layout extends AppCompatActivity {

    private Button btn_started;
    private TextView txt_goto_login, txt_goto_register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_banner_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();

        btn_started = findViewById(R.id.btn_started);
        txt_goto_login = findViewById(R.id.txt_goto_login);
        txt_goto_register = findViewById(R.id.txt_goto_register);

        btn_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAcountToScreen();
            }
        });
        txt_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Banner_Layout.this, Login_Layout.class));
            }
        });
        txt_goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Banner_Layout.this, Register_Layout.class));
            }
        });

    }

    private void loadAcountToScreen() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        if(!username.isEmpty() || !password.isEmpty()){

            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(Banner_Layout.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(Banner_Layout.this, Menu_Layout.class));
                        startActivity(new Intent(Banner_Layout.this, ASM_GD1.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Banner_Layout.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            startActivity(new Intent(Banner_Layout.this, Login_Layout.class));
        }
    }
}