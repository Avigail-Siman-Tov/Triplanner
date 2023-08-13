package com.triplanner.triplanner.ui.MyTrip;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

import org.json.JSONObject;

public class PlaceTravelerDetailsFragment extends Fragment {
    Place place;
    ImageView placeImg;
    TextView placeName,placeAddress,placeOpeningHours,placeWebsite,placePhone,editRating;
    RatingBar ratingBar,ratingBarTraveler;
    double placeRating,placeTravelerRating;
    JSONObject jsonData=null;
    String jsonStringPlace;
    String destination;
    ProgressDialog myLoadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_place_traveler_details, container, false);
        place = PlaceTravelerDetailsFragmentArgs.fromBundle(getArguments()).getPlace();
        return view;
    }


    public PlaceTravelerDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}