package com.triplanner.triplanner.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.bson.types.ObjectId;

@Entity
public class OpenHours {
    @PrimaryKey
    @NonNull
    private String id;

    private String open;
    private  String place_id;

    @Ignore
    public OpenHours(){

    }
    public OpenHours(String open, String place_id){
        ObjectId _id=new ObjectId();
        this.id = _id.toString();
        this.open=open;
        this.place_id=place_id;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
