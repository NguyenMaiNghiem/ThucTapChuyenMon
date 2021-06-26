package com.example.trasuaserver1.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasuaserver1.R;
import com.example.trasuaserver1.models.OrdersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    List<OrdersModel> ordersModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public OrdersAdapter(Context context, List<OrdersModel> ordersModelList) {
        this.context = context;
        this.ordersModelList = ordersModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.name.setText(ordersModelList.get(position).getProductName());
        holder.price.setText(ordersModelList.get(position).getProductPrice());
        holder.date.setText(ordersModelList.get(position).getCurrentDate());
        holder.time.setText(ordersModelList.get(position).getCurrentTime());
        holder.quantity.setText(ordersModelList.get(position).getTotalQuantity());


    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
        TextView userName,userPhone,userAddress,name,price,date,time,quantity,totalPrice;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userPhone = itemView.findViewById(R.id.user_phone);
            userAddress = itemView.findViewById(R.id.user_address);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.product_date);
            time = itemView.findViewById(R.id.product_time);
            quantity = itemView.findViewById(R.id.product_total);
            totalPrice = itemView.findViewById(R.id.product_total_price);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

//        public void setItemClickListener(ItemClickListener itemClickListener){
//            this.itemClickListener = itemClickListener;
//        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
