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

import com.example.trasuaserver1.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class DangKi extends AppCompatActivity {
    Button btnDangKi;
    EditText edtPhoneDK,edtNameDK,edtEmailDK,edtPassDK,edtAddressDK;
    TextView txtDangNhap;

    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDangKi = findViewById(R.id.btnDangKi);
        edtPhoneDK = findViewById(R.id.edtPhoneDK);
        edtNameDK = findViewById(R.id.edtNameDK);
        edtEmailDK = findViewById(R.id.edtEmailDK);
        edtAddressDK = findViewById(R.id.edtAddressDK);
        edtPassDK = findViewById(R.id.edtPassDK);
        txtDangNhap = findViewById(R.id.txtDangNhap);

        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangKi.this,DangNhap.class));
            }
        });
    }
    private void createUser() {
        String userPhone = edtPhoneDK.getText().toString().trim();
        String userName = edtNameDK.getText().toString().trim();
        String userEmail = edtEmailDK.getText().toString().trim();
        String userAddress = edtAddressDK.getText().toString().trim();
        String userPass = edtPassDK.getText().toString().trim();

        if (userPhone.isEmpty()){
            edtPhoneDK.setError("S??? ??i???n tho???i kh??ng ???????c b??? tr???ng");
            edtPhoneDK.requestFocus();
            return;
        }
        if (userPhone.length() < 10 || userPhone.length() > 10){
            edtPhoneDK.setError("S??? ??i???n tho???i ph???i 10 ch??? s???");
            edtPhoneDK.requestFocus();
            return;
        }
        if (userName.isEmpty()){
            edtNameDK.setError("T??n kh??ng ???????c b??? tr???ng");
            edtNameDK.requestFocus();
            return;
        }
        if (userAddress.isEmpty()){
            edtAddressDK.setError("?????a ch??? kh??ng ???????c b??? tr???ng");
            edtAddressDK.requestFocus();
            return;
        }
        if (userEmail.isEmpty()){
            edtEmailDK.setError("Email kh??ng ???????c b??? tr???ng");
            edtEmailDK.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            edtEmailDK.setError("Email kh??ng h???p l???");
            edtEmailDK.requestFocus();
            return;
        }
        if (userPass.isEmpty()){
            edtPassDK.setError("M???t kh???u kh??ng ???????c b??? tr???ng");
            edtPassDK.requestFocus();
            return;
        }
        if (userPass.length() < 6){
            edtPassDK.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???");
            edtPassDK.requestFocus();
            return;
        }

//      create User
        auth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    UserModel userModel = new UserModel(userPhone,userName,userEmail,userAddress,userPass);
//                    String id = task.getResult().getUser().getUid();
//                    database.getReference().child("Users").child(id).setValue(userModel);

                    FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(DangKi.this, "????ng k?? th??nh c??ng"+task.getException(),Toast.LENGTH_LONG).show();

                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(DangKi.this, "????ng  k?? th???t b???i"+task.getException(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DangKi.this, "????ng  k?? th???t b???i"+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}