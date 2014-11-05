package com.casual_dev.CASUALWatch.digital;
/*Copyright 2014 Adam Outler

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

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
        awakeView.clearAnimation();
        dimView.clearAnimation();
        dimView.setVisibility(View.VISIBLE);
        awakeView.setVisibility(View.INVISIBLE);


    }

    private void setView(final boolean awakening) {

        if (awakening) {
            dimView.setVisibility(View.VISIBLE);
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
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            awakeView.setVisibility(View.VISIBLE);
                            dimView.setVisibility(View.GONE);
                            awakeView.clearAnimation();
                            dimView.clearAnimation();
                            getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

        } else {
            dimView.setVisibility(View.INVISIBLE);
            awakeView.setAlpha(1f);
            awakeView.setVisibility(View.VISIBLE);
            dimView.setVisibility(View.VISIBLE);
            awakeView.animate()
                    .alpha(0f)
                    .setDuration(1000)
                    .setListener(null);
            dimView.setAlpha(0f);
            dimView.setVisibility(View.VISIBLE);
            awakeView.animate()
                    .alpha(0f)
                    .setDuration(1000)
                    .setListener(null);
            dimView.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            String random = (words[new Random().nextInt(words.length)]);
                            randomness.setText(random);
                            randomnessDim.setText(random);
                            awakeView.setVisibility(View.GONE);
                            dimView.setVisibility(View.VISIBLE);
                            awakeView.clearAnimation();
                            dimView.clearAnimation();
                            getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

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
