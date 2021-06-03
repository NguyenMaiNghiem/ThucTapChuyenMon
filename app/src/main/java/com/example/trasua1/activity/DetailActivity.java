package com.example.trasua1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trasua1.R;
import com.example.trasua1.models.SanPhamNoiBat;
import com.example.trasua1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    TextView name,price,rating,quantity;
    Button addtocard;
    ImageButton addItem,remoteItem;
    Toolbar toolbar;
    ViewAllModel viewAllModel = null;
    SanPhamNoiBat sanPhamNoiBat = null;
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

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }
        if (object instanceof SanPhamNoiBat){
            sanPhamNoiBat = (SanPhamNoiBat) object;
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        detailImg = findViewById(R.id.detail_img);
        name = findViewById(R.id.detail_name);
        price = findViewById(R.id.detail_price);
        rating = findViewById(R.id.detail_rating);
        addtocard = findViewById(R.id.add_to_card);
        addItem = findViewById(R.id.add_item);
        remoteItem = findViewById(R.id.remove_item);
        quantity = findViewById(R.id.quantity);

//        totalPrice = Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;

        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailImg);
            name.setText(viewAllModel.getName());
            price.setText(viewAllModel.getPrice());
            rating.setText(viewAllModel.getRating());
        }
        if (sanPhamNoiBat != null){
            Glide.with(getApplicationContext()).load(sanPhamNoiBat.getImg_url()).into(detailImg);
            name.setText(sanPhamNoiBat.getName());
            price.setText(sanPhamNoiBat.getPrice());
            rating.setText(sanPhamNoiBat.getRating());
        }


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 100){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
//                    totalPrice = Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;
                }

            }
        });

        remoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 0){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
//                    totalPrice = Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;
                }

            }
        });

        addtocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocard();
            }
        });
    }

    private void addtocard() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

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