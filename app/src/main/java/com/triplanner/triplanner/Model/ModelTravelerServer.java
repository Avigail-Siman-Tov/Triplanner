package com.triplanner.triplanner.Model;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.triplanner.triplanner.HTTP.HttpCall;
import com.triplanner.triplanner.HTTP.HttpRequest;
import com.triplanner.triplanner.Model.FavoriteCategories;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.ModelTravelerSQL;
import com.triplanner.triplanner.Model.OpenHours;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.Model.Traveler;
import com.triplanner.triplanner.Model.Trip;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ModelTravelerServer {
    private ModelTravelerSQL travelerModelSQL=new ModelTravelerSQL();
    public void addTraveler(Traveler traveler, List<FavoriteCategories>listFavoriteCategories, Context context, Model.AddTravelerListener listener) {
        final String URL_ADD_TRIP = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/traveler/addtraveler";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_ADD_TRIP);
        HashMap<String, String> paramsTraveler = new HashMap<>();
        paramsTraveler.put("travelerMail",traveler.getTravelerMail());
        paramsTraveler.put("travelerName",traveler.getTravelerName());
        paramsTraveler.put("travelerBirthYear", String.valueOf(traveler.getTravelerBirthYear()));
        paramsTraveler.put("travelerGender",traveler.getTravelerGender());
        for (int i = 0; i < listFavoriteCategories.size() ; ++i) {
            paramsTraveler.put("travelerFavoriteCategories" + "[" + i + "]", listFavoriteCategories.get(i).getCategory());
        }
        httpCallPost.setParams(paramsTraveler);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                try {
                    travelerModelSQL.addTraveler(traveler, listFavoriteCategories, context, new Model.AddTravelerListener() {
                        @Override
                        public void onComplete(String isSuccess) {

                        }
                    });
                    listener.onComplete(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.execute(httpCallPost);

    }
    public void getTraveler(String travelerMail, Context context, Model.GetTravelerByEmailListener listener){
        final String URL_GET_TRAVELER = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/traveler/getinfotraveler";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_GET_TRAVELER);
        HashMap<String, String> paramsTraveler = new HashMap<>();
        paramsTraveler.put("travelerMail",travelerMail);
        httpCallPost.setParams(paramsTraveler);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                try {
                    JSONObject  jsonTraveler = new JSONObject(result);
                    Traveler traveler= new Traveler(jsonTraveler.get("travelerMail").toString(),jsonTraveler.get("travelerName").toString(),Integer.valueOf(jsonTraveler.get("travelerBirthYear").toString()),jsonTraveler.get("travelerGender").toString());
                    List<FavoriteCategories>listFavoriteCategories= new ArrayList<FavoriteCategories>();
                    String str = jsonTraveler.get("travelerFavoriteCategories").toString();
                    str = str.substring(1,str.length()-1);
                    String[] travelerFavoriteCategories = str.split(",");
                    for(int i=0 ;i< travelerFavoriteCategories.length;++i){
                        travelerFavoriteCategories[i] = travelerFavoriteCategories[i].substring(1, travelerFavoriteCategories[i].length() - 1);
                    }
                    for(int i=0; i<travelerFavoriteCategories.length;++i){
                        listFavoriteCategories.add(new FavoriteCategories(travelerFavoriteCategories[i],jsonTraveler.get("travelerMail").toString()));
                    }
                    travelerModelSQL.addTraveler(traveler, listFavoriteCategories, context, new Model.AddTravelerListener() {
                        @Override
                        public void onComplete(String isSuccess) {

                        }
                    });
                    listener.onComplete(traveler, Arrays.asList(travelerFavoriteCategories));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute(httpCallPost);

    }
    public void editTraveler(Traveler traveler, List<FavoriteCategories>listFavoriteCategories, Context context, Model.EditTravelerListener listener) {
        final String URL_EDIT_TRAVELER = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/traveler/editTraveler";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_EDIT_TRAVELER);
        HashMap<String, String> paramsTraveler = new HashMap<>();
        paramsTraveler.put("travelerMail",traveler.getTravelerMail());
        paramsTraveler.put("travelerName",traveler.getTravelerName());
        paramsTraveler.put("travelerBirthYear", String.valueOf(traveler.getTravelerBirthYear()));
        paramsTraveler.put("travelerGender",traveler.getTravelerGender());
        for (int i = 0; i < listFavoriteCategories.size() ; ++i) {
            paramsTraveler.put("travelerFavoriteCategories" + "[" + i + "]", listFavoriteCategories.get(i).getCategory());
        }
        httpCallPost.setParams(paramsTraveler);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                try {
                    travelerModelSQL.editTraveler(traveler, listFavoriteCategories, context, new Model.AddTravelerListener() {
                        @Override
                        public void onComplete(String isSuccess) {

                        }
                    });
                    listener.onComplete(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCallPost);

    }
    public void addTrip(String tripName,String tripLocation,String travelerMail, Integer  tripDays,Context context, Model.AddTripListener listener) {
        final String URL_ADD_TRIP = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/traveler/addTrip";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_ADD_TRIP);

        HashMap<String, String> paramsPost = new HashMap<>();
        paramsPost.put("tripDaysNumber", String.valueOf(tripDays));
        paramsPost.put("tripName", tripName);
        paramsPost.put("travelerMail", travelerMail);
        paramsPost.put("tripDestination", tripLocation);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String myDate=dateFormat.format(date);
        paramsPost.put("tripDate",myDate);
        ObjectId _id= new ObjectId();
        String myId=_id.toString();
        paramsPost.put("tripId",myId);
        httpCallPost.setParams(paramsPost);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                try {

                    Trip trip = new Trip(myId, myDate, travelerMail, tripLocation, tripName, tripDays);
                    travelerModelSQL.addTrip(trip, context, new Model.AddTripListener() {
                        @Override
                        public void onComplete(String tripId) {
                            listener.onComplete(myId);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCallPost);

    }
    public void planTrip(ArrayList<PlacePlanning> chosenPlaces, int tripDays, Model.PlanTripListener listener ){
        final String URL_PLAN_TRIP = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/plantrip/samesizekmeans";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_PLAN_TRIP);
        HashMap<String, String> paramsPost = new HashMap<>();

        for (int i = 0; i < chosenPlaces.size(); ++i) {
            paramsPost.put("t" + "[" + i + "]", String.valueOf(chosenPlaces.get(i).getPlaceLocationLat()) + "," + String.valueOf(chosenPlaces.get(i).getPlaceLocationLng()));
        }
        paramsPost.put("numDayTrip", String.valueOf(tripDays));
        httpCallPost.setParams(paramsPost);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                try {
                    String[] arrOfStr = result.split(",");
                    for (int j=0; j<chosenPlaces.size();++j){
                        String[] temp = arrOfStr[j].split("=");
                        chosenPlaces.get(j).setDay_in_trip(Integer.parseInt((temp[1]))+1);

                    }
                    listener.onComplete(chosenPlaces);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute(httpCallPost);

    }

    public void addPlace(PlacePlanning place,String tripLocation,String travelerMail,String tripId,Context context,Model.AddPlaceListener listener){
        final String URL_ADD_Place = "https://triplanner-server-1d6bb31d6c46.herokuapp.com/traveler/addPlace";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_ADD_Place);
        HashMap<String, String> paramsPlace = new HashMap<>();
        paramsPlace.put("placeId",place.getPlaceID());
        paramsPlace.put("placeName",place.getPlaceName());
        paramsPlace.put("placeLocationLat",String.valueOf(place.getPlaceLocationLat()));
        paramsPlace.put("placeLocationLng",String.valueOf(place.getPlaceLocationLng()));
        paramsPlace.put("placeFormattedAddress", place.getPlaceFormattedAddress());
        paramsPlace.put("placeInternationalPhoneNumber", place.getPlaceInternationalPhoneNumber());
        paramsPlace.put("placeRating", String.valueOf(place.getPlaceRating()));
        paramsPlace.put("placeWebsite",place.getPlaceWebsite());
        paramsPlace.put("placeImgUrl",place.getPlaceImgUrl());
        paramsPlace.put("placeDayInTrip",String.valueOf(place.getDay_in_trip()));
        paramsPlace.put("travelerMail", travelerMail);
        paramsPlace.put("tripDestination", tripLocation);
        paramsPlace.put("tripId",tripId);
        if(place.getPlaceOpeningHours()!=null){
            for (int j=0;j<place.getPlaceOpeningHours().size();++j){
                paramsPlace.put("placeOpeningHours"+"[" + j + "]",place.getPlaceOpeningHours().get(j));
            }
        }
        else{
            paramsPlace.put("placeOpeningHours","");
        }
        httpCallPost.setParams(paramsPlace);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d("My Response:",response.toString());
                String result = response.toString();
                List<OpenHours>openHoursList= new ArrayList<OpenHours>();
                try {
                    Place newPlace=new Place(place.getPlaceID(),place.getPlaceName(),place.getPlaceLocationLat(),place.getPlaceLocationLng(),place.getPlaceFormattedAddress(),place.getPlaceInternationalPhoneNumber(), place.getPlaceRating(), place.getPlaceWebsite(), place.getPlaceImgUrl(), place.getDay_in_trip(),travelerMail,tripId);
                    if(place.getPlaceOpeningHours()!=null){
                        for(int i=0;i<place.getPlaceOpeningHours().size();++i){
                            openHoursList.add(new OpenHours(place.getPlaceOpeningHours().get(i),place.getPlaceID()));
                        }
                    }
                    travelerModelSQL.addPlace(newPlace, openHoursList, context, new Model.AddTripListener() {
                        @Override
                        public void onComplete(String tripId) {
                            listener.onComplete(true);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCallPost);


    }


}

