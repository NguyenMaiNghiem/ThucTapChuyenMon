package com.example.trasua1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;

import org.jetbrains.annotations.NotNull;

public class ResetPassFragment extends Fragment {
    Button btnDoiPass;
    EditText edtEmailReset;
    ProgressBar progressBar;

    FirebaseAuth auth;

    public ResetPassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reset_pass, container, false);

        btnDoiPass = root.findViewById(R.id.btnDoiPass);
        edtEmailReset = root.findViewById(R.id.edtEmailReset);
        progressBar = root.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnDoiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });

        return root;
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
                    Toast.makeText(getContext(),"Bạn kiểm tra Email để đặt lại mật khẩu nhé !!!",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"Reset thất bại !!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}