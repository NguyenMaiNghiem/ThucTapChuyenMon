package com.example.trasua1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trasua1.R;
import com.example.trasua1.activity.ViewAllActivity;
import com.example.trasua1.models.NavCategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {
    Context context;
    List<NavCategoryModel> navCategoryModelList;

    public NavCategoryAdapter(Context context, List<NavCategoryModel> navCategoryModelList) {
        this.context = context;
        this.navCategoryModelList = navCategoryModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_theloai_item_,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(navCategoryModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(navCategoryModelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("category",navCategoryModelList.get(position).getCategory());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return navCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.cat_nav_name);
        }
    }
}
