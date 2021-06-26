package com.example.trasua1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trasua1.DangNhapFragment;
import com.example.trasua1.R;
import com.example.trasua1.models.SanPhamNoiBat;
import com.example.trasua1.models.UserModel;
import com.example.trasua1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ImageView detailImg;
    int totalQuantity = 1;
    int totalPrice = 0;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public String userName,userPhone,userAddress,userID;
    public FirebaseUser user;
    public DatabaseReference reference;

    TextView name,price,rating,quantity;
    Button addtocard;

    ImageButton addItem,remoteItem;
    Toolbar toolbar;
    ViewAllModel viewAllModel = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        if (user != null){
            userID = user.getUid();
            if (userID != null){
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserModel userProfile = snapshot.getValue(UserModel.class);

                        if (userProfile != null){
                            userName = userProfile.name;
                            userPhone = userProfile.phone;
                            userAddress = userProfile.address;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(DetailActivity.this,"Có lỗi xảy ra !!! ",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }
//        if (object instanceof SanPhamNoiBat){
//            sanPhamNoiBat = (SanPhamNoiBat) object;
//        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        detailImg = findViewById(R.id.detail_img);
        name = findViewById(R.id.detail_name);
        price = findViewById(R.id.detail_price);
        rating = findViewById(R.id.detail_rating);

        addtocard = findViewById(R.id.add_to_card);
        addItem = findViewById(R.id.add_item);
        remoteItem = findViewById(R.id.remove_item);
        quantity = findViewById(R.id.quantity);

        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailImg);
            name.setText(viewAllModel.getName());
            price.setText(viewAllModel.getPrice()+" VNĐ");
            rating.setText(viewAllModel.getRating());

            totalPrice = Integer.parseInt(viewAllModel.getPrice())*totalQuantity;
        }
//        if (sanPhamNoiBat != null){
//            Glide.with(getApplicationContext()).load(sanPhamNoiBat.getImg_url()).into(detailImg);
//            name.setText(sanPhamNoiBat.getName());
//            price.setText(sanPhamNoiBat.getPrice()+" VNĐ");
//            rating.setText(sanPhamNoiBat.getRating());
//
//            totalPrice = sanPhamNoiBat.getPrice()*totalQuantity;
//        }



        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 100){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    totalPrice = Integer.parseInt(viewAllModel.getPrice())*totalQuantity;
                }
            }
        });

        remoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    totalPrice = Integer.parseInt(viewAllModel.getPrice())*totalQuantity;
                }
            }
        });


        addtocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
                addtocard.setVisibility(View.GONE);
            }
        });

    }

    private void openDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windownAttributes = window.getAttributes();
        windownAttributes.gravity = gravity;
        window.setAttributes(windownAttributes);

        dialog.setCancelable(false);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocard();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, HomeActivity.class));
//                Intent intent = new Intent(DetailActivity.this, DangNhapFragment.class);
//                startActivity(intent);

//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.Detail_layout, new DangNhapFragment()).commit();

//                DangNhapFragment fragment = new DangNhapFragment();
//                FragmentManager manager = getSupportFragmentManager();
//                manager.beginTransaction().add(R.id.Detail_layout,fragment).commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addtocard() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("UserName",userName);
        cartMap.put("UserPhone",userPhone);
        cartMap.put("Address",userAddress);
        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                Toast.makeText(DetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}