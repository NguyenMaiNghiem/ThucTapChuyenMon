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
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trasuaserver1.R;
import com.example.trasuaserver1.models.ViewAllModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trasuaserver1.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    ImageView imgItemNew;
    EditText edtItemName,edtItemPrice,edtItemCategory,edtItemRating,edtItemHot;
    Uri saveUri;

    FirebaseFirestore firestore;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference newItem;

    ViewAllModel viewAllModel;
    DrawerLayout drawer;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        newItem = database.getReference("AllSanPham");

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogItemNew(Gravity.CENTER);
            }
        });

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_trangchu, R.id.nav_hoso, R.id.nav_theloai, R.id.nav_orders, R.id.nav_dangxuat)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            imgItemNew.setImageURI(saveUri);
        }
    }

    private void openDialogItemNew(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

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

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        imgItemNew = dialog.findViewById(R.id.imgItemNew);

        edtItemName = dialog.findViewById(R.id.edtItemName);
        edtItemPrice = dialog.findViewById(R.id.edtItemPrice);
        edtItemCategory = dialog.findViewById(R.id.edtItemCategory);
        edtItemRating = dialog.findViewById(R.id.edtItemRating);
        edtItemHot = dialog.findViewById(R.id.edtItemHot);


        imgItemNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage();
                Toast.makeText(HomeActivity.this, "Thêm thành công !", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void upLoadImage() {
        if (saveUri != null){
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            StorageReference reference = storage.getReference().child("images/");
            reference.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(HomeActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    viewAllModel = new ViewAllModel(edtItemName.getText().toString(),edtItemPrice.getText().toString(),edtItemCategory.getText().toString(),edtItemRating.getText().toString(),edtItemHot.getText().toString(),uri.toString());

                                    firestore.collection("AllSanPham").add(viewAllModel);

                                    Snackbar.make(drawer,"San Pham "+viewAllModel.getName()+" đã được thêm vào !",Snackbar.LENGTH_LONG).show();
                                }
                            });
                            mDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}