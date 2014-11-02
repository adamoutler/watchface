package com.casual_dev.CASUALWatch.analog;

import com.casual_dev.CASUALWatch.R;
import com.twotoasters.watchface.gears.activity.GearsWatchfaceActivity;
import com.twotoasters.watchface.gears.widget.IWatchface;

public class AnalogWatchfaceActivity extends GearsWatchfaceActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.analog_watchface;
    }

    @Override
    protected IWatchface getWatchface() {
        return (IWatchface) findViewById(R.id.watchface);
    }
}
