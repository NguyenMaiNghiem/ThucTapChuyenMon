package com.example.trasua1;

import android.graphics.Region;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.telephony.ims.RegistrationManager;
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

import com.example.trasua1.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;


public class DangKiFragment extends Fragment {

    Button btnDangKi;
    EditText edtPhoneDK,edtNameDK,edtEmailDK,edtPassDK,edtAddressDK;
    TextView txtDangNhap;

    FirebaseAuth auth;
//    FirebaseDatabase database;

    ProgressBar progressBar;

    public DangKiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dang_ki, container, false);

        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDangKi = root.findViewById(R.id.btnDangKi);
        edtPhoneDK = root.findViewById(R.id.edtPhoneDK);
        edtNameDK = root.findViewById(R.id.edtNameDK);
        edtEmailDK = root.findViewById(R.id.edtEmailDK);
        edtAddressDK = root.findViewById(R.id.edtAddressDK);
        edtPassDK = root.findViewById(R.id.edtPassDK);
        txtDangNhap = root.findViewById(R.id.txtDangNhap);

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
                NavHostFragment.findNavController(DangKiFragment.this).navigate(R.id.action_nav_dangki_to_nav_dangnhap);
            }
        });

        return root;
    }

    private void createUser() {
        String userPhone = edtPhoneDK.getText().toString().trim();
        String userName = edtNameDK.getText().toString().trim();
        String userEmail = edtEmailDK.getText().toString().trim();
        String userAddress = edtAddressDK.getText().toString().trim();
        String userPass = edtPassDK.getText().toString().trim();

        if (userPhone.isEmpty()){
            edtPhoneDK.setError("Số điện thoại không được bỏ trống");
            edtPhoneDK.requestFocus();
            return;
        }
        if (userName.isEmpty()){
            edtNameDK.setError("Tên không được bỏ trống");
            edtNameDK.requestFocus();
            return;
        }
        if (userAddress.isEmpty()){
            edtAddressDK.setError("Địa chỉ không được bỏ trống");
            edtAddressDK.requestFocus();
            return;
        }
        if (userEmail.isEmpty()){
            edtEmailDK.setError("Email không được bỏ trống");
            edtEmailDK.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            edtEmailDK.setError("Email không hợp lệ");
            edtEmailDK.requestFocus();
            return;
        }
        if (userPass.isEmpty()){
            edtPassDK.setError("Mật khẩu không được bỏ trống");
            edtPassDK.requestFocus();
            return;
        }
        if (userPass.length() < 6){
            edtPassDK.setError("Mật khẩu phải có ít nhất 6 kí tự");
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
                                Toast.makeText(getContext(), "Đăng kí thành công"+task.getException(),Toast.LENGTH_LONG).show();

                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Đăng  kí thất bại"+task.getException(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Đăng  kí thất bại"+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}