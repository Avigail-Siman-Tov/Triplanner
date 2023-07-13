package com.triplanner.triplanner.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class Model {
    public final static Model instance = new Model();
    String appID = "triplanner-fjgqd";
    App app = new App(new AppConfiguration.Builder(appID).build());
    private ModelTravelerServer travelerModelServer=new ModelTravelerServer();
    private ModelTravelerSQL travelerModelSQL=new ModelTravelerSQL();
    List<String>listFavoriteCategoriesOfTraveler;
    private Model(){}

    public interface AddTravelerListener{
        void onComplete(String isSuccess);
    }
    public void addTraveler(final Traveler traveler,final List<FavoriteCategories> listFavoriteCategories,Context context ,final AddTravelerListener listener){
        travelerModelServer.addTraveler(traveler,listFavoriteCategories,context,listener);
    }
    public void getTravelerByEmailInDB(String travelerMail, Context context, final GetTravelerByEmailListener listener){

        travelerModelSQL.getTravelerByMail(travelerMail, context,listener);

    }

    public void getTravelerByEmailInServer(String travelerMail, Context context, final GetTravelerByEmailListener listener){

        travelerModelServer.getTraveler(travelerMail, context,listener);

    }
    public void editTraveler(final Traveler traveler,final List<FavoriteCategories> listFavoriteCategories,Context context ,final EditTravelerListener listener){
        travelerModelServer.editTraveler(traveler,listFavoriteCategories,context,listener);
    }

    public  void deleteTraveler(String travelerMail, Context context,final DeleteTravelerListener listener){
        travelerModelSQL.deleteTraveler(travelerMail,context,listener);
    }
    public void getAllFavoriteCategoriesOfTraveler(String travelerMail,Context context,final  GetTravelerFavoriteCategories listener) {

      travelerModelSQL.getAllFavoriteCategoriesOfTraveler(travelerMail,context, listener);
    }

    public void getOpenHoursOfPlace(String placeId,Context context,Model.GetOpenHoursOfPlaceListener listener){
        travelerModelSQL.getOpenHoursOfPlace(placeId,context,listener);
    }

    public interface AddTravelerAndFavoriteCategoriesListener{
        void onComplete(boolean isSuccess);
    }
    public interface GetTravelerByEmailListener{
        void onComplete(Traveler traveler,List<String> favoriteCategories);
    }
    public interface GetTravelerFavoriteCategories{
        void onComplete(List<String> favoriteCategories);
    }
    public  interface DeleteTravelerListener{
        void onComplete(boolean isSuccess);
    }
    public interface EditTravelerListener{
        void onComplete(String isSuccess);
    }

    public interface GetOpenHoursOfPlaceListener{
        void onComplete(List<String>result);
    }
}
