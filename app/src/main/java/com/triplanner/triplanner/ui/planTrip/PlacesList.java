package com.triplanner.triplanner.ui.planTrip;
import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PlacesList {
    public static final String api_key_place = "AIzaSyC-2lRMHs-8VTX-pePcej_Lsb807VOxk8U";

    public static List<PlacePlanning> JsonArrayToListPlace(JSONArray arrayPlace) throws JSONException {
        List<PlacePlanning> myList = new ArrayList<PlacePlanning>();
        final String url = "https://maps.googleapis.com/maps/api/place/photo?photo_reference=";

        for (int i = 0; i < arrayPlace.length(); ++i) {
            String placeID = null;
            String placeName = "", placeFormattedAddress = "", placeInternationalPhoneNumber = "", placeWebsite = "", placeImgUrl = "";
            double placeLocationLat = 0, placeLocationLng = 0;
            float placeRating = 0;
            boolean status = false;
            List<String> placeOpeningHours = null;
            JSONObject place = (JSONObject) arrayPlace.get(i);
            if (place.has("name")) {
                placeName = place.get("name").toString();
            }
            if (place.has("formatted_address")) {
                placeFormattedAddress = place.get("formatted_address").toString();
            }
            if (place.has("geometry")) {
                JSONObject geometry = (JSONObject) place.get("geometry");
                if (geometry.has("location")) {
                    JSONObject location = (JSONObject) geometry.get("location");
                    if (location.has("lat") && location.has("lng")) {
                        placeLocationLat = location.getDouble("lat");
                        placeLocationLng = location.getDouble("lng");
                    }
                }
            }
            if (place.has("place_id")) {
                placeID = place.get("place_id").toString();
            }
            if (place.has("photos")) {
                JSONArray photos = (JSONArray) place.get("photos");

                if (photos.length() > 0) {
                    JSONObject photo = (JSONObject) photos.get(0);
                    if (photo.has("photo_reference") && photo.has("width") && photo.has("height")) {
                        String myUrl = url + photo.get("photo_reference").toString() + "&maxwidth=" + photo.get("width").toString() + "&maxheight=" + photo.get("height").toString() + "&key=" + api_key_place;
                        placeImgUrl = myUrl;
                    }
                }

            }
            if (place.has("rating")) {
                placeRating = (float) place.getDouble("rating");
            }
            if(place.has("international_phone_number")){
                placeInternationalPhoneNumber = place.get("international_phone_number").toString();
            }
            if (placeLocationLat == 0 && placeLocationLng == 0) {
                System.out.println("Error");
            } else {
                PlacePlanning myPlace = new PlacePlanning(placeID, placeName, placeLocationLat, placeLocationLng, placeFormattedAddress, placeInternationalPhoneNumber, placeOpeningHours, placeRating, placeWebsite, placeImgUrl, status);
                myList.add(myPlace);
            }

        }

        return myList;
    }


    public static PlacePlanning JsonToPlace(JSONObject place) throws JSONException {
        PlacePlanning placePlanning = new PlacePlanning();
        final String url = "https://maps.googleapis.com/maps/api/place/photo?photo_reference=";
        String placeID = null;
        String placeName = "", placeFormattedAddress = "", placeInternationalPhoneNumber = "", placeWebsite = "", placeImgUrl = "";
        double placeLocationLat = 0, placeLocationLng = 0;
        float placeRating = 0;
        boolean status = false;
        List<String> placeOpeningHours = new ArrayList<String>();

        if (place.has("name")) {
            placeName = place.get("name").toString();
        }
        if (place.has("formatted_address")) {
            placeFormattedAddress = place.get("formatted_address").toString();
        }
        if (place.has("international_phone_number")) {
            placeInternationalPhoneNumber = place.get("international_phone_number").toString();
        }
        if (place.has("website")) {
            placeWebsite = place.get("website").toString();
        }
        if (place.has("geometry")) {
            JSONObject geometry = (JSONObject) place.get("geometry");
            if (geometry.has("location")) {
                JSONObject location = (JSONObject) geometry.get("location");
                if (location.has("lat") && location.has("lng")) {
                    placeLocationLat = location.getDouble("lat");
                    placeLocationLng = location.getDouble("lng");
                }
            }
        }
        if (place.has("place_id")) {
            placeID = place.get("place_id").toString();
        }
        if (place.has("photos")) {
            JSONArray photos = (JSONArray) place.get("photos");
            if (photos.length() > 0) {
                JSONObject photo = (JSONObject) photos.get(0);
                if (photo.has("photo_reference") && photo.has("width") && photo.has("height")) {
                    String myUrl = url + photo.get("photo_reference").toString() + "&maxwidth=" + photo.get("width").toString() + "&maxheight=" + photo.get("height").toString() + "&key=" + api_key_place;
                    placeImgUrl = myUrl;
                }
            }
        }
        if (place.has("rating")) {
            placeRating = (float) place.getDouble("rating");
        }
        if (placeLocationLat == 0 && placeLocationLng == 0) {
            System.out.println("Error");
        }
        if (place.has("opening_hours")) {
            JSONObject openingHours = (JSONObject) place.get("opening_hours");
            if (openingHours.has("weekday_text")) {
                JSONArray weekdayText = (JSONArray) openingHours.get("weekday_text");
                for (int i = 0; i < weekdayText.length(); ++i) {
                    placeOpeningHours.add((String) weekdayText.get(i));
                }
            }
            placeRating = (float) place.getDouble("rating");
        }
        placePlanning = new PlacePlanning(placeID, placeName, placeLocationLat, placeLocationLng, placeFormattedAddress, placeInternationalPhoneNumber, placeOpeningHours, placeRating, placeWebsite, placeImgUrl, status);

        return placePlanning;
    }
    JSONObject jsonData=null;
    String jsonStringPlace;
    public  PlacePlanning getPlaceDetailsById(String placeId) {
        PlacePlanning p=new PlacePlanning();
        Thread placeThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url="https://maps.googleapis.com/maps/api/place/details/json?place_id=";
                    url+=placeId+"&key="+api_key_place;
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





