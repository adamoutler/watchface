package com.casual_dev.casualmessenger.Serialization;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by adamoutler on 11/15/14.
 */
public class SerializableImage implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int NO_IMAGE = -1;
    public byte[] imagebytes;

    public SerializableImage(Bitmap bmp) {
        convertToByteArray(bmp);
    }

    public Integer length() {
        if (imagebytes == null) return 0;

        return imagebytes.length;
    }

    public Bitmap getImage() {
        return BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);


    }

    private void convertToByteArray(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imagebytes = stream.toByteArray();

    }


}