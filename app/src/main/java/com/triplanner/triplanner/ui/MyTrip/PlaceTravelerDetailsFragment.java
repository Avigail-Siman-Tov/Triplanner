package com.triplanner.triplanner.ui.MyTrip;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

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
        placeName=view.findViewById(R.id.fragment_place_traveler_details_place_name);
        placeAddress=view.findViewById(R.id.fragment_place_traveler_details_place_address);
        destination= PlaceTravelerDetailsFragmentArgs.fromBundle(getArguments()).getTripDestination();
        placeOpeningHours=view.findViewById(R.id.fragment_place_traveler_details_place_opening_hours);
        placeWebsite=view.findViewById(R.id.fragment_place_traveler_details_place_website);
        ratingBar=view.findViewById(R.id.fragment_place_traveler_details_place_rating);
        placeImg=view.findViewById(R.id.fragment_place_traveler_details_image);
        placePhone = view.findViewById(R.id.fragment_place_traveler_details_place_phone);
        placeName.setText(place.getPlaceName());
        placeAddress.setText(place.getPlaceFormattedAddress());
        myLoadingDialog=new ProgressDialog(getContext());
        Model.instance.getOpenHoursOfPlace(place.getPlaceID(), getContext(), new Model.GetOpenHoursOfPlaceListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(List<String> result) {

                String openingHours = result.stream().map(n -> String.valueOf(n))
                        .collect(Collectors.joining("\n", "", ""));
                placeOpeningHours.setText(openingHours);
            }
        });
        String weblink = place.getPlaceWebsite();
        if(weblink!=null && weblink!="") {
            placeWebsite.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='"+weblink+"'> Link </a>";
            placeWebsite.setText(Html.fromHtml(text));
        }
        return view;
    }


    public PlaceTravelerDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}