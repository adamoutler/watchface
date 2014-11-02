package com.casual_dev.CASUALWatch.digital;

import android.animation.Animator;
import android.view.View;

import java.util.Random;

 /*
  *Created by adamoutler on 11/1/14.
 */

public class DigitalWatchfaceActions extends DigitalWatchfaceActivity {

    private static String[] words = new String[]{"ELOPMENT", "EVOURING", "OUTSNESS", "ISTATION", "OTEDNESS", "ELISHNESS"};

    public DigitalWatchfaceActions() {
    }

    @Override
    public void onScreenDim() {
        setView(false);
    }

    @Override
    public void onScreenAwake() {
        setView(true);
    }


    @Override
    public void onWatchFaceRemoved() {
    }

    @Override
    public void onScreenOff() {
        setView(false);
    }

    private void setView(final boolean awakening) {

        if (awakening) {
            awakeView.setAlpha(0f);
            awakeView.setVisibility(View.VISIBLE);
            awakeView.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .setListener(null);
            dimView.setAlpha(1f);
            dimView.setVisibility(View.VISIBLE);
            dimView.animate()
                    .alpha(0f)
                    .setDuration(1000)
                    .setListener(null);
        } else {
            awakeView.setAlpha(1f);
            awakeView.setVisibility(View.VISIBLE);
            awakeView.animate()
                    .alpha(0f)
                    .setDuration(5000)
                    .setListener(null);
            dimView.setAlpha(0f);
            dimView.setVisibility(View.VISIBLE);
            dimView.animate()
                    .alpha(1f)
                    .setDuration(5000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            String random = (words[new Random().nextInt(words.length)]);
                            randomness.setText(random);
                            randomnessDim.setText(random);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });


        }
    }

}
