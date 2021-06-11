package com.example.trasua1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasua1.R;
import com.example.trasua1.activity.HomeActivity;
import com.example.trasua1.activity.ViewAllActivity;
import com.example.trasua1.activity.ViewAllActivity1;
import com.example.trasua1.activity.ViewAllActivity2;
import com.example.trasua1.adapter.SanPhamAdapter;
import com.example.trasua1.adapter.SanPhamMoiAdapter;
import com.example.trasua1.adapter.TheLoaiAdapter;
import com.example.trasua1.adapter.ViewAllAdapter;
import com.example.trasua1.models.SanPhamMoi;
import com.example.trasua1.models.SanPhamNoiBat;
import com.example.trasua1.models.TheLoai;
import com.example.trasua1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    TextView viewall,viewall1,viewall2;

    ProgressBar progressBar;
    ScrollView scrollView;

    RecyclerView sanPhamNoiBatRec,theLoaiRec,sanPhamMoiRec;
    FirebaseFirestore db;

    //SanPhamNoiBat Items
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;


    //TheLoai Items
    List<TheLoai> theLoaiList;
    TheLoaiAdapter theLoaiAdapter;

    //SanPhamMoi Items
    List<SanPhamMoi> sanPhamMoiList;
    SanPhamMoiAdapter sanPhamMoiAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        db = FirebaseFirestore.getInstance();

        sanPhamNoiBatRec = root.findViewById(R.id.pop_rec);
        theLoaiRec = root.findViewById(R.id.explore_rec);
        sanPhamMoiRec = root.findViewById(R.id.recommended_rec);

        progressBar = root.findViewById(R.id.progressbar);
        scrollView = root.findViewById(R.id.scroll_view);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        viewall = root.findViewById(R.id.view_all);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity1.class);
                startActivity(intent);
            }
        });

        viewall1 = root.findViewById(R.id.view_all_1);
        viewall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity1.class);
                startActivity(intent);
            }
        });

        viewall2 = root.findViewById(R.id.view_all_2);
        viewall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllActivity2.class);
                startActivity(intent);
            }
        });

        //SanPhamNoiBat item
        sanPhamNoiBatRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getActivity(),viewAllModelList);
        sanPhamNoiBatRec.setAdapter(viewAllAdapter);

//        db.collection("SanPhamNoiBat")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                SanPhamNoiBat sanPhamNoiBat = document.toObject(SanPhamNoiBat.class);
//                                sanPhamNoiBatList.add(sanPhamNoiBat);
//                                sanPhamAdapter.notifyDataSetChanged();
//
//                                progressBar.setVisibility(View.GONE);
//                                scrollView.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//
//                            Toast.makeText(getActivity(),"Lỗi"+task.getException(),Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });

        db.collection("AllSanPham").whereEqualTo("event","hot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ViewAllModel viewAllModel = document.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {

                            Toast.makeText(getActivity(),"Lỗi"+task.getException(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //TheLoai item
        theLoaiRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        theLoaiList = new ArrayList<>();
        theLoaiAdapter = new TheLoaiAdapter(getActivity(),theLoaiList);
        theLoaiRec.setAdapter(theLoaiAdapter);

        db.collection("TheLoai")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                TheLoai theLoai = document.toObject(TheLoai.class);
                                theLoaiList.add(theLoai);
                                theLoaiAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {

                            Toast.makeText(getActivity(),"Lỗi"+task.getException(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //SanPhamMoi item
        sanPhamMoiRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        sanPhamMoiList = new ArrayList<>();
        sanPhamMoiAdapter = new SanPhamMoiAdapter(getActivity(),sanPhamMoiList);
        sanPhamMoiRec.setAdapter(sanPhamMoiAdapter);

        db.collection("SanPhamMoi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                SanPhamMoi sanPhamMoi = document.toObject(SanPhamMoi.class);
                                sanPhamMoiList.add(sanPhamMoi);
                                sanPhamMoiAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {

                            Toast.makeText(getActivity(),"Lỗi"+task.getException(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        return root;
    }

}