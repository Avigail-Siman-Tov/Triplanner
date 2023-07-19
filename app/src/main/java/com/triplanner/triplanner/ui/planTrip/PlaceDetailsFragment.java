package com.triplanner.triplanner.ui.planTrip;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class PlaceDetailsFragment extends Fragment {

    private static final String WAZE_NOTE_URL = "https://waze.com/ul?q=%s";
    PlacePlanning placePlanning,placeFullDetails;
    ImageView placeImg;
    TextView placeName,placeAddress,placeOpeningHours,placeWebsite,placePhone;
    RatingBar ratingBar;
    double placeRating;
    Button addBtn ,noteBtn ,moovitBtn;
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
        noteBtn = view.findViewById(R.id.btn_waze);
        moovitBtn = view.findViewById(R.id.btn_moovit);
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

        if(!placePlanning.getStatus()){
            addBtn.setText("Add");
            addBtn.setBackgroundColor(colorArray[1]);
        }
        else{
            addBtn.setText("Remove");
            addBtn.setBackgroundColor(colorArray[0]);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeChosen();
            }
        });

        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = ""+placeName.getText(); // Replace with the actual place you want to note
                String url = String.format(WAZE_NOTE_URL, Uri.encode(place));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        moovitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tripName = ""+placeName.getText();
                openMoovitWithTripName(tripName);
            }
        });


        return view;
    }


    public void openMoovitWithTripName(final String tripName) {
        AsyncTask<Void, Void, String> geocodingTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String apiKey = "AIzaSyD5iAAUo7k43rJzBeOhfx8fDO1qV-Zm7Lw";
                String geocodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + tripName + "&key=" + apiKey;
                try {
                    URL url = new URL(geocodingUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String jsonContent) {
                if (jsonContent != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        double destLat = location.getDouble("lat");
                        double destLon = location.getDouble("lng");
                        String uri = "moovit://directions?dest_lat=" + destLat + "&dest_lon=" + destLon + "&dest_name=" + tripName;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case when the geocoding data couldn't be fetched properly.
                    Log.e("MyFragment", "Error fetching geocoding data for tripName: " + tripName);
                }
            }
        };

        geocodingTask.execute();
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

    private void placeChosen() {
        if(!placePlanning.getStatus()){
            placePlanning.setStatus(true);
            addBtn.setText("Remove");
            addBtn.setBackgroundColor(colorArray[0]);
        }
        else{
            placePlanning.setStatus(false);
            addBtn.setText("Add");
            addBtn.setBackgroundColor(colorArray[1]);
        }
        addBtn.setTag(placePlanning.getStatus());
    }


}