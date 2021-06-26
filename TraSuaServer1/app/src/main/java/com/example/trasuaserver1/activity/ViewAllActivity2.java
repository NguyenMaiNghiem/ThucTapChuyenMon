package com.example.trasuaserver1.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trasuaserver1.OnItemClick;
import com.example.trasuaserver1.R;
import com.example.trasuaserver1.adapter.SanPhamMoiAdapter;
import com.example.trasuaserver1.models.SanPhamMoi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ViewAllActivity2 extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseStorage storage;

    RecyclerView recyclerView;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    List<SanPhamMoi> sanPhamMoiList;
    Toolbar toolbar;
    ProgressBar progressBar;

    EditText editName,editPrice,editCategory,editRating,editHot;
    ImageView editImage;

    private final int PICK_IMAGE_REQUEST = 71;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all2);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.view_all_rec2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setVisibility(View.GONE);

        sanPhamMoiList = new ArrayList<>();
        sanPhamMoiAdapter = new SanPhamMoiAdapter(this,sanPhamMoiList);
        recyclerView.setAdapter(sanPhamMoiAdapter);



        ///Getting all

        firestore.collection("SanPhamMoi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                    final String item_id = documentSnapshot.getId();

                    SanPhamMoi sanPhamMoi = documentSnapshot.toObject(SanPhamMoi.class).withId(item_id);
                    sanPhamMoiList.add(sanPhamMoi);
                    sanPhamMoiAdapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


                    sanPhamMoiAdapter.setOnItemCLick(new OnItemClick() {
                        @Override
                        public void onClick(View view, int position) {

                        }

                        @Override
                        public void onUpdate(int position) {
                            final String item_id = sanPhamMoiList.get(position).itemId;

                            final Dialog dialog = new Dialog(ViewAllActivity2.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_edit);

                            Window window = dialog.getWindow();
                            if (window == null){
                                return;
                            }
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            WindowManager.LayoutParams windownAttributes = window.getAttributes();
                            windownAttributes.gravity = Gravity.CENTER;
                            window.setAttributes(windownAttributes);

                            dialog.setCancelable(false);

                            Button btnEdit = dialog.findViewById(R.id.btnEdit);
                            Button btnCancelEdit = dialog.findViewById(R.id.btnCancelEdit);

                            editName = dialog.findViewById(R.id.edtItemNameEdit);
                            editPrice = dialog.findViewById(R.id.edtItemPriceEdit);
                            editCategory = dialog.findViewById(R.id.edtItemCategoryEdit);
                            editRating = dialog.findViewById(R.id.edtItemRatingEdit);
                            editHot = dialog.findViewById(R.id.edtItemHotEdit);
                            editImage = dialog.findViewById(R.id.imgItemEdit);

                            editImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
                                }
                            });


                            firestore.collection("SanPhamMoi").document(item_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    SanPhamMoi sanPhamMoi1 = documentSnapshot.toObject(SanPhamMoi.class);

                                    String name = sanPhamMoi1.name;
                                    String price = sanPhamMoi1.price;
                                    String category = sanPhamMoi1.category;
                                    String rating = sanPhamMoi1.rating;


                                    editName.setText(name);
                                    editPrice.setText(price);
                                    editCategory.setText(category);
                                    editRating.setText(rating);


                                    Glide.with(getApplicationContext()).load(sanPhamMoi1.getImg_url()).into(editImage);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                    Toast.makeText(ViewAllActivity2.this, "Có Lỗi Xảy ra"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                            btnEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (saveUri != null){
                                        ProgressDialog mDialog = new ProgressDialog(ViewAllActivity2.this);
                                        mDialog.setMessage("Uploading...");
                                        mDialog.show();

                                        String imageName = UUID.randomUUID().toString();
                                        StorageReference reference = storage.getReference().child("images/"+imageName);
                                        reference.putFile(saveUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                        Toast.makeText(ViewAllActivity2.this,"Uploaded",Toast.LENGTH_LONG).show();

                                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                HashMap<String,Object> hashMap = new HashMap<>();
                                                                hashMap.put("name",editName.getText().toString());
                                                                hashMap.put("price",editPrice.getText().toString());
                                                                hashMap.put("category",editCategory.getText().toString());
                                                                hashMap.put("rating",editRating.getText().toString());
                                                                hashMap.put("img_url",uri.toString());

                                                                firestore.collection("SanPhamMoi").document(item_id).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(ViewAllActivity2.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                                        sanPhamMoiAdapter.notifyItemChanged(position);
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                                                        Toast.makeText(ViewAllActivity2.this, "Cập nhật thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                dialog.dismiss();

                                                            }
                                                        });
                                                        mDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                                mDialog.dismiss();
                                                Toast.makeText(ViewAllActivity2.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull @org.jetbrains.annotations.NotNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                                mDialog.setMessage("Upload "+progress+"%");
                                            }
                                        });
                                    }

                                }
                            });

                            btnCancelEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }

                        @Override
                        public void onDelete(int position) {
                            final String item_id = sanPhamMoiList.get(position).itemId;

                            final Dialog dialog = new Dialog(ViewAllActivity2.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_delete);

                            Window window = dialog.getWindow();
                            if (window == null){
                                return;
                            }
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            WindowManager.LayoutParams windownAttributes = window.getAttributes();
                            windownAttributes.gravity = Gravity.CENTER;
                            window.setAttributes(windownAttributes);

                            dialog.setCancelable(false);

                            Button btnDelete = dialog.findViewById(R.id.btnYesDelete);
                            Button btnCancelDelete = dialog.findViewById(R.id.btnNoDelete);

                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    firestore.collection("SanPhamMoi").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ViewAllActivity2.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            sanPhamMoiAdapter.notifyItemRemoved(position);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                            Toast.makeText(ViewAllActivity2.this, "Xóa thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });

                            btnCancelDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();


                        }
                    });





                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            editImage.setImageURI(saveUri);
        }
    }
}