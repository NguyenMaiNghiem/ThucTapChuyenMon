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
import com.example.trasuaserver1.models.ViewAllModel;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder>{
    Context context;
    List<ViewAllModel> list;
    OnItemClick onItemClick;
    FirebaseFirestore firestore;
//    List<ViewAllModel> mlistOld;

    public ViewAllAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;
//        this.mlistOld = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice()+ " VNĐ");
        holder.rating.setText(list.get(position).getRating());

        firestore = FirebaseFirestore.getInstance();

        final String item_id = list.get(position).itemId;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ID : " + item_id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("detail",list.get(position));
//                context.startActivity(intent);
            }
        });

//        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//                popupMenu.inflate(R.menu.select_menu);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.edit_menu:
//                                if (onItemClick != null) {
////                                    Toast.makeText(context, "ID : " + item_id, Toast.LENGTH_SHORT).show();
////                                    onItemClick.onUpdate(getAdapterPosition());
//                                }
//                                break;
//                            case R.id.delete_menu:
//                                if (onItemClick != null) {
//                                    firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
//                                            notifyItemRemoved(position);
//                                        }
//                                    });
////                                    onItemClick.onDelete(getAdapterPosition());
//                                }
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popupMenu.show();
//
//            }
//        });
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
        ImageButton imgMenu;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.view_img);
            name = itemView.findViewById(R.id.view_name);
            price = itemView.findViewById(R.id.view_price);
            rating = itemView.findViewById(R.id.view_rating);
            imgMenu = itemView.findViewById(R.id.imgMenu);

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null){
                        onItemClick.onClick(view,getAdapterPosition());
                    }
                }
           });

            imgMenu.setOnClickListener(new View.OnClickListener() {
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
//                                    Toast.makeText(context, "ID : " + item_id, Toast.LENGTH_SHORT).show();
                                    onItemClick.onUpdate(getAdapterPosition());
                                    }
                                    break;
                                case R.id.delete_menu:
                                    if (onItemClick != null) {
//                                        firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
//                                                notifyItemRemoved(position);
//                                            }
//                                        });
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
        }
    }


    public void setOnItemCLick(OnItemClick onItemCLick){
        this.onItemClick = onItemCLick;
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String strSearch = constraint.toString();
//                if (strSearch.isEmpty()){
//                    list = mlistOld;
//                }else {
//                    List<ViewAllModel> list1 = new ArrayList<>();
//                    for (ViewAllModel viewAllModel : mlistOld){
//                        if (viewAllModel.getName().toLowerCase().contains(strSearch.toLowerCase())){
//                            list1.add(viewAllModel);
//                        }
//                    }
//                    list = list1;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values= list;
//
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                list = (List<ViewAllModel>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
}
