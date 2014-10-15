package com.bigandroidbbq;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;

/**
 * A listener for watch faces that have callbacks for the various screen states (dim, awake, and off)
 * as well as a callback for when the watch face is removed.
 * <p/>
 * Prefer composition over inheritance
 * <p/>
 * This was created by Paul Blundell based on the Unofficial Base WatchFace Activity by Tavon Gatling
 * https://gist.github.com/blundell/bba18a128214d5fa1250
 * https://gist.github.com/kentarosu/52fb21eb92181716b0ce
 * http://gist.github.com/PomepuyN/cdd821eca163a3279de2.
 */
public class WatchFaceLifecycle {

    private WatchFaceLifecycle() {
    }

    public static void attach(Activity activity, Bundle savedInstanceState, Listener listener) {
        RealWatchFaceLifecycleCallbacks.newInstance(activity, savedInstanceState, listener);
    }

    public interface Listener {
        /**
         * Used to detect when the watch is dimming.<br/>
         * Remember to make gray-scale versions of your watch face so they won't burn
         * and drain battery unnecessarily.
         */
        void onScreenDim();

        /**
         * Used to detect when the watch is not in a dimmed state.<br/>
         * This does not handle when returning "home" from a different activity/application.
         */
        void onScreenAwake();

        /**
         * Used to detect when a watch face is being removed (switched).<br/>
         * You can either do what you need here, or simply override {@code onDestroy()}.
         */
        void onWatchFaceRemoved();

        /**
         * When the screen is OFF due to "Always-On" being disabled.
         */
        void onScreenOff();

    }

    private static class RealWatchFaceLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, DisplayManager.DisplayListener {

        private final DisplayManager displayManager;
        private final Listener listener;

        public RealWatchFaceLifecycleCallbacks(DisplayManager displayManager, Listener listener) {
            this.displayManager = displayManager;
            this.listener = listener;
        }

        private static void newInstance(Activity activity, Bundle savedInstanceState, Listener listener) {
            DisplayManager displayManager = (DisplayManager) activity.getSystemService(Context.DISPLAY_SERVICE);
            RealWatchFaceLifecycleCallbacks watchFace = new RealWatchFaceLifecycleCallbacks(displayManager, listener);
            activity.getApplication().registerActivityLifecycleCallbacks(watchFace);
            watchFace.onActivityCreated(activity, savedInstanceState);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //  Set up the display manager and register a listener (this activity).
            displayManager.registerDisplayListener(this, null);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // not used
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // not used
        }

        @Override
        public void onDisplayAdded(int displayId) {
            //  In testing, this was never called, so the listener for this was removed.
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            listener.onWatchFaceRemoved();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            Display display = displayManager.getDisplay(displayId);
            if (display == null) {
                // No display found for this ID, treating this as an onScreenOff() but you could remove this line
                // and swallow this exception quietly. What circumstance means 'there is no display for this id'?
                listener.onScreenOff();
                return;
            }
            switch (display.getState()) {
                case Display.STATE_DOZING:
                    listener.onScreenDim();
                    break;
                case Display.STATE_OFF:
                    listener.onScreenOff();
                    break;
                default:
                    //  Not really sure what to so about Display.STATE_UNKNOWN, so we'll treat it as if the screen is normal.
                    listener.onScreenAwake();
                    break;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // not used
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // not used
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // not used
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            //  Unregister the listener. If you don't, even after the watch face is gone, it will still accept your callbacks.
            displayManager.unregisterDisplayListener(this);
        }
    }
}
