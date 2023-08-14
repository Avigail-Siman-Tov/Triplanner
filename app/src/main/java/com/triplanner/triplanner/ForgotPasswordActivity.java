package com.triplanner.triplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText inputMail;
    Button btnSave;
    App app;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputMail=findViewById(R.id.activity_forgetPassword_inputMail);
        btnSave=findViewById(R.id.activity_forgetPassword_btn_send);
        Realm.init(this); // context, usually an Activity or Application
        app=new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        builder= new AlertDialog.Builder(this);
        builder.setMessage("Check your email!\n\n" + "A password reset link has been sent\n");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLinkToMail();
            }
        });
    }

    private void sendLinkToMail() {
        String mail=inputMail.getText().toString();
        if(mail.isEmpty()|| !mail.contains("@")){
            Toast.makeText(ForgotPasswordActivity.this,"Please Enter Your Email", Toast.LENGTH_SHORT).show();
        }
        else{
            app.getEmailPassword().sendResetPasswordEmailAsync(String.valueOf(mail), new App.Callback<Void>() {
                @Override
                public void onResult(App.Result<Void> result) {
                    if (result.isSuccess()) {
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
//
                    }else {
                        Toast.makeText(ForgotPasswordActivity.this,"Failed to reset the password for" + mail + ": " + result.getError().getErrorMessage(),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

            });

        }
    }
}