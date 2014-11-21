package com.casual_dev.casualmessenger.user_types;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Provides a way to serialize an image into a Byte array.   Bitmap is not serializable.
 * This allows a bitmap to be converted into a byte array and sent for JSON serialization.
 * Created by adamoutler on 11/15/14.
 */
public class SerializableImage implements Serializable {

    private static final long serialVersionUID = 1L;

    public byte[] imageBytes;

    /**
     * instantiates a serializable image from a bitmap
     *
     * @param bmp bitmap to be contained.
     */
    public SerializableImage(Bitmap bmp) {
        convertToByteArray(bmp);
    }

    /**
     * returns the length of the bitmap
     *
     * @return length
     */
    public Integer length() {
        if (imageBytes == null) return 0;

        return imageBytes.length;
    }

    /**
     * gets the bitmap after serialization
     *
     * @return bitmap to be returned
     */
    public Bitmap getImage() {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


    }

    private void convertToByteArray(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageBytes = stream.toByteArray();

    }


}