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
import com.example.trasuaserver1.adapter.ViewAllAdapter;
import com.example.trasuaserver1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ViewAllActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseStorage storage;

    EditText editName,editPrice,editCategory,editRating,editHot;
    ImageView editImage;

    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;
    List<ViewAllModel> viewAllModelList;
    Toolbar toolbar;
    ProgressBar progressBar;

    private final int PICK_IMAGE_REQUEST = 71;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        String category = getIntent().getStringExtra("category");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setVisibility(View.GONE);

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getApplicationContext(),viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);


        /////Getting trasua
        if (category != null && category.equalsIgnoreCase("trasua")){
            firestore.collection("AllSanPham").whereEqualTo("category","trasua").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        String item_id = documentSnapshot.getId();
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class).withId(item_id);

                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        viewAllAdapter.setOnItemCLick(new OnItemClick() {
                            @Override
                            public void onClick(View view, int position) {

                            }

                            @Override
                            public void onUpdate(int position) {
                                final String item_id = viewAllModelList.get(position).itemId;


                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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


                                firestore.collection("AllSanPham").document(item_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ViewAllModel viewAllModel1 = documentSnapshot.toObject(ViewAllModel.class);

                                        String name = viewAllModel1.name;
                                        String price = viewAllModel1.price;
                                        String category = viewAllModel1.category;
                                        String rating = viewAllModel1.rating;
                                        String event = viewAllModel1.event;

                                        editName.setText(name);
                                        editPrice.setText(price);
                                        editCategory.setText(category);
                                        editRating.setText(rating);
                                        editHot.setText(event);

                                        Glide.with(getApplicationContext()).load(viewAllModel1.getImg_url()).into(editImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewAllActivity.this, "Có Lỗi Xảy ra"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                                btnEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (saveUri != null){
                                            ProgressDialog mDialog = new ProgressDialog(ViewAllActivity.this);
                                            mDialog.setMessage("Uploading...");
                                            mDialog.show();

                                            String imageName = UUID.randomUUID().toString();
                                            StorageReference reference = storage.getReference().child("images/"+imageName);
                                            reference.putFile(saveUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            Toast.makeText(ViewAllActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                                    hashMap.put("name",editName.getText().toString());
                                                                    hashMap.put("price",editPrice.getText().toString());
                                                                    hashMap.put("category",editCategory.getText().toString());
                                                                    hashMap.put("rating",editRating.getText().toString());
                                                                    hashMap.put("event",editHot.getText().toString());
                                                                    hashMap.put("img_url",uri.toString());

                                                                    firestore.collection("AllSanPham").document(item_id).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                                            viewAllAdapter.notifyItemChanged(position);
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                            mDialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(ViewAllActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
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
                                final String item_id = viewAllModelList.get(position).itemId;

                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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
                                        firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                                viewAllAdapter.notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        /////Getting trachanh
        if (category != null && category.equalsIgnoreCase("trachanh")){
            firestore.collection("AllSanPham").whereEqualTo("category","trachanh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        String item_id = documentSnapshot.getId();
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class).withId(item_id);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        viewAllAdapter.setOnItemCLick(new OnItemClick() {
                            @Override
                            public void onClick(View view, int position) {

                            }

                            @Override
                            public void onUpdate(int position) {
                                final String item_id = viewAllModelList.get(position).itemId;


                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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


                                firestore.collection("AllSanPham").document(item_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ViewAllModel viewAllModel1 = documentSnapshot.toObject(ViewAllModel.class);

                                        String name = viewAllModel1.name;
                                        String price = viewAllModel1.price;
                                        String category = viewAllModel1.category;
                                        String rating = viewAllModel1.rating;
                                        String event = viewAllModel1.event;

                                        editName.setText(name);
                                        editPrice.setText(price);
                                        editCategory.setText(category);
                                        editRating.setText(rating);
                                        editHot.setText(event);

                                        Glide.with(getApplicationContext()).load(viewAllModel1.getImg_url()).into(editImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewAllActivity.this, "Có Lỗi Xảy ra"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                                btnEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (saveUri != null){
                                            ProgressDialog mDialog = new ProgressDialog(ViewAllActivity.this);
                                            mDialog.setMessage("Uploading...");
                                            mDialog.show();

                                            String imageName = UUID.randomUUID().toString();
                                            StorageReference reference = storage.getReference().child("images/"+imageName);
                                            reference.putFile(saveUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            Toast.makeText(ViewAllActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                                    hashMap.put("name",editName.getText().toString());
                                                                    hashMap.put("price",editPrice.getText().toString());
                                                                    hashMap.put("category",editCategory.getText().toString());
                                                                    hashMap.put("rating",editRating.getText().toString());
                                                                    hashMap.put("event",editHot.getText().toString());
                                                                    hashMap.put("img_url",uri.toString());

                                                                    firestore.collection("AllSanPham").document(item_id).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                                            viewAllAdapter.notifyItemChanged(position);
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                            mDialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(ViewAllActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
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
                                final String item_id = viewAllModelList.get(position).itemId;

                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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
                                        firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                                viewAllAdapter.notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        /////Getting topping
        if (category != null && category.equalsIgnoreCase("topping")){
            firestore.collection("AllSanPham").whereEqualTo("category","topping").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        String item_id = documentSnapshot.getId();
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class).withId(item_id);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        viewAllAdapter.setOnItemCLick(new OnItemClick() {
                            @Override
                            public void onClick(View view, int position) {

                            }

                            @Override
                            public void onUpdate(int position) {
                                final String item_id = viewAllModelList.get(position).itemId;


                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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


                                firestore.collection("AllSanPham").document(item_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ViewAllModel viewAllModel1 = documentSnapshot.toObject(ViewAllModel.class);

                                        String name = viewAllModel1.name;
                                        String price = viewAllModel1.price;
                                        String category = viewAllModel1.category;
                                        String rating = viewAllModel1.rating;
                                        String event = viewAllModel1.event;

                                        editName.setText(name);
                                        editPrice.setText(price);
                                        editCategory.setText(category);
                                        editRating.setText(rating);
                                        editHot.setText(event);

                                        Glide.with(getApplicationContext()).load(viewAllModel1.getImg_url()).into(editImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewAllActivity.this, "Có Lỗi Xảy ra"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                                btnEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (saveUri != null){
                                            ProgressDialog mDialog = new ProgressDialog(ViewAllActivity.this);
                                            mDialog.setMessage("Uploading...");
                                            mDialog.show();

                                            String imageName = UUID.randomUUID().toString();
                                            StorageReference reference = storage.getReference().child("images/"+imageName);
                                            reference.putFile(saveUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            Toast.makeText(ViewAllActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                                    hashMap.put("name",editName.getText().toString());
                                                                    hashMap.put("price",editPrice.getText().toString());
                                                                    hashMap.put("category",editCategory.getText().toString());
                                                                    hashMap.put("rating",editRating.getText().toString());
                                                                    hashMap.put("event",editHot.getText().toString());
                                                                    hashMap.put("img_url",uri.toString());

                                                                    firestore.collection("AllSanPham").document(item_id).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                                            viewAllAdapter.notifyItemChanged(position);
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                            mDialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(ViewAllActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
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
                                final String item_id = viewAllModelList.get(position).itemId;

                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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
                                        firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                                viewAllAdapter.notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        /////Getting  an vat
        if (category != null && category.equalsIgnoreCase("anvat")){
            firestore.collection("AllSanPham").whereEqualTo("category","anvat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        String item_id = documentSnapshot.getId();
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class).withId(item_id);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        viewAllAdapter.setOnItemCLick(new OnItemClick() {
                            @Override
                            public void onClick(View view, int position) {

                            }

                            @Override
                            public void onUpdate(int position) {
                                final String item_id = viewAllModelList.get(position).itemId;


                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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


                                firestore.collection("AllSanPham").document(item_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ViewAllModel viewAllModel1 = documentSnapshot.toObject(ViewAllModel.class);

                                        String name = viewAllModel1.name;
                                        String price = viewAllModel1.price;
                                        String category = viewAllModel1.category;
                                        String rating = viewAllModel1.rating;
                                        String event = viewAllModel1.event;

                                        editName.setText(name);
                                        editPrice.setText(price);
                                        editCategory.setText(category);
                                        editRating.setText(rating);
                                        editHot.setText(event);

                                        Glide.with(getApplicationContext()).load(viewAllModel1.getImg_url()).into(editImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewAllActivity.this, "Có Lỗi Xảy ra"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                                btnEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (saveUri != null){
                                            ProgressDialog mDialog = new ProgressDialog(ViewAllActivity.this);
                                            mDialog.setMessage("Uploading...");
                                            mDialog.show();

                                            String imageName = UUID.randomUUID().toString();
                                            StorageReference reference = storage.getReference().child("images/"+imageName);
                                            reference.putFile(saveUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            Toast.makeText(ViewAllActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                                    hashMap.put("name",editName.getText().toString());
                                                                    hashMap.put("price",editPrice.getText().toString());
                                                                    hashMap.put("category",editCategory.getText().toString());
                                                                    hashMap.put("rating",editRating.getText().toString());
                                                                    hashMap.put("event",editHot.getText().toString());
                                                                    hashMap.put("img_url",uri.toString());

                                                                    firestore.collection("AllSanPham").document(item_id).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                                            viewAllAdapter.notifyItemChanged(position);
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                                            Toast.makeText(ViewAllActivity.this, "Cập nhật thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                            mDialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(ViewAllActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
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
                                final String item_id = viewAllModelList.get(position).itemId;

                                final Dialog dialog = new Dialog(ViewAllActivity.this);
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
                                        firestore.collection("AllSanPham").document(item_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                                viewAllAdapter.notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(ViewAllActivity.this, "Xóa thất bại"+e.getMessage(), Toast.LENGTH_SHORT).show();
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