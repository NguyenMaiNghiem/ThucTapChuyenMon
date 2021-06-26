package com.example.trasua1.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trasua1.R;
import com.example.trasua1.activity.DetailActivity;
import com.example.trasua1.models.SanPhamNoiBat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    Context context;
    List<SanPhamNoiBat> sanPhamNoiBatList;

    public SanPhamAdapter(Context context, List<SanPhamNoiBat> sanPhamNoiBatList) {
        this.context = context;
        this.sanPhamNoiBatList = sanPhamNoiBatList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sanphamnoibat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(sanPhamNoiBatList.get(position).getImg_url()).into(holder.popImg);
        holder.name.setText(sanPhamNoiBatList.get(position).getName());
        holder.price.setText(sanPhamNoiBatList.get(position).getPrice()+" VNĐ");
        holder.rating.setText(sanPhamNoiBatList.get(position).getRating());
        holder.discount.setText(sanPhamNoiBatList.get(position).getDiscount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail",sanPhamNoiBatList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sanPhamNoiBatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popImg;
        TextView name,price,rating,discount;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            popImg = itemView.findViewById(R.id.pop_img);
            name = itemView.findViewById(R.id.pop_name);
            price = itemView.findViewById(R.id.pop_price);
            rating = itemView.findViewById(R.id.pop_rating);
            discount = itemView.findViewById(R.id.pop_discount);
        }
    }
}
