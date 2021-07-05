package com.example.trasua1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trasua1.R;
import com.example.trasua1.models.MyCardModel;
import com.example.trasua1.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOderActivity extends AppCompatActivity {
    Button buymore;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    String userName,userPhone,userAddress,userID;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_oder);

        FirebaseMessaging.getInstance().subscribeToTopic("client");


        buymore = findViewById(R.id.buymore);
        buymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlacedOderActivity.this,HomeActivity.class));
            }
        });


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trasua1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        if (user != null){
            userID = user.getUid();
            if (userID != null){
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserModel userProfile = snapshot.getValue(UserModel.class);

//                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//                            @Override
//                            public void onComplete(@NonNull @NotNull Task<String> task) {
//                                if (!task.isSuccessful()) {
//                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
//                                    return;
//                                }
//
//                                // Get new FCM registration token
//                                String id_token = task.getResult();
//                                Log.d("qqqqqqqqqq","token : "+id_token);
//
////                            }
////                        });
//
                        user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                            @Override
                            public void onSuccess(GetTokenResult getTokenResult) {
                                String id_token = getTokenResult.getToken();

                                Log.d("qqqqqqqqq","id_token : "+id_token);

                                if (userProfile != null){
                                    userName = userProfile.name;
                                    userPhone = userProfile.phone;
                                    userAddress = userProfile.address;

                                    List<MyCardModel> list = (ArrayList<MyCardModel>) getIntent().getSerializableExtra("itemList");

                                    if (list != null && list.size() > 0 ){
                                        for (MyCardModel model : list){

                                            final HashMap<String,Object> cartMap = new HashMap<>();

                                            cartMap.put("token_id",id_token);
                                            cartMap.put("userName",userName);
                                            cartMap.put("userPhone",userPhone);
                                            cartMap.put("userAddress",userAddress);
                                            cartMap.put("productName",model.getProductName());
                                            cartMap.put("productPrice",model.getProductPrice());
                                            cartMap.put("currentDate",model.getCurrentDate());
                                            cartMap.put("currentTime",model.getCurrentTime());
                                            cartMap.put("totalQuantity",model.getTotalQuantity());
                                            cartMap.put("totalPrice",model.getTotalPrice());

                                            firestore.collection("Order").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                                    Toast.makeText(PlacedOderActivity.this, "Đơn hàng của bạn đã được đặt thành công !", Toast.LENGTH_SHORT).show();

                                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/server",
                                                            "Notification","Have a new Order",getApplicationContext(),PlacedOderActivity.this);
                                                    notificationsSender.SendNotifications();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });


                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(PlacedOderActivity.this,"Có lỗi xảy ra !!! ",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}