package com.triplanner.triplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class ResetPassActivity extends AppCompatActivity {
    private TextInputLayout InputPassword, InputVerifyPassword;
    ProgressDialog myLoadingDialog;
    Button resPasswordBtm;
    App app;
    Intent intent;
    Uri data;
    String data1,data2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        InputPassword =findViewById(R.id.fragment_reset_password_password);
        InputVerifyPassword =findViewById(R.id.fragment_reset_password_verify_password);
        resPasswordBtm=findViewById(R.id.fragment_reset_password_btm);
        Realm.init(this); // context, usually an Activity or Application
        app=new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());




        myLoadingDialog=new ProgressDialog(this);

            resPasswordBtm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetPassword();
                }
            });
        handleIntent(getIntent());
    }



    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            intent=getIntent();
            data=intent.getData();
            data1=data.getQueryParameter("token");
            data2=data.getQueryParameter("tokenId");
        }
    }
    private void resetPassword() {
            String password= InputPassword.getEditText().getText().toString();
            String verifyPassword= InputVerifyPassword.getEditText().getText().toString();
             if(password.isEmpty() || password.length()<6){
                showError(InputPassword,"Password must be longer than 6 characters");
            }
            else if(!verifyPassword.equals(password)){
                showError(InputVerifyPassword,"Password does not equal");
            }
            else{
                 resetPasswordInMongoDb(password);
            }

    }
    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
    private void resetPasswordInMongoDb(String password) {

        myLoadingDialog.setTitle("Reset Password");
        myLoadingDialog.setMessage("Please wait for your password to be reset,");
        myLoadingDialog.setCanceledOnTouchOutside(false);
        myLoadingDialog.show();
        app.getEmailPassword().resetPasswordAsync(data1, data2, password, new App.Callback<Void>() {
            @Override
            public void onResult(App.Result<Void> result) {
                if(result.isSuccess()){
                    myLoadingDialog.dismiss();
                    Toast.makeText(ResetPassActivity.this,"Password reset successfully",Toast.LENGTH_LONG).show();

                }
                else{
                    myLoadingDialog.dismiss();
                    Toast.makeText(ResetPassActivity.this,"Password reset failed",Toast.LENGTH_LONG).show();
                }
                Intent intent=new Intent(ResetPassActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

}