package com.casual_dev.CASUALWatch.analog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.casual_dev.CASUALWatch.R;
import com.twotoasters.watchface.gears.widget.IWatchface;
import com.twotoasters.watchface.gears.widget.Watch;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

public class AnalogWatchfaceActions extends FrameLayout implements IWatchface {

    final static String TAG = AnalogWatchfaceActions.class.getSimpleName();
    static Bitmap watch_bg_normal;
    static Bitmap watch_bg_dimmed;
    static Bitmap overlay_shadow_dimmed;
    static Bitmap overlay_shadow_normal;
    static Bitmap hand_hour_dimmed;
    static Bitmap hand_minute_normal;
    static Bitmap hand_minute_dimmed;
    static Bitmap hand_hour_normal;
    static Bitmap hand_second_dimmed;
    static Bitmap hand_second_normal;
    static AnalogWatchfaceActions instance;
    @InjectView(R.id.face)
    ImageView face;
    @InjectView(R.id.shadow_overlay)
    ImageView shadowOverlay;
    @InjectView(R.id.hand_hour)
    ImageView handHour;
    @InjectView(R.id.hand_minute)
    ImageView handMinute;
    @InjectView(R.id.hand_second)
    ImageView handSecond;
    private Watch mWatch;
    private boolean mInflated;
    private boolean mActive;
    private Animation animFade;


    public AnalogWatchfaceActions(Context context) {
        super(context);
        if (isInEditMode()) return;
        init(context, 0);
    }

    public AnalogWatchfaceActions(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        init(context, 0);
    }

    public AnalogWatchfaceActions(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
        init(context, defStyle);
    }

    public static AnalogWatchfaceActions getInstance() {
        return instance;
    }

    @DebugLog
    @SuppressWarnings("unused")
    private void init(Context context, int defStyle) {
        mWatch = new Watch(this);
        createResourceBitmaps();

    }

    @DebugLog
    @Override
    protected void onFinishInflate() {
        if (isInEditMode()) return;
        super.onFinishInflate();
        ButterKnife.inject(this, getRootView());
        mInflated = true;
    }

    @DebugLog
    @Override
    public void onAttachedToWindow() {
        if (isInEditMode()) return;
        super.onAttachedToWindow();
        mWatch.onAttachedToWindow();
    }

    @DebugLog
    @Override
    public void onDetachedFromWindow() {
        if (isInEditMode()) return;
        super.onDetachedFromWindow();
        mWatch.onDetachedFromWindow();
    }

    private void rotateHands(int hour, int minute, int second) {
        int rotHr = (int) (30 * hour + 0.5f * minute);
        int rotMin = 6 * minute;
        int rotSec = 6 * second;

        handHour.setRotation(rotHr);
        handMinute.setRotation(rotMin);
        handSecond.setRotation(rotSec);
    }

    @Override
    public void onTimeChanged(Calendar time) {
        int hr = time.get(Calendar.HOUR_OF_DAY) % 12;
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);

        rotateHands(hr, min, sec);
        invalidate();
    }

    @Override
    @DebugLog
    public void onActiveStateChanged(boolean active) {
        if (isInEditMode()) return;
        this.mActive = active;
        setImageResources();
    }

    @Override
    public boolean handleSecondsInDimMode() {
        return false;
    }


    private void createResourceBitmaps() {
        watch_bg_normal = BitmapFactory.decodeResource(getResources(), R.drawable.watch_bg_normal);
        watch_bg_dimmed = BitmapFactory.decodeResource(getResources(), R.drawable.watch_bg_dimmed);
        overlay_shadow_dimmed = BitmapFactory.decodeResource(getResources(), R.drawable.overlay_shadow_dimmed);
        overlay_shadow_normal = BitmapFactory.decodeResource(getResources(), R.drawable.overlay_shadow_normal);
        hand_hour_dimmed = BitmapFactory.decodeResource(getResources(), R.drawable.hand_hour_dimmed);
        hand_hour_normal = BitmapFactory.decodeResource(getResources(), R.drawable.hand_hour_normal);
        hand_minute_dimmed = BitmapFactory.decodeResource(getResources(), R.drawable.hand_minute_dimmed);
        hand_minute_normal = BitmapFactory.decodeResource(getResources(), R.drawable.hand_minute_normal);
        hand_second_dimmed = BitmapFactory.decodeResource(getResources(), R.drawable.hand_second_dimmed);
        hand_second_normal = BitmapFactory.decodeResource(getResources(), R.drawable.hand_second_normal);
    }


    @DebugLog
    private void setImageResources() {
        if (mInflated) {
            crossfade(mActive, face, watch_bg_normal, watch_bg_dimmed);
            crossfade(mActive, shadowOverlay, overlay_shadow_dimmed, overlay_shadow_normal);
            crossfade(mActive, handHour, hand_hour_dimmed, hand_hour_normal);
            crossfade(mActive, handMinute, hand_minute_dimmed, hand_minute_normal);
            crossfade(mActive, handSecond, hand_second_dimmed, hand_second_normal);
            handSecond.setVisibility(mActive ? View.VISIBLE : View.INVISIBLE);
        }
    }


    private BitmapDrawable crossfade(boolean transitionToFirstImage, final ImageView v, Bitmap firstImg, Bitmap secondImg) {
        Log.d(TAG, "fade " + (transitionToFirstImage ? "In" : "out") + " requested.");

        BitmapDrawable[] layers = new BitmapDrawable[2];

        layers[0] = new BitmapDrawable(getResources(), transitionToFirstImage ? secondImg : firstImg);
        layers[1] = new BitmapDrawable(getResources(), transitionToFirstImage ? firstImg : secondImg);

        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
        v.setImageDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(1000);
        return layers[0];
    }
}