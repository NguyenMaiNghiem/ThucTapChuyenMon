package com.example.trasua1.ui.dangxuat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trasua1.R;
import com.example.trasua1.models.UserModel;
import com.example.trasua1.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class DangXuatFragment extends Fragment {
    Button btnDangXuat;
    TextView Email,Address,Name,Phone,TamBiet;

    ProgressBar progressBar;

    private String userID;
    private FirebaseUser user;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dang_xuat, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDangXuat = root.findViewById(R.id.btnDangXuat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");


        Email = root.findViewById(R.id.Email);
        Name = root.findViewById(R.id.Name);
        Phone = root.findViewById(R.id.Phone);
        Address = root.findViewById(R.id.Address);
        TamBiet = root.findViewById(R.id.TamBiet);

        if (user != null){
            userID = user.getUid();
            if (userID != null){
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserModel userProfile = snapshot.getValue(UserModel.class);

                        if (userProfile != null){
                            String email = userProfile.email;
                            String phone = userProfile.phone;
                            String address = userProfile.address;
                            String name = userProfile.name;


                            TamBiet.setText("Tạm biệt "+name+"!");
                            Email.setText(email);
                            Address.setText(address);
                            Name.setText(name);
                            Phone.setText(phone);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getContext(),"Có lỗi xảy ra !!! ",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }



        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Đăng xuất thành công",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                NavHostFragment.findNavController(DangXuatFragment.this).navigate(R.id.action_nav_dangxuat_to_nav_trangchu);
            }
        });

        return root;
    }
}