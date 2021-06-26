package com.example.trasua1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


public class DangNhapFragment extends Fragment implements View.OnClickListener {
    Button btnDangNhap;
    EditText edtEmail,edtPass;
    TextView txtDangKi,txtQuenPass;

    FirebaseAuth auth;

    ProgressBar progressBar;

    public DangNhapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dang_nhap, container, false);

        auth = FirebaseAuth.getInstance();

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDangNhap = root.findViewById(R.id.btnDangNhap);
        edtEmail = root.findViewById(R.id.edtEmail);
        edtPass = root.findViewById(R.id.edtPass);
        txtDangKi = root.findViewById(R.id.txtDangKi);
        txtQuenPass = root.findViewById(R.id.txtQuenPass);


        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(DangNhapFragment.this,DangKiFragment.class));
                NavHostFragment.findNavController(DangNhapFragment.this).navigate(R.id.action_nav_dangnhap_to_nav_dangki);
            }
        });

        txtQuenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DangNhapFragment.this).navigate(R.id.action_nav_dangnhap_to_resetPassFragment);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
    }

    private void loginUser() {
        String userEmail = edtEmail.getText().toString();
        String userPass = edtPass.getText().toString();

        if (userEmail.isEmpty()){
            edtEmail.setError("Email không được bỏ trống");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }
        if (userPass.isEmpty()){
            edtPass.setError("Mật khẩu không được bỏ trống");
            edtPass.requestFocus();
            return;
        }
        if (userPass.length() < 6){
            edtPass.setError("Mật khẩu phải có ít nhất 6 kí tự");
            edtPass.requestFocus();
            return;
        }

        //Login User
        auth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    NavHostFragment.findNavController(DangNhapFragment.this).navigate(R.id.action_nav_dangnhap_to_nav_trangchu);
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Đăng nhập thất bại !!! Vui lòng thử lại"+task.isSuccessful(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}