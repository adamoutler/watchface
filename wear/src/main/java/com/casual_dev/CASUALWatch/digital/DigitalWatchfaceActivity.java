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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.casual_dev.CASUALWatch.R;
import com.casual_dev.CASUALWatch.widget.MyTextView;
import com.casual_dev.CASUALWatch.widget.WatchFaceLifecycle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DigitalWatchfaceActivity extends Activity implements WatchFaceLifecycle.Listener {
    final static String TAG = "DigitalWatchFaceActivity";
    /*Date change Filters*/
    private final static IntentFilter INTENT_FILTER_DATE; //Date Filter

    /*Time change Filters*/
    private final static IntentFilter INTENT_FILTER_TIME;
    private final static IntentFilter INTENT_FILTER_BATTERY;
    static TextView randomness;
    static ImageView mBackLogo;

    static {
        INTENT_FILTER_BATTERY = new IntentFilter();
        INTENT_FILTER_DATE = new IntentFilter();
        INTENT_FILTER_TIME = new IntentFilter();
        setFilters();
    }

    static DigitalWatchfaceActivity instance;
    MyTextView mTimeAwake;
    MyTextView mTimeDim;
    TextView mDateAwake;
    TextView mDateDim;
    TextView mDev;
    TextView mDevDim;
    TextView battery;
    TextView mCasualDim;
    TextView mCasual;
    TextView randomnessDim;
    View awakeView;
    View dimView;
    boolean receiversSet = false;
    private BroadcastReceiver mBattInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            batteryLevelChanged(level);
        }


    };
    private BroadcastReceiver mDateInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String DATE_FORMAT_DISPLAYED = "E dLLLyy";
            String time = new SimpleDateFormat(DATE_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime());
            mDateAwake.setText(time);
            mDateDim.setText(time);
        }
    };
    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String TIME_FORMAT_DISPLAYED = "kk:mm";
            String date = new SimpleDateFormat(TIME_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime());
            mTimeAwake.setText(date);
            mTimeDim.setText(date);
            Log.d("Tick", "tock");
        }
    };

    private static void setFilters() {
        //date filters
        INTENT_FILTER_BATTERY.addAction(Intent.ACTION_BATTERY_CHANGED);
        INTENT_FILTER_DATE.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_DATE.addAction(Intent.ACTION_DATE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }

    public static DigitalWatchfaceActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WatchFaceLifecycle.attach(this, savedInstanceState, this);
        setContentView(R.layout.digital_main_activity);
        findViewById(R.id.digital_watch_view_stub_awake).setVisibility(View.VISIBLE);
        findViewById(R.id.digital_watch_view_stub_dim).setVisibility(View.INVISIBLE);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.digital_watch_view_stub_dim);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Log.d(TAG, "Layout Inflated");
                setWidgets();
                setReceivers();
            }
        });
        instance = this;
    }

    private void setReceivers() {
        if (!receiversSet) {
            mBattInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_BATTERY));
            mTimeInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_TIME));    //Time.
            registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);
            mDateInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_DATE));    //Date.
            registerReceiver(mDateInfoReceiver, INTENT_FILTER_DATE);
            receiversSet = true;
        }
    }

    private void setWidgets() {
        battery = (TextView) findViewById(R.id.battery);
        battery.setVisibility(View.INVISIBLE);
        dimView = findViewById(R.id.digital_watch_view_stub_dim);
        awakeView = findViewById(R.id.digital_watch_view_stub_awake);
        mTimeAwake = (MyTextView) findViewById(R.id.timeawake);
        mDateAwake = (TextView) findViewById(R.id.dateawake);
        mTimeDim = (MyTextView) findViewById(R.id.timedimmed);
        mDateDim = (TextView) findViewById(R.id.datedimmed);
        randomness = (TextView) findViewById(R.id.randomness);
        randomnessDim = (TextView) findViewById(R.id.randomnessdim);
        mDev = (TextView) findViewById(R.id.dev);
        mDevDim = (TextView) findViewById(R.id.devdim);
        mCasual = (TextView) findViewById(R.id.LogoText);
        mCasualDim = (TextView) findViewById(R.id.LogoTextDim);
        mBackLogo = (ImageView) findViewById(R.id.logo);

    }

    @Override
    protected void onDestroy() { //Always unregister listeners to be courtious on battery life
        super.onDestroy();
        unregisterReceivers();
    }

    private void unregisterReceivers() {
        receiversSet = false;
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mDateInfoReceiver);
        unregisterReceiver(mBattInfoReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onScreenDim() {

    }

    @Override
    public void onScreenAwake() {

    }

    @Override
    public void onWatchFaceRemoved() {
        //unregisterReceivers();
    }

    @Override
    public void onScreenOff() {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

    }

    public void setPrimaryText(String s) {
        if (s.isEmpty()) {
            s = "CASUAL";
        }
        mCasual.setText(s);
        mCasualDim.setText(s);
    }

    public void setSecondaryText(String s) {
        if (s.isEmpty()) {
            s = "DEV";
        }
        mDev.setText(s);
        mDevDim.setText(s);
        if (s.toLowerCase().equals("DEV".toLowerCase())) {
            randomness.setVisibility(View.VISIBLE);
            randomnessDim.setVisibility(View.INVISIBLE);
        } else {
            randomness.setVisibility(View.GONE);
            randomnessDim.setVisibility(View.GONE);

        }
    }


    private void batteryLevelChanged(int level) {
        //   battery.setText(level+"%");
    }

}


