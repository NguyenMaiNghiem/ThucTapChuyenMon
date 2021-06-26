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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class QuenMK extends AppCompatActivity {
    Button btnDoiPass,btnDangNhap;
    EditText edtEmailReset;
    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mk);

        btnDoiPass = findViewById(R.id.btnDoiPass);
        btnDangNhap = findViewById(R.id.btnDangNhap1);
        edtEmailReset = findViewById(R.id.edtEmailReset);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuenMK.this,MainActivity.class));
            }
        });

        btnDoiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });
    }
    private void resetPassword() {
        String email = edtEmailReset.getText().toString().trim();

        if (email.isEmpty()){
            edtEmailReset.setError("Email không được bỏ trống");
            edtEmailReset.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailReset.setError("Email không hợp lệ");
            edtEmailReset.requestFocus();
            return;
        }

        progressBar.setVisibility(View.GONE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(QuenMK.this,"Bạn kiểm tra Email để đặt lại mật khẩu nhé !!!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(QuenMK.this,"Reset thất bại !!!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}