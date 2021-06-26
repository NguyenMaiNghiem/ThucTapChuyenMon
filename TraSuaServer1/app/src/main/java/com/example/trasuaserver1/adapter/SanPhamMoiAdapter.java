package com.example.trasuaserver1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trasuaserver1.OnItemClick;
import com.example.trasuaserver1.R;
import com.example.trasuaserver1.models.SanPhamMoi;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.ViewHolder> {

    Context context;
    List<SanPhamMoi> list;
    OnItemClick onItemClick;
    FirebaseFirestore firestore;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sanphammoi_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        holder.rating.setText(list.get(position).getRating());

        firestore = FirebaseFirestore.getInstance();

        final String item_id = list.get(position).itemId;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ID : " + item_id, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price,rating;
        ImageButton imgMenu1;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rec_img);
            name = itemView.findViewById(R.id.rec_name);
            price = itemView.findViewById(R.id.rec_price);
            rating = itemView.findViewById(R.id.rec_rating);
            imgMenu1 = itemView.findViewById(R.id.imgMenu1);

            imgMenu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                    popupMenu.inflate(R.menu.select_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit_menu:
                                    if (onItemClick != null) {
                                        onItemClick.onUpdate(getAdapterPosition());
                                    }
                                    break;
                                case R.id.delete_menu:
                                    if (onItemClick != null) {
                                        onItemClick.onDelete(getAdapterPosition());
                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null){
                        onItemClick.onClick(view,getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemCLick(OnItemClick onItemCLick){
        this.onItemClick = onItemCLick;
    }

}
