package com.example.policeapp.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.policeapp.R;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ViewDialog {
    ImageView imageview;
    ProgressBar progressBar;
    Button cancel,ok;
    public void showDialog(final Activity activity, final File file, final Context context, final String check){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_preview_dialog);
        imageview = dialog.findViewById(R.id.imagepreview);
        progressBar  = dialog.findViewById(R.id.progressBar3);
        cancel = (Button) dialog.findViewById(R.id.cancel);
        ok = (Button) dialog.findViewById(R.id.ok);
        final Manuallpdialog manuallpdialog = new Manuallpdialog();
        Picasso.get().load(file).into(imageview,new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                if(progressBar!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Exception e) {
                if(progressBar!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(check=="camera"){
                file.delete();}
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveBitmapToFile(file,activity);
                dialog.dismiss();
                if(file.getAbsoluteFile()!=null){
                    manuallpdialog.showDialog(activity,file,context,check);
                }
                else {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    public void saveBitmapToFile(File file,Activity activity){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            selectedBitmap = Bitmap.createBitmap(selectedBitmap,0,0,selectedBitmap.getWidth(),selectedBitmap.getHeight(),matrix,true);
            inputStream.close();

            // here i override the original image file
            File file1 = new File(activity.getExternalFilesDir(null),
                    "Compress.jpg");
            FileOutputStream outputStream = new FileOutputStream(file1);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);


        } catch (Exception e) {
            Log.e("ViewDialog",e.toString());
        }
    }
    public File saveBitmapratiotofile(File file,Activity activity){
        Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
// original measurements
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = 644;//or the width you need
        File file1 = new File(activity.getExternalFilesDir(null),
                "Compress.jpg");
        if(origWidth > destWidth){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / destWidth ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            b2 = Bitmap.createBitmap(b2,0,0,b2.getWidth(),b2.getHeight(),matrix,true);


            // we save the file, at least until we have made use of it
            // here i override the original image file

            //write the bytes in file
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(file1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG,100 , fo);

        }
        return file1;
    }
}
