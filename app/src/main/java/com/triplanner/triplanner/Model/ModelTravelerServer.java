package com.triplanner.triplanner.Model;

import android.content.Context;
import android.util.Log;

import com.triplanner.triplanner.HTTP.HttpCall;
import com.triplanner.triplanner.HTTP.HttpRequest;


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
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
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
    public void getTraveler(String travelerMail,Context context,Model.GetTravelerByEmailListener listener){
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
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
        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodtype(HttpCall.GET);
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
}
