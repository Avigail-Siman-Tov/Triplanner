package com.triplanner.triplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Traveler implements Parcelable {
    @PrimaryKey
    @NonNull
    private String travelerMail;
    private Integer travelerBirthYear;
    private String travelerGender;
    private String travelerName;

    @Ignore
    public Traveler(){ }

    public Traveler(String travelerMail,String travelerName, int travelerBirthYear, String travelerGender) {
        this.travelerName = travelerName;
        this.travelerBirthYear = travelerBirthYear;
        this.travelerGender = travelerGender;
        this.travelerMail=travelerMail;
    }
    // Standard getters & setters

    protected Traveler(Parcel in) {
        travelerMail = in.readString();
        if (in.readByte() == 0) {
            travelerBirthYear = null;
        } else {
            travelerBirthYear = in.readInt();
        }
        travelerGender = in.readString();
        travelerName = in.readString();
    }

    public static final Creator<Traveler> CREATOR = new Creator<Traveler>() {
        @Override
        public Traveler createFromParcel(Parcel in) {
            return new Traveler(in);
        }

        @Override
        public Traveler[] newArray(int size) {
            return new Traveler[size];
        }
    };

    public Integer getTravelerBirthYear() { return travelerBirthYear; }
    public void setTravelerBirthYear(Integer travelerBirthYear) { this.travelerBirthYear = travelerBirthYear; }
    public String getTravelerGender() { return travelerGender; }
    public void setTravelerGender(String travelerGender) { this.travelerGender = travelerGender; }
    public String getTravelerMail() { return travelerMail; }
    public void setTravelerMail(String travelerMail) { this.travelerMail = travelerMail; }
    public String getTravelerName() { return travelerName; }
    public void setTravelerName(String travelerName) { this.travelerName = travelerName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(travelerMail);
        if (travelerBirthYear == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(travelerBirthYear);
        }
        dest.writeString(travelerGender);
        dest.writeString(travelerName);
    }
}

