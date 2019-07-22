package com.example.policeapp.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.androidnetworking.AndroidNetworking;
import com.example.policeapp.Fragment.Camera2RawFragment;
import com.example.policeapp.R;
import com.example.policeapp.Views.ViewDialog;
import java.io.File;


public class MainActivity extends AppCompatActivity {

    Button gallery,camera;
    Context context;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startinglayout);

        gallery=findViewById(R.id.gallery);
        camera=findViewById(R.id.camera);
        context=this;
        activity=this;
        AndroidNetworking.initialize(getApplicationContext());

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 300);
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Camera2RawFragment.newInstance())
                        .commit();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            Uri picturePath = data.getData();
            cursor.close();

            File file = new File(getRealPathFromURI(picturePath));

            ViewDialog dialog =new ViewDialog();
            dialog.showDialog(activity,file,context,"main");



        }

    }

    public String getRealPathFromURI(Uri contentUri) {


        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

}


