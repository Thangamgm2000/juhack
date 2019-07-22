package com.example.policeapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.policeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FinalActivity extends AppCompatActivity {
    TextView reg_no,reg_noval,name,nameval,chassis,chassisval,engine,engineval,Reg_date,Reg_dateval,vehicleclass,vehicleclassval,fuel,fuelval,maker,makerval,fitnessdate,fitnessdateval,roadtax,roadtaxval,license,licenseval,insurence,insurenceval,rcbook,rcbookval;
    JSONObject jsonObject;
    @Override
    public void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.finallayout);
        reg_no=findViewById(R.id.reg_no);
        reg_noval=findViewById(R.id.reg_noval);
        name=findViewById(R.id.name);
        nameval=findViewById(R.id.nameval);
        chassis=findViewById(R.id.chassis);
        chassisval=findViewById(R.id.chassisval);
        engine=findViewById(R.id.engine);
        engineval=findViewById(R.id.engineval);
        Reg_date=findViewById(R.id.Reg_date);
        Reg_dateval=findViewById(R.id.Reg_dateval);
        vehicleclass=findViewById(R.id.vehicleclass);
        vehicleclassval=findViewById(R.id.vehicleclassval);
        fuel=findViewById(R.id.fuel);
        fuelval=findViewById(R.id.fuelval);
        maker=findViewById(R.id.maker);
        makerval=findViewById(R.id.makerval);
        fitnessdate=findViewById(R.id.fitnessdate);
        fitnessdateval=findViewById(R.id.fitnessdateval);
        roadtax=findViewById(R.id.roadtax);
        roadtaxval=findViewById(R.id.roadtaxval);
        license=findViewById(R.id.license);
        licenseval=findViewById(R.id.licenseval);
        insurence=findViewById(R.id.insurence);
        insurenceval=findViewById(R.id.insurenceval);
        rcbook=findViewById(R.id.rcbook);
        rcbookval=findViewById(R.id.rcbookval);
        try {
            jsonObject=new JSONObject(getIntent().getStringExtra("object"));
            reg_noval.setText(jsonObject.getString("REGNO"));
            nameval.setText(jsonObject.getString("NAME"));
            chassisval.setText(jsonObject.getString("CHASSIS"));
            engineval.setText(jsonObject.getString("ENGINE"));
            Reg_dateval.setText(jsonObject.getString("REGDATE"));
            vehicleclassval.setText(jsonObject.getString("VEHICLE"));
            fuelval.setText(jsonObject.getString("FUEL"));
            makerval.setText(jsonObject.getString("MAKER"));
            fitnessdateval.setText(jsonObject.getString("FITNESS"));
            roadtaxval.setText(jsonObject.getString("ROADTAX"));
            licenseval.setText(jsonObject.getString("LICENSE"));
            insurenceval.setText(jsonObject.getString("INSURANCE"));
            rcbookval.setText(jsonObject.getString("RCBOOK"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
