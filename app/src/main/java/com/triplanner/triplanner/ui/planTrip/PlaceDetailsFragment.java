package com.triplanner.triplanner.ui.planTrip;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class PlaceDetailsFragment extends Fragment {
    PlacePlanning placePlanning,placeFullDetails;
    ImageView placeImg;
    TextView placeName,placeAddress,placeOpeningHours,placeWebsite,placePhone;
    RatingBar ratingBar;
    double placeRating;
    Button addBtn;
    JSONObject jsonData=null;
    String jsonStringPlace;

    int[] colorArray;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_place_details, container, false);
        placePlanning=PlaceDetailsFragmentArgs.fromBundle(getArguments()).getPlace();

        placeName=view.findViewById(R.id.fragment_place_details_place_name);
        placeAddress=view.findViewById(R.id.fragment_place_details_place_address);
        placeOpeningHours=view.findViewById(R.id.fragment_place_details_place_opening_hours);
        placeWebsite=view.findViewById(R.id.fragment_place_details_place_website);
        ratingBar=view.findViewById(R.id.fragment_place_details_place_rating);
        placeImg=view.findViewById(R.id.fragment_place_details_image);
        addBtn= view.findViewById(R.id.fragment_place_details_btn_add_place_btn);
        placePhone = view.findViewById(R.id.fragment_place_details_place_phone);
        String placeId=placePlanning.getPlaceID();
        placeFullDetails=getPlaceDetailsById(placeId);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        colorArray= getContext().getResources().getIntArray(R.array.array_name);
        placeName.setText(placeFullDetails.getPlaceName());
        placeAddress.setText(placeFullDetails.getPlaceFormattedAddress());
        String openingHours = placeFullDetails.getPlaceOpeningHours().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining("\n", "", ""));
        placeOpeningHours.setText(openingHours);
        String weblink = placeFullDetails.getPlaceWebsite();
        if(weblink!=null && weblink!="") {
            placeWebsite.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='"+weblink+"'> Link </a>";
            placeWebsite.setText(Html.fromHtml(text));
        }
        String phone= placeFullDetails.getPlaceInternationalPhoneNumber();
        if(phone!=null && phone!=""){
            placePhone.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href=\"tel:"+phone+"\""+">"+phone+"</a>";
            placePhone.setText(Html.fromHtml(text));
        }
        placeRating=placeFullDetails.getPlaceRating();
        ratingBar.setRating((float)placeRating);
        placeImg.setTag(placeFullDetails.getPlaceImgUrl());

        if (placeFullDetails.getPlaceImgUrl() != null && !placeFullDetails.getPlaceImgUrl().equals("")) {
            if (placeFullDetails.getPlaceImgUrl() == placeImg.getTag()) {
                Picasso.get().load(placeFullDetails.getPlaceImgUrl()).into(placeImg);
            }
        }

        return view;
    }

    private PlacePlanning getPlaceDetailsById(String placeId) {
        PlacePlanning p=new PlacePlanning();
        Thread placeThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url="https://maps.googleapis.com/maps/api/place/details/json?place_id=";
                    url+=placeId+"&key="+getString(R.string.places_api_key);
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .method("GET", null)
                            .build();
                    Response response = client.newCall(request).execute();
                    jsonStringPlace= response.body().string();
                    jsonData = new JSONObject(jsonStringPlace);
                }catch (IOException | JSONException f){
                }
            }
        });
        placeThread.start();
        try {
            placeThread.join();
            if(jsonData!=null)
            {
                JSONObject result=(JSONObject)jsonData.get("result");
                p=PlacesList.JsonToPlace(result);
            }
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return p;
    }
}