package com.triplanner.triplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.bson.types.ObjectId;

@Entity
public class Place implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String placeID;
    private String placeName;
    private double placeLocationLat;
    private double placeLocationLng;
    private String placeFormattedAddress;
    private String placeInternationalPhoneNumber;
    private float placeRating;
    private String placeWebsite;
    private String placeImgUrl;
    private int day_in_trip;
    private String travelerMail;
    private String id_trip;
    private float travelerRating;

    @Ignore
    public Place(){}
    public Place(String placeID, String placeName, double placeLocationLat, double placeLocationLng, String placeFormattedAddress, String placeInternationalPhoneNumber, float placeRating, String placeWebsite, String placeImgUrl,int day_in_trip,String travelerMail,String id_trip){
        ObjectId _id= new ObjectId();
        this.id =_id.toString();
        this.placeID=placeID;
        this.placeName=placeName;
        this.placeLocationLat=placeLocationLat;
        this.placeLocationLng=placeLocationLng;
        this.placeFormattedAddress=placeFormattedAddress;
        this.placeInternationalPhoneNumber= placeInternationalPhoneNumber;
        this.placeRating=placeRating;
        this.placeWebsite=placeWebsite;
        this.placeImgUrl=placeImgUrl;
        this.day_in_trip=day_in_trip;
        this.travelerMail=travelerMail;
        this.id_trip=id_trip;
        this.travelerRating=0;
    }

    protected Place(Parcel in) {
        id = in.readString();
        placeID = in.readString();
        placeName = in.readString();
        placeLocationLat = in.readDouble();
        placeLocationLng = in.readDouble();
        placeFormattedAddress = in.readString();
        placeInternationalPhoneNumber = in.readString();
        placeRating = in.readFloat();
        placeWebsite = in.readString();
        placeImgUrl = in.readString();
        day_in_trip = in.readInt();
        travelerMail = in.readString();
        id_trip = in.readString();
        travelerRating = in.readFloat();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getPlaceLocationLat() {
        return placeLocationLat;
    }

    public void setPlaceLocationLat(double placeLocationLat) {
        this.placeLocationLat = placeLocationLat;
    }

    public double getPlaceLocationLng() {
        return placeLocationLng;
    }

    public void setPlaceLocationLng(double placeLocationLng) {
        this.placeLocationLng = placeLocationLng;
    }

    public String getPlaceFormattedAddress() {
        return placeFormattedAddress;
    }

    public void setPlaceFormattedAddress(String placeFormattedAddress) {
        this.placeFormattedAddress = placeFormattedAddress;
    }

    public String getPlaceInternationalPhoneNumber() {
        return placeInternationalPhoneNumber;
    }

    public void setPlaceInternationalPhoneNumber(String placeInternationalPhoneNumber) {
        this.placeInternationalPhoneNumber = placeInternationalPhoneNumber;
    }

    public int getDay_in_trip() {
        return day_in_trip;
    }

    public void setDay_in_trip(int day_in_trip) {
        this.day_in_trip = day_in_trip;
    }

    public float getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(float placeRating) {
        this.placeRating = placeRating;
    }

    public float getTravelerRating() {
        return travelerRating;
    }

    public void setTravelerRating(float travelerRating) {
        this.travelerRating = travelerRating;
    }

    public String getId_trip() {
        return id_trip;
    }

    public void setId_trip(String id_trip) {
        this.id_trip = id_trip;
    }

    public String getTravelerMail() {
        return travelerMail;
    }

    public void setTravelerMail(String travelerMail) {
        this.travelerMail = travelerMail;
    }

    public String getPlaceImgUrl() {
        return placeImgUrl;
    }

    public void setPlaceImgUrl(String placeImgUrl) {
        this.placeImgUrl = placeImgUrl;
    }

    public String getPlaceWebsite() {
        return placeWebsite;
    }

    public void setPlaceWebsite(String placeWebsite) {
        this.placeWebsite = placeWebsite;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(placeID);
        dest.writeString(placeName);
        dest.writeDouble(placeLocationLat);
        dest.writeDouble(placeLocationLng);
        dest.writeString(placeFormattedAddress);
        dest.writeString(placeInternationalPhoneNumber);
        dest.writeFloat(placeRating);
        dest.writeString(placeWebsite);
        dest.writeString(placeImgUrl);
        dest.writeInt(day_in_trip);
        dest.writeString(travelerMail);
        dest.writeString(id_trip);
        dest.writeFloat(travelerRating);
    }
}
