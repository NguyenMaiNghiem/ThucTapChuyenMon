package com.example.trasuaserver1;

import android.view.View;

public interface OnItemClick {
    void onClick(View view, int position);
    void onUpdate(int position);
    void onDelete(int position);
}
