package com.example.trasuaserver1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasuaserver1.adapter.OrdersAdapter;
import com.example.trasuaserver1.models.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

//    FirebaseRecyclerAdapter<Request,OrdersAdapter> adapter;

    FirebaseDatabase db;
    DatabaseReference request;



 public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        //Init
//        recyclerView = root.findViewById(R.id.ordersModelList);
//        recyclerView.setHasFixedSize(true);

        db = FirebaseDatabase.getInstance();
        request = db.getReference("");


        return root;
    }
}