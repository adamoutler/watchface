package com.casual_dev.CASUALWatch.analog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.casual_dev.CASUALWatch.R;
import com.casual_dev.CASUALWatch.widget.WatchFaceLifecycle;
import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.WatchMessaging;
import com.casual_dev.casualmessenger.observer.MessageObserver;
import com.casual_dev.casualmessenger.user_types.SerializableImage;
import com.twotoasters.watchface.gears.activity.GearsWatchfaceActivity;
import com.twotoasters.watchface.gears.widget.IWatchface;

public class AnalogWatchfaceActivity extends GearsWatchfaceActivity implements WatchFaceLifecycle.Listener, MessageObserver.MessageInterface {

    static AnalogWatchfaceActivity instance;

    TextView mCASUAL;
    TextView mDev;
    ImageView mBackground;

    public static AnalogWatchfaceActivity getInstance() {
        return instance;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.analog_watchface;
    }

    @Override
    protected IWatchface getWatchface() {
        return (IWatchface) findViewById(R.id.watchface);
    }


    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        instance = this;
        setWidgets();
        MessageObserver.connect(this);

    }

    private void setWidgets() {

        mCASUAL = (TextView) findViewById(R.id.analogtoptext);
        mDev = (TextView) findViewById(R.id.analogbottomtext);
        mBackground = (ImageView) findViewById(R.id.background);
        WatchMessaging wm = new WatchMessaging(getFilesDir().getAbsolutePath());
        try {
            wm.load();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setDataItems(wm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onScreenDim() {

    }

    @Override
    public void onScreenAwake() {

    }

    @Override
    public void onWatchFaceRemoved() {
        MessageObserver.disconnect(this);
    }

    @Override
    public void onScreenOff() {

    }


    public void setDataItems(WatchMessaging wm) {
        setPrimaryText(wm.getObject(Message.ITEMS.TOPTEXTTAG, String.class));
        setSecondaryText(wm.getObject(Message.ITEMS.BOTTOMTEXTTAG, String.class));
        SerializableImage si = wm.getObject(Message.ITEMS.BACKGROUNDIMAGE, SerializableImage.class);
        if (si != null && si.length() > 1) {
            mBackground.setImageBitmap(si.getImage());
        } else {
            mBackground.setImageResource(0);
        }
    }

    public void setBackground(SerializableImage si) {
        if (si != null && si.length() > 1) {
            mBackground.setImageBitmap(si.getImage());
        } else {
            mBackground.setImageResource(0);
        }
    }
    public void setPrimaryText(String s) {
        if (null == s || s.isEmpty()) {
            s = "CASUAL";
        }
        mCASUAL.setText(s);
    }

    public void setSecondaryText(String s) {
        if (null == s || s.isEmpty()) {
            s = "DEV";
        }
        mDev.setText(s);

    }


    @Override
    public void onMessageReceived(final Message message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Message.ITEMS i : message.keySet()) {
                    switch (i) {
                        case TOPTEXTTAG:
                            setPrimaryText(message.get(Message.ITEMS.TOPTEXTTAG, String.class));
                            break;
                        case BOTTOMTEXTTAG:
                            setSecondaryText(message.get(Message.ITEMS.BOTTOMTEXTTAG, String.class));
                            break;

                        case BACKGROUNDIMAGE:
                            setBackground(message.get(Message.ITEMS.BACKGROUNDIMAGE, SerializableImage.class));
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
