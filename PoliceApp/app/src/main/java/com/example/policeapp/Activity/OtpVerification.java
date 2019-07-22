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

public class OtpVerification extends AppCompatActivity {

    TextView email,otp;
    EditText otpval,emailval;
    Button verify,submit;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpverify);
        email = findViewById(R.id.email);
        otp = findViewById(R.id.OTP);
        emailval = findViewById(R.id.emailval);
        otpval = findViewById(R.id.OTPval);
        verify = findViewById(R.id.verify);
        submit = findViewById(R.id.submit);
        progressBar=findViewById(R.id.progressBarotp);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(!emailval.getText().toString().equals("")){
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("email",emailval.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post("https://86d58070.ap.ngrok.io/email")
                            .addJSONObjectBody(jsonObject) // posting json
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    try {
                                        if(response.get("flag").toString().equals("success")){
                                            submit.setVisibility(View.INVISIBLE);
                                            email.setVisibility(View.INVISIBLE);
                                            emailval.setVisibility(View.INVISIBLE);
                                            verify.setVisibility(View.VISIBLE);
                                            otp.setVisibility(View.VISIBLE);
                                            otpval.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            Toast.makeText(OtpVerification.this,"Wrong Email!!!",Toast.LENGTH_SHORT).show();
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
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(OtpVerification.this,"Enter A Email!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(!otpval.getText().toString().equals("")){
                    JSONObject jsonObject= new JSONObject();


                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hash = digest.digest(otpval.getText().toString().getBytes("UTF-8"));
                        StringBuilder hexString = new StringBuilder();

                        for (int i = 0; i < hash.length; i++) {
                            String hex = Integer.toHexString(0xff & hash[i]);
                            if(hex.length() == 1) hexString.append('0');
                            hexString.append(hex);
                        }
                        Log.e("hash",hexString.toString());
                        jsonObject.put("otp",hexString.toString());
                    }catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post("https://86d58070.ap.ngrok.io/otp_verify")
                            .addJSONObjectBody(jsonObject) // posting json
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    try {
                                        if(response.get("flag").toString().equals("success")){
                                            Intent intent = new Intent(OtpVerification.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(OtpVerification.this,"Wrong OTP!!!",Toast.LENGTH_SHORT).show();
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
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(OtpVerification.this,"Enter A OTP!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(OtpVerification.this, LoginActivity.class);
        startActivity(intent);
    }
}
