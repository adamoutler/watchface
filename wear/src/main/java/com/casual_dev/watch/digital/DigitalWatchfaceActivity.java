package com.casual_dev.watch.digital;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.casual_dev.R;
import com.casual_dev.watch.MyTextView;
import com.casual_dev.watch.WatchFaceLifecycle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DigitalWatchfaceActivity extends Activity implements WatchFaceLifecycle.Listener {
    /*Date change Filters*/
    private final static IntentFilter INTENT_FILTER_DATE; //Date Filter
    static {
        INTENT_FILTER_DATE = new IntentFilter();
        INTENT_FILTER_DATE.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_DATE.addAction(Intent.ACTION_DATE_CHANGED);
    }
    /*Time change Filters*/
    private final static IntentFilter INTENT_FILTER_TIME;
    static {
        INTENT_FILTER_TIME = new IntentFilter();
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }
    private final String TIME_FORMAT_DISPLAYED = "kk:mm";
    private final String DATE_FORMAT_DISPLAYED = "E LLL d";
    MyTextView mTime;
    TextView mDate;
    TextView mBABBQText;
    TextView blueFive;
    ImageView mBackLogo;
    ImageView mTextBack;

    private BroadcastReceiver mDateInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            mDate.setText(new SimpleDateFormat(DATE_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime()));
        }
    };
    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            mTime.setText(new SimpleDateFormat(TIME_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime()));
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_main_activity);
        WatchFaceLifecycle.attach(this, savedInstanceState, this);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.digital_watch_view_stub);


        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/digital-7 (mono).ttf");
                setWidgets();
                mTime.resizeText();
                //setFonts();
                mTimeInfoReceiver.onReceive(DigitalWatchfaceActivity.this, registerReceiver(null, INTENT_FILTER_TIME));    //Time.
                registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);
                mDateInfoReceiver.onReceive(DigitalWatchfaceActivity.this, registerReceiver(null, INTENT_FILTER_DATE));    //Date.
                registerReceiver(mDateInfoReceiver, INTENT_FILTER_DATE);

            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setFonts() {
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/digital-7 (mono).ttf");
        mTime.setTypeface(myTypeface);

    }

    private void setWidgets() {
        mTime = (MyTextView) findViewById(R.id.time);
        mDate = (TextView) findViewById(R.id.date);
        blueFive= (TextView)findViewById(R.id.blueFive);
        mBABBQText = (TextView) findViewById(R.id.babbqText);
        mBackLogo = (ImageView) findViewById(R.id.logo);
        mTextBack = (ImageView) findViewById(R.id.grayBackColor);
    }

    @Override
    protected void onDestroy() { //Always unregister listeners to be courtious on battery life
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mDateInfoReceiver);
    }



    @Override
    public void onScreenDim() {
        super.onPause();
        mBackLogo.setVisibility(View.INVISIBLE);
        mDate.setTextColor(Color.GRAY);
        mBABBQText.setTextColor(Color.GRAY);
        mTime.setTextColor(Color.parseColor("#3fa9f5"));
        mTextBack.setVisibility(View.INVISIBLE);
        mDate.setTypeface(null, Typeface.BOLD);
        mBABBQText.setTypeface(null, Typeface.BOLD);
        blueFive.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void onScreenAwake() {
        super.onResume();
        mTextBack.setBackgroundColor(Color.LTGRAY);
        mDate.setTextColor(Color.BLACK);
        mBABBQText.setTextColor(Color.BLACK);
        mBackLogo.setVisibility(View.VISIBLE);
        mTextBack.setVisibility(View.VISIBLE);
        mTime.setTextColor(Color.BLACK);
        mDate.setTypeface(null, Typeface.NORMAL);
        mBABBQText.setTypeface(null, Typeface.NORMAL);
        blueFive.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public void onWatchFaceRemoved() {

    }

    @Override
    public void onScreenOff() {

    }
}


