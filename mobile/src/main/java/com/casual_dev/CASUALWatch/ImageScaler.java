package com.casual_dev.CASUALWatch;

import android.graphics.Bitmap;

/**
 * Scales images by cropping to square
 * found on http://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
 * Created by adamoutler on 11/23/14.
 */
public class ImageScaler {

    public Bitmap makeImageSquare(int px, Bitmap bitmap) {
        Bitmap returnbmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            returnbmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {

            returnbmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return Bitmap.createScaledBitmap(returnbmp, px, px, true);
    }
}



