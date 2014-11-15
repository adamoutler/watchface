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
import java.io.IOException;

public class ImageGrabber extends Activity {
    final static String TAG = "ImageGrabber";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ImageView imgview;
    Intent intent;

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
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 400);
                intent.putExtra("outputY", 400);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("return-data", true);

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
        Intent i = new Intent();

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            i.putExtra("image", Bitmap.createScaledBitmap((Bitmap) extras.get("data"), 300, 300, false));
            setResult(Activity.RESULT_OK, i);
            Log.d(TAG, "got images");
            finish();
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                i.putExtra("image", Bitmap.createScaledBitmap(bitmap, 300, 300, true));
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
}


