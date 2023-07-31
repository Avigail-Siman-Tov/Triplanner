package com.triplanner.triplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.realm.RealmList;

public class PlaceDetails implements Parcelable{
    private String placeID;
    private String placeName;
    private double placeLocationLat;
    private double placeLocationLng;
    private String placeFormattedAddress;
    private String placeInternationalPhoneNumber;
    private List<String> placeOpeningHours;
    private float placeRating;
    private String placeWebsite;
    private String placeImgUrl;

    public PlaceDetails() {
    }

    public PlaceDetails(String placeID, String placeName, double placeLocationLat, double placeLocationLng, String placeFormattedAddress, String placeInternationalPhoneNumber, List<String> placeOpeningHours, float placeRating, String placeWebsite, String placeImgUrl) {
        this.placeID = placeID;
        this.placeName = placeName;
        this.placeLocationLat = placeLocationLat;
        this.placeLocationLng = placeLocationLng;
        this.placeFormattedAddress = placeFormattedAddress;
        this.placeInternationalPhoneNumber = placeInternationalPhoneNumber;
        this.placeOpeningHours = placeOpeningHours;
        this.placeRating = placeRating;
        this.placeWebsite = placeWebsite;
        this.placeImgUrl = placeImgUrl;
    }


    protected PlaceDetails(Parcel in) {
        placeID = in.readString();
        placeName = in.readString();
        placeLocationLat = in.readDouble();
        placeLocationLng = in.readDouble();
        placeFormattedAddress = in.readString();
        placeInternationalPhoneNumber = in.readString();
        placeOpeningHours = in.createStringArrayList();
        placeRating = in.readFloat();
        placeWebsite = in.readString();
        placeImgUrl = in.readString();
    }

    public static final Creator<PlaceDetails> CREATOR = new Creator<PlaceDetails>() {
        @Override
        public PlaceDetails createFromParcel(Parcel in) {
            return new PlaceDetails(in);
        }

        @Override
        public PlaceDetails[] newArray(int size) {
            return new PlaceDetails[size];
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

    public List<String> getPlaceOpeningHours() {
        return placeOpeningHours;
    }

    public void setPlaceOpeningHours(RealmList<String> placeOpeningHours) {
        this.placeOpeningHours = placeOpeningHours;
    }

    public float getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(float placeRating) {
        this.placeRating = placeRating;
    }

    public String getPlaceWebsite() {
        return placeWebsite;
    }

    public void setPlaceWebsite(String placeWebsite) {
        this.placeWebsite = placeWebsite;
    }

    public String getPlaceImgUrl() {
        return placeImgUrl;
    }

    public void setPlaceImgUrl(String placeImgUrl) {
        this.placeImgUrl = placeImgUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeID);
        dest.writeString(placeName);
        dest.writeDouble(placeLocationLat);
        dest.writeDouble(placeLocationLng);
        dest.writeString(placeFormattedAddress);
        dest.writeString(placeInternationalPhoneNumber);
        dest.writeStringList(placeOpeningHours);
        dest.writeFloat(placeRating);
        dest.writeString(placeWebsite);
        dest.writeString(placeImgUrl);
    }
}
