package com.triplanner.triplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Traveler;

import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout InputMail, InputPassword;
    Button loginBtn;
    TextView forgotPassword, createAccount;
    ProgressDialog myLoadingDialog;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");
        InputMail=findViewById(R.id.activity_login_input_mail);
        InputPassword=findViewById(R.id.activity_login_input_password);
        loginBtn=findViewById(R.id.activity_login_btn_signin);
        forgotPassword=findViewById(R.id.activity_login_forget_password);
        createAccount=findViewById(R.id.activity_login_create_new_account);
        myLoadingDialog= new ProgressDialog(this);

        Realm.init(this); // context, usually an Activity or Application
        app=new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentLinkToEmailResPassword();
            }
        });
    }
    private void sentLinkToEmailResPassword() {
        Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }
    private void goToSignUp(){
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    private void UserLogin(){
        String mail= InputMail.getEditText().getText().toString();
        String password= InputPassword.getEditText().getText().toString();

        if(mail.isEmpty() || !mail.contains("@")){
            showError(InputMail,"Email Address is not Valid");
        }
        else if(password.isEmpty() || password.length()<6){
            showError(InputPassword,"Password must be longer than 6 characters");
        }
        else{
            myLoadingDialog.setTitle("Login");
            myLoadingDialog.setMessage("Please Wait,Log-in To Your Account");
            myLoadingDialog.setCanceledOnTouchOutside(false);
            myLoadingDialog.show();
            Credentials credential = Credentials.emailPassword(mail,password);
            app.loginAsync(credential,new App.Callback<User>(){
                @Override
                public void onResult(App.Result<User> result) {
                    if(result.isSuccess()){
                        Log.d("mylog","aaa");
                        Model.instance.getTravelerByEmailInServer(mail, getApplicationContext(), new Model.GetTravelerByEmailListener() {
                            @Override
                            public void onComplete(Traveler traveler, List<String> favoriteCategories) {
                                Log.d("mylog","I am before");
                                myLoadingDialog.dismiss();
                                Log.d("mylog","I am after");
                                Toast.makeText(LoginActivity.this,"Log-in Succeeded",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else{
                        myLoadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Log-in Failed",Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }
    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}