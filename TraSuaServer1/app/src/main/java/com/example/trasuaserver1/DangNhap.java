package com.example.trasuaserver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasuaserver1.activity.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class DangNhap extends AppCompatActivity {
    Button btnDangNhap;
    EditText edtEmail, edtPass;
    TextView txtDangKi, txtQuenPass;
    String userEmail, userPass;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseDatabase db;
    DatabaseReference reference;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        firestore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        txtDangKi = findViewById(R.id.txtDangKi);
        txtQuenPass = findViewById(R.id.txtQuenPass);


        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this,DangKi.class));

            }
        });

        txtQuenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this,QuenMK.class));            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    private void loginUser() {
        userEmail = edtEmail.getText().toString();
        userPass = edtPass.getText().toString();

        if (userEmail.isEmpty()) {
            edtEmail.setError("Email không được bỏ trống");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }
        if (userPass.isEmpty()) {
            edtPass.setError("Mật khẩu không được bỏ trống");
            edtPass.requestFocus();
            return;
        }
        if (userPass.length() < 6) {
            edtPass.setError("Mật khẩu phải có ít nhất 6 kí tự");
            edtPass.requestFocus();
            return;
        }

        //Login User
        auth.signInWithEmailAndPassword(userEmail, userPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkAdmin(authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(DangNhap.this, "Đăng Nhập Thất Bại !", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkAdmin(String uid) {
        reference.child(uid).child("isAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String isAdmin = (String) snapshot.getValue();
                if (isAdmin.equals("true")) {
                    Toast.makeText(DangNhap.this, "Đăng Nhập Thành Công !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(DangNhap.this, HomeActivity.class));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DangNhap.this, "Vui Lòng Đăng Nhập Với Tài Khoản Admin !", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(DangNhap.this, "Có lỗi xảy ra ! "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}