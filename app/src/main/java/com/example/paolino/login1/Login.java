package com.example.paolino.login1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends Activity {


    EditText username, password;
    Button sign_in ;
    String ST_username, ST_password;
    String finalResult ;
    String HttpURL = "http://findyourcarapp.altervista.org/login.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserName = "";
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.log_password);
        sign_in = findViewById(R.id.sign_in);

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);


        if(sp.contains("username")){

            finish();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

        }
        else {

            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CheckEditTextIsEmptyOrNot();

                    if (CheckEditText) {

                        UserLoginFunction(ST_username, ST_password);

                    } else {

                        Toast.makeText(Login.this, "Please Fill All Form Fields", Toast.LENGTH_LONG).show();
                        if (TextUtils.isEmpty(username.getText().toString())) {

                            username.setError("This Field Is Required");

                        }
                        if (TextUtils.isEmpty(password.getText().toString())) {

                            password.setError("This Filed Is Required");

                        }
                    }
                }
            });
        }
    }


    public void register(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }


    public void CheckEditTextIsEmptyOrNot(){

        ST_username = username.getText().toString();
        ST_password = password.getText().toString();

        if(TextUtils.isEmpty(ST_username) || TextUtils.isEmpty(ST_password))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;

        }
    }


    public void UserLoginFunction(final String username, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Login.this,"Logging In","Please Wait",true,false);

            }


            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.trim().equalsIgnoreCase("Data Matched")){

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username",ST_username);
                    editor.commit();

                    finish();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra(UserName,ST_username);
                    startActivity(intent);

                }
                else if(httpResponseMsg.trim().equalsIgnoreCase("Invalid Username or Password Please Try Again") ||
                        httpResponseMsg.trim().equalsIgnoreCase("Check Again")){

                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();

                }
                else{

                    Toast.makeText(Login.this,"Connection Error",Toast.LENGTH_LONG).show();

                }
            }


            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);
                hashMap.put("password",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;

            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(username,password);

    }
}