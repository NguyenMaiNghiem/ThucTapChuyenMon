package com.example.trasuaserver1;

import androidx.annotation.NonNull;

public class ItemId {
    public String itemId;
    public <T extends ItemId> T withId(@NonNull final String id){
        this.itemId = id;
        return (T) this;
    }
}
