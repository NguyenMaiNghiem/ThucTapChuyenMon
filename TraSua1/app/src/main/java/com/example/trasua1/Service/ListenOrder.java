package com.example.trasua1.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.trasua1.models.MyCardModel;
import com.example.trasua1.ui.giohang.GioHangFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference requests;


    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Order");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
        //Trigger here
        MyCardModel myCardModel = snapshot.getValue(MyCardModel.class);
        showNotification(snapshot.getKey(),myCardModel);
    }

    private void showNotification(String key, MyCardModel myCardModel) {
        Intent intent = new Intent(getBaseContext(), GioHangFragment.class);
        intent.putExtra("userPhone",myCardModel.getDocumentId());
//        PendingIntent contentIntent = PendingIntent.getActivities(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("TraSuaRilley")
                .setContentInfo("Your oder was updated")
                .setContentText("Order #"+key+ " was update status to");
    }

    @Override
    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
}