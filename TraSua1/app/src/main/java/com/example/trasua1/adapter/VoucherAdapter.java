package com.example.trasua1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trasua1.R;
import com.example.trasua1.models.VoucherModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder>{
    Context context;
    List<VoucherModel> voucherModelList;

    public VoucherAdapter(Context context, List<VoucherModel> voucherModelList) {
        this.context = context;
        this.voucherModelList = voucherModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(voucherModelList.get(position).getImg_url()).into(holder.voucherImg);
        holder.voucher_price.setText(voucherModelList.get(position).getPrice() + " VNĐ");

    }

    @Override
    public int getItemCount() {
        return voucherModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView voucherImg;
        TextView voucher_price;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            voucherImg = itemView.findViewById(R.id.voucher_img);
            voucher_price = itemView.findViewById(R.id.voucher_price);
        }
    }
}
