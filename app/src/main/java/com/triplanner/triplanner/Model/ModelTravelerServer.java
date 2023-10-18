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
        final String URL_ADD_TRIP = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/addtraveler";
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
        paramsTraveler.put("travelerPicture",traveler.getTravelerPicture());
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
        final String URL_GET_TRAVELER = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/getinfotraveler";
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
                    Traveler traveler= new Traveler(jsonTraveler.get("travelerMail").toString(),jsonTraveler.get("travelerName").toString(),Integer.valueOf(jsonTraveler.get("travelerBirthYear").toString()),jsonTraveler.get("travelerGender").toString(),jsonTraveler.get("travelerPicture").toString());
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
        final String URL_EDIT_TRAVELER = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/editTraveler";
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
        paramsTraveler.put("travelerPicture",traveler.getTravelerPicture());
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
        final String URL_ADD_TRIP = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/addTrip";
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
        final String URL_PLAN_TRIP = "https://triplanner--server-5139d3ccceab.herokuapp.com/plantrip/samesizekmeans";
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
        final String URL_ADD_Place = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/addPlace";
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
    public  void editPlace(Place place,String tripDestination,Context context, Model.EditPlaceListener listener){
        final String URL_EDIT_TRIP = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/editTravelerPlace";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_EDIT_TRIP);
        HashMap<String, String> paramsPlace = new HashMap<>();
        paramsPlace.put("travelerMail", place.getTravelerMail());
        paramsPlace.put("placeId", place.getPlaceID());
        paramsPlace.put("tripId", place.getId_trip());
        paramsPlace.put("placeDayInTrip", String.valueOf(place.getDay_in_trip()));
        paramsPlace.put("travelerPlaceRating", String.valueOf(place.getTravelerRating()));
        paramsPlace.put("tripDestination", tripDestination);
        httpCallPost.setParams(paramsPlace);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                try {
                    if (response != "false") {
                        travelerModelSQL.editPlace(place, context, new Model.EditPlaceListener() {
                            @Override
                            public void onComplete(boolean isSuccess) {
                                listener.onComplete(true);
                            }
                        });
                    }
                    else{
                        listener.onComplete(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCallPost);
    }



    public  void getTripUser(String travelerMail, Context context,Model.GetTripUserListener listener){
        final String URL_GET_TRIP = "https://triplanner--server-5139d3ccceab.herokuapp.com/traveler/getTripUser";
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
        httpCallPost.setUrl(URL_GET_TRIP);
        HashMap<String, String> paramsPlace = new HashMap<>();
        paramsPlace.put("travelerMail", travelerMail);
        httpCallPost.setParams(paramsPlace);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                try {
                    if (response != "false") {
                        JSONObject object  = new JSONObject(response);
                        JSONArray trips = (JSONArray) object.get("trips");
                        JSONArray placeTraveler = (JSONArray)object.get("placeTraveler") ;
                        for(int k=0;k<trips.length();++k){
                            JSONObject trip = trips.getJSONObject(k);
                            String id_trip = trip.get("tripId").toString();
                            String travelerMail=  trip.get("travelerMail").toString();
                            String tripDestination = trip.get("tripDestination").toString();
                            String tripName = trip.get("tripName").toString();
                            int tripDaysNumber= Integer.parseInt(trip.get("tripDaysNumber").toString());
                            String date= trip.get("tripDate").toString();
                            Trip myTrip=new Trip(id_trip,date,travelerMail,tripDestination,tripName,tripDaysNumber);
                            travelerModelSQL.addTrip(myTrip, context, new Model.AddTripListener() {
                                @Override
                                public void onComplete(String tripId) {

                                }
                            });
                        }
                        for(int i=0;i<placeTraveler.length();++i){
                            JSONObject placeObject= placeTraveler.getJSONObject(i);
                            String placeID = placeObject.get("placeId").toString();
                            String placeName = placeObject.get("placeName").toString();
                            double placeLocationLat = Double.valueOf(placeObject.get("placeLocationLat").toString());
                            double placeLocationLng = Double.valueOf(placeObject.get("placeLocationLng").toString());
                            String placeFormattedAddress = placeObject.get("placeFormattedAddress").toString();
                            String placeInternationalPhoneNumber=placeObject.get("placeInternationalPhoneNumber").toString();
                            float placeRating = Float.parseFloat(placeObject.get("placeRating").toString());
                            String placeWebsite = placeObject.get("placeWebsite").toString();
                            String placeImgUrl = placeObject.get("placeImgUrl").toString();
                            int day_in_trip = Integer.parseInt(placeObject.get("placeDayInTrip").toString());
                            String travelerMail = placeObject.get("travelerMail").toString();
                            String id_trip = placeObject.get("tripId").toString();
                            float travelerRating = Float.parseFloat(placeObject.get("travelerPlaceRating").toString());
                            JSONArray openHours = placeObject.getJSONArray("placeOpeningHours");
                            Place place= new Place(placeID,placeName,placeLocationLat,placeLocationLng,placeFormattedAddress,placeInternationalPhoneNumber,placeRating,placeWebsite,placeImgUrl,day_in_trip,travelerMail,id_trip);
                            place.setTravelerRating(travelerRating);
                            List<OpenHours> myOpenHours=null;
                            if(openHours.length()>0) {
                                myOpenHours= new ArrayList<>();
                                for (int j = 0; j < openHours.length(); ++j) {
                                    myOpenHours.add(new OpenHours(openHours.get(j).toString(), travelerMail));
                                }
                            }

                            travelerModelSQL.addPlace(place, myOpenHours, context, new Model.AddTripListener() {
                                @Override
                                public void onComplete(String tripId) {

                                }
                            });
                        }
                        listener.onComplete(true);
                    }
                    else{
                        listener.onComplete(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCallPost);

    }
    public void getPlaceFromRecommender(String travelerMail,String tripDestination,Model.GetPlaceRecommenderListener listener) {
    }



}

