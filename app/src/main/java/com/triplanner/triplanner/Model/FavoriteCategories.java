package com.triplanner.triplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class FavoriteCategories implements Parcelable {
    @PrimaryKey
    @NotNull
    private  String category;
    private String travelerMail;

    public FavoriteCategories(String category, String travelerMail){
        this.category=category;
        this.travelerMail= travelerMail;
    }

    protected FavoriteCategories(Parcel in) {
        category = in.readString();
        travelerMail = in.readString();
    }

    public static final Creator<FavoriteCategories> CREATOR = new Creator<FavoriteCategories>() {
        @Override
        public FavoriteCategories createFromParcel(Parcel in) {
            return new FavoriteCategories(in);
        }

        @Override
        public FavoriteCategories[] newArray(int size) {
            return new FavoriteCategories[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTravelerMail() {
        return travelerMail;
    }

    public void setTravelerMail(String travelerMail) {
        this.travelerMail = travelerMail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(travelerMail);
    }
}
