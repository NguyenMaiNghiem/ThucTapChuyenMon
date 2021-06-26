package com.example.trasuaserver1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasuaserver1.adapter.VoucherAdapter;
import com.example.trasuaserver1.models.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VoucherFragment extends Fragment {

    RecyclerView voucherRec;
    FirebaseFirestore db;
    //SanPhamNoiBat Items
    List<VoucherModel> voucherModelList;
    VoucherAdapter voucherAdapter;

    public VoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_voucher, container, false);

        db = FirebaseFirestore.getInstance();
        voucherRec = root.findViewById(R.id.recyclerview_voucher);

        //Voucher item
        voucherRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        voucherModelList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(getActivity(),voucherModelList);
        voucherRec.setAdapter(voucherAdapter);

        db.collection("Voucher")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                VoucherModel voucherModel = document.toObject(VoucherModel.class);
                                voucherModelList.add(voucherModel);
                                voucherAdapter.notifyDataSetChanged();

                            }
                        } else {

                            Toast.makeText(getContext(),"Lỗi"+task.getException(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        return root;
    }
}