//Read more: http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz3IuDPqPxS

package com.casual_dev.CASUALWatch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ImageGrabber extends Activity {
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ImageView imgview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grabber);

        imgview = (ImageView) findViewById(R.id.imageView1);
        Button buttonCamera = (Button) findViewById(R.id.btn_take_camera);
        Button buttonGallery = (Button) findViewById(R.id.btn_select_gallery);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
// call android default gallery
                Uri uri = Uri.parse(new File(getFilesDir(), "tempFile").getAbsolutePath());

                intent.setDataAndType(uri, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// ******** code for crop image
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {

                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_GALLERY);

                } catch (ActivityNotFoundException e) {
// Do nothing for now
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("got image", imageBitmap.toString());
        }

    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
}


