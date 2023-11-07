package com.triplanner.triplanner.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.triplanner.triplanner.LoginActivity;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class LogoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Realm.init(this);
        App app=new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        User user = app.currentUser();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                user.logOutAsync( result -> {
                    if (result.isSuccess()) {
//                        Model.instance.deleteTraveler(user.getProfile().getEmail(), LogoutActivity.this, new Model.DeleteTravelerListener() {
//                            @Override
//                            public void onComplete(boolean isSuccess) {
//                                if(isSuccess){
//                                    Toast.makeText(LogoutActivity.this,"Delete user Succeeded",Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
                        Intent intent=new Intent(LogoutActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("AUTH", result.getError().toString());
                    }
                });
//
            }
        };
        Handler handler=new Handler();
        handler.postDelayed(runnable,5000);

    }
}