package com.triplanner.triplanner.Model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PlacePlanning extends PlaceDetails implements Parcelable {
    private boolean status;
    private int day_in_trip;
    private String isRecommended;

    public PlacePlanning() {
    }

    public PlacePlanning(String placeID, String placeName, double placeLocationLat, double placeLocationLng, String placeFormattedAddress, String placeInternationalPhoneNumber, List<String> placeOpeningHours, float placeRating, String placeWebsite, String placeImgUrl, boolean status) {
        super(placeID, placeName, placeLocationLat, placeLocationLng, placeFormattedAddress, placeInternationalPhoneNumber,  placeOpeningHours, placeRating,  placeWebsite,  placeImgUrl);
        this.status=status;
        this.day_in_trip = 0;
        this.isRecommended="0";
    }

    protected PlacePlanning(Parcel in) {
        status = in.readByte() != 0;
        day_in_trip = in.readInt();
        isRecommended = in.readString();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(day_in_trip);
        dest.writeString(isRecommended);
    }
    public int getDay_in_trip() {
        return day_in_trip;
    }

    public void setDay_in_trip(int day_in_trip) {
        this.day_in_trip = day_in_trip;
    }

    public String isRecommended() {
        return isRecommended;
    }

    public void setRecommended(String recommended) {
        isRecommended = recommended;
    }
}