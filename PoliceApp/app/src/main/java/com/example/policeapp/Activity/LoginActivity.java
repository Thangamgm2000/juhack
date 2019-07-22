package com.example.policeapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.policeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    TextView username,password;
    EditText usernameval,passwordval;
    Button submit;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        usernameval = findViewById(R.id.usernameval);
        passwordval = findViewById(R.id.passwordval);
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progressBarlogin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameval.setVisibility(View.INVISIBLE);
                passwordval.setVisibility(View.INVISIBLE);
                username.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                if(!usernameval.getText().toString().equals("")&&!passwordval.getText().toString().equals("")){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hash = digest.digest(passwordval.getText().toString().getBytes("UTF-8"));
                        StringBuilder hexString = new StringBuilder();

                        for (int i = 0; i < hash.length; i++) {
                            String hex = Integer.toHexString(0xff & hash[i]);
                            if(hex.length() == 1) hexString.append('0');
                            hexString.append(hex);
                        }
                        Log.e("hash",hexString.toString());
                        jsonObject.put("username",usernameval.getText().toString());
                        jsonObject.put("password",hexString.toString());
                    }catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post("https://86d58070.ap.ngrok.io/credentials")
                            .addJSONObjectBody(jsonObject) // posting json
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.get("flag").toString().equals("success")){
                                            Intent intent = new Intent(LoginActivity.this, OtpVerification.class);
                                            startActivity(intent);
                                        }
                                        else {

                                            Toast.makeText(LoginActivity.this,"Username or Password wrong",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            usernameval.setVisibility(View.VISIBLE);
                                            passwordval.setVisibility(View.VISIBLE);
                                            username.setVisibility(View.VISIBLE);
                                            password.setVisibility(View.VISIBLE);
                                            submit.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Enter username and password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    usernameval.setVisibility(View.VISIBLE);
                    passwordval.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
}
