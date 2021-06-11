package com.example.trasua1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.trasua1.R;
import com.example.trasua1.adapter.SanPhamMoiAdapter;
import com.example.trasua1.adapter.ViewAllAdapter;
import com.example.trasua1.models.SanPhamMoi;
import com.example.trasua1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity2 extends AppCompatActivity {

    FirebaseFirestore firestore;

    RecyclerView recyclerView;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    List<SanPhamMoi> sanPhamMoiList;
    Toolbar toolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all2);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        String category = getIntent().getStringExtra("category");
        recyclerView = findViewById(R.id.view_all_rec2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setVisibility(View.GONE);

        sanPhamMoiList = new ArrayList<>();
        sanPhamMoiAdapter = new SanPhamMoiAdapter(this,sanPhamMoiList);
        recyclerView.setAdapter(sanPhamMoiAdapter);

        ///Getting all

        firestore.collection("SanPhamMoi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                    SanPhamMoi sanPhamMoi = documentSnapshot.toObject(SanPhamMoi.class);
                    sanPhamMoiList.add(sanPhamMoi);
                    sanPhamMoiAdapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}