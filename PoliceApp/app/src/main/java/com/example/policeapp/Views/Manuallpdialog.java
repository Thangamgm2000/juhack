package com.example.policeapp.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.policeapp.Activity.FinalActivity;

import com.example.policeapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
public class Manuallpdialog {
    ProgressBar progressBar;
    Button cancel,send;
    TextView serverplatetextview,manualplate;
    EditText manualenter;
    String plate;
    Activity activity1;
    public void showDialog(final Activity activity, final File compressfile, Context context, final String check){
        activity1=activity;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.manual_lp_dialog);
        progressBar=dialog.findViewById(R.id.progressBar4);
        cancel=dialog.findViewById(R.id.cancel1);
        send=dialog.findViewById(R.id.send);
        serverplatetextview=dialog.findViewById(R.id.serverplate);
        manualplate=dialog.findViewById(R.id.manualplate);
        manualenter=dialog.findViewById(R.id.editText);
        AndroidNetworking.upload("https://1f7b3962.ap.ngrok.io/upload")
                .addMultipartFile("file",compressfile.getAbsoluteFile())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        progressBar.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        send.setVisibility(View.VISIBLE);
                        manualplate.setVisibility(View.VISIBLE);
                        manualenter.setVisibility(View.VISIBLE);
                        try {
                            serverplatetextview.setVisibility(View.VISIBLE);
                            serverplatetextview.setText(response.getString("detectedPlate"));
                            plate=response.getString("detectedPlate");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("hello",error.getErrorBody());
                        progressBar.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        send.setVisibility(View.VISIBLE);
                        manualplate.setVisibility(View.VISIBLE);
                        manualenter.setVisibility(View.VISIBLE);
                        serverplatetextview.setVisibility(View.VISIBLE);

                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manualenter.getText().toString().equals("")&&plate==null){
                    Toast.makeText(activity, "Enter Plate No!!!", Toast.LENGTH_SHORT).show();
                }
                if(manualenter.getText().toString().equals("")&&plate!=null){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("plate", plate);
                        sendplateno(jsonObject);
                        dialog.dismiss();

//                        if(manualenter.getText().toString().matches("(([A-Z]){2}(?:[0-9]){1,2}(?:[A-Z]){2}([0-9]){4})")){
//
//                        }
//                        else {
//                            Toast.makeText(activity, "Enter a proper plate no!!!", Toast.LENGTH_SHORT).show();
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                if(!(manualenter.getText().toString().equals(""))&&plate==null){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("plate",manualenter.getText().toString());
                        sendplateno(jsonObject);
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if ((!(manualenter.getText().toString().equals(""))&&plate!=null)){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("plate", manualenter.getText().toString());
                        sendplateno(jsonObject);
                        dialog.dismiss();

//                        if(manualenter.getText().toString().matches("(([A-Z]){2}(?:[0-9]){1,2}(?:[A-Z]){2}([0-9]){4})")){
//
//                        }
//                        else {
//                            Toast.makeText(activity, "Enter a proper plate no!!!", Toast.LENGTH_SHORT).show();
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check=="camera"){
                compressfile.delete();}
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void sendplateno(JSONObject jsonObject){
        activity1.setContentView(R.layout.progress);
        AndroidNetworking.post("https://86d58070.ap.ngrok.io/finding")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("hello","hello");
                        Intent intent = new Intent(activity1, FinalActivity.class);
                        intent.putExtra("object",response.toString());
                        activity1.startActivity(intent);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }



}
