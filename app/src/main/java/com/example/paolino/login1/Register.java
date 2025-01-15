package com.example.paolino.login1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {


    Button sign_up;
    EditText username, email, password, confirm;
    String ST_username, ST_email, ST_password, ST_confirm;
    String finalResult ;
    String HttpURL = "http://findyourcarapp.altervista.org/registration.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        sign_up = findViewById(R.id.sign_up);


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus){

                    if(!TextUtils.isEmpty(email.getText().toString()) && !isEmailValid(email.getText().toString())){

                        email.setError("This Email Address Is Not Valid");

                    }
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus) {

                    if (!TextUtils.isEmpty(password.getText().toString()) && password.getText().toString().length() < 5) {

                        password.setError("Password Must Be At Least 5 Characters");

                    }
                }
            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                if (CheckEditText) {

                    if(isEmailValid(email.getText().toString())) {

                        if (password.getText().toString().length() > 4) {

                            if(confirm.getText().toString().equals(password.getText().toString())) {

                                UserRegisterFunction(ST_username, ST_email, ST_password, ST_confirm);

                            }
                            else{

                                confirm.setError("Passwords Don't Correspond");

                            }
                        }
                        else {

                            Toast.makeText(Register.this, "Password Must Be At Least 5 Characters", Toast.LENGTH_LONG).show();

                        }
                    }
                    else{

                        Toast.makeText(Register.this, "This Email Address Is Not Valid", Toast.LENGTH_LONG).show();

                    }
                }
                else {

                    Toast.makeText(Register.this, "Please Fill All Form Fields", Toast.LENGTH_LONG).show();
                    if(TextUtils.isEmpty(username.getText().toString())) {

                        username.setError("This Field Is Required");

                    }
                    if(TextUtils.isEmpty(email.getText().toString())) {

                        email.setError("This Field Is Required");

                    }
                    if(TextUtils.isEmpty(password.getText().toString())) {

                        password.setError("This Field Is Required");

                    }
                    if(TextUtils.isEmpty(confirm.getText().toString())) {

                        confirm.setError("This Field Is Required");

                    }
                }
            }
        });
    }


    public void login(View view){
        finish();
    }


    public void CheckEditTextIsEmptyOrNot(){

        ST_username = username.getText().toString();
        ST_email = email.getText().toString();
        ST_password = password.getText().toString();
        ST_confirm = confirm.getText().toString();

        if(TextUtils.isEmpty(ST_username) || TextUtils.isEmpty(ST_email) || TextUtils.isEmpty(ST_password) || TextUtils.isEmpty(ST_confirm)) {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;

        }
    }


    public void UserRegisterFunction(final String username, final String email, final String password, final String confirm){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Register.this,"Registration", "Please Wait",true, false);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();

                if(httpResponseMsg.trim().equalsIgnoreCase("Username Already Exist") ||
                        httpResponseMsg.trim().equalsIgnoreCase("Email Already Exist")) {

                    Toast.makeText(Register.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

                }
                else if(httpResponseMsg.trim().equalsIgnoreCase("Registration Successfully")){

                    Toast.makeText(Register.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    finish();

                }
                else{

                    Toast.makeText(Register.this,"Connection Error", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);
                hashMap.put("email",params[1]);
                hashMap.put("password",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;

            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(username,email,password,confirm);

    }


    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

