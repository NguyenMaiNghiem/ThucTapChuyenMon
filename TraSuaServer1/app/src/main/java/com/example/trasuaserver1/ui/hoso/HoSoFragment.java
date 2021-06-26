package com.example.trasuaserver1.ui.hoso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.trasuaserver1.models.UserModel;
import com.example.trasuaserver1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class HoSoFragment extends Fragment {
    CircleImageView profileImg;
    TextView name,email,phone,address;
    Button update;

    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    FirebaseDatabase database;

    private String userID;
    private FirebaseUser user;
    private DatabaseReference reference;


    public HoSoFragment() {
        // Required empty public constructor
    }

    String Name,Phone,Email,Address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ho_so, container, false);
        profileImg = root.findViewById(R.id.imgUS);
        name = root.findViewById(R.id.edtNameUS);
        email = root.findViewById(R.id.edtEmailUS);
        phone = root.findViewById(R.id.edtPhoneUS);
        address = root.findViewById(R.id.edtAddressUS);
        update = root.findViewById(R.id.btnUpdate);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        if (user != null){
            userID = user.getUid();
            if (userID != null){
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserModel userProfile = snapshot.getValue(UserModel.class);

                        Phone = userProfile.phone;
                        Name = userProfile.name;
                        Email = userProfile.email;
                        Address = userProfile.address;

                        phone.setText(Phone);
                        name.setText(Name);
                        email.setText(Email);
                        address.setText(Address);

                        Glide.with(getContext()).load(userProfile.getProfileImg()).into(profileImg);

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getContext(),"Có lỗi xảy ra !!! ",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        return root;
    }

    private void updateUserProfile() {
        String userPhone = phone.getText().toString().trim();
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userAddress = address.getText().toString().trim();

        if (userPhone.isEmpty()){
            phone.setError("Số điện thoại không được bỏ trống");
            phone.requestFocus();
            return;
        }
        if (userName.isEmpty()){
            name.setError("Tên không được bỏ trống");
            name.requestFocus();
            return;
        }
        if (userAddress.isEmpty()){
            address.setError("Địa chỉ không được bỏ trống");
            address.requestFocus();
            return;
        }
        if (userEmail.isEmpty()){
            email.setError("Email không được bỏ trống");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Email không hợp lệ");
            email.requestFocus();
            return;
        }

        HashMap hashMap = new HashMap();
        hashMap.put("phone",userPhone);
        hashMap.put("name",userName);
        hashMap.put("email",userEmail);
        hashMap.put("address",userAddress);

        reference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(HoSoFragment.this).navigate(R.id.action_nav_hoso_to_nav_home);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null){
            Uri profileUri = data.getData();
            profileImg.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString());

                            Toast.makeText(getContext(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}