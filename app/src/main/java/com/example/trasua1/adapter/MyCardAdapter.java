package com.example.trasua1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasua1.R;
import com.example.trasua1.models.MyCardModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {
    Context context;
    List<MyCardModel> myCardModelList;
    int totalPrice = 0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyCardAdapter(Context context, List<MyCardModel> myCardModelList) {
        this.context = context;
        this.myCardModelList = myCardModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gio_hang_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.name.setText(myCardModelList.get(position).getProductName());
        holder.price.setText(myCardModelList.get(position).getProductPrice());
        holder.date.setText(myCardModelList.get(position).getCurrentDate());
        holder.time.setText(myCardModelList.get(position).getCurrentTime());
        holder.quantity.setText(myCardModelList.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(myCardModelList.get(position).getTotalPrice()));

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(myCardModelList.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    myCardModelList.remove(myCardModelList.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Xoá thành công",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(context,"Xóa Thất Bại"+task.getException(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        //tính tổng số tiền
        totalPrice = totalPrice + myCardModelList.get(position).getTotalPrice();
        Intent intent = new Intent("MyToTalAmount");
        intent.putExtra("totalAmount",totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    @Override
    public int getItemCount() {
        return myCardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,date,time,quantity,totalPrice;
        ImageView deleteItem;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.product_date);
            time = itemView.findViewById(R.id.product_time);
            quantity = itemView.findViewById(R.id.product_total);
            totalPrice = itemView.findViewById(R.id.product_total_price);
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }
}
