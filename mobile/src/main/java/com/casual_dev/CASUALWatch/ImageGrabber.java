//Read more: http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz3IuDPqPxS

package com.casual_dev.CASUALWatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ImageGrabber extends Activity {
    final static String TAG = "ImageGrabber";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_GALLERY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grabber);
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
                dispatchGalleryIntent();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent i = new Intent();

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            i.putExtra("image", new ImageScaler().makeImageSquare(320, bitmap));
            setResult(Activity.RESULT_OK, i);
            Log.d(TAG, "got image");
            finish();
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bundle extras = data.getExtras();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                i.putExtra("image", new ImageScaler().makeImageSquare(320, bitmap));
                setResult(Activity.RESULT_OK, i);
            } catch (IOException e) {
                e.printStackTrace();
            }

            finish();


        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    private void dispatchGalleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY);

    }
}


