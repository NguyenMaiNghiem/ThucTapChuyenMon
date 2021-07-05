package com.example.trasuaserver1.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.trasuaserver1.Remote.IGeoCoordinates;
import com.example.trasuaserver1.Remote.RetrofitClient;
import com.example.trasuaserver1.models.OrdersModel;
import com.example.trasuaserver1.models.Request;
import com.example.trasuaserver1.models.UserModel;

public class Common {
    public static UserModel currentUser;
    public static OrdersModel currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static String convertCodeToStatus(String code)
    {
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }

    public static IGeoCoordinates getGeoCodeService()
    {
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth,int newHeight)
    {
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX = 0 , pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }
}
