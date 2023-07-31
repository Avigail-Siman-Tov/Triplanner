package com.triplanner.triplanner.Model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PlacePlanning extends PlaceDetails implements Parcelable {
    private boolean status;

    public PlacePlanning() {
    }

    public PlacePlanning(String placeID, String placeName, double placeLocationLat, double placeLocationLng, String placeFormattedAddress, String placeInternationalPhoneNumber, List<String> placeOpeningHours, float placeRating, String placeWebsite, String placeImgUrl, boolean status) {
        super(placeID, placeName, placeLocationLat, placeLocationLng, placeFormattedAddress, placeInternationalPhoneNumber,  placeOpeningHours, placeRating,  placeWebsite,  placeImgUrl);
        this.status=status;
    }


    protected PlacePlanning(Parcel in) {
        status = in.readByte() != 0;
    }


    public static final Creator<PlacePlanning> CREATOR = new Creator<PlacePlanning>() {
        @Override
        public PlacePlanning createFromParcel(Parcel in) {
            return new PlacePlanning(in);
        }

        @Override
        public PlacePlanning[] newArray(int size) {
            return new PlacePlanning[size];
        }
    };

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}