package com.casual_dev.CASUALWatch;

import android.util.Log;

import com.casual_dev.mobilewearmessaging.Message;
import com.casual_dev.mobilewearmessaging.WatchMessaging;

import java.io.IOException;

/**
 * Created by adamoutler on 11/11/14.
 */
public class WearMessenger {
    WatchMessaging mo;
    final String TAG="WearMessenger";
    WearMessenger(String path){
        mo = new WatchMessaging(path);
        loadData();
    }

    private void loadData() {
        try {
            mo.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Object o:mo.getTable().keySet()){
            Log.d(TAG, "Table Information: " + o.toString() + " mo.getTable.get+--  +mo.getObject-" + mo.getObject((Message.ITEMS) o, String.class) + "-");
        }
    }
    public  WatchMessaging getComms(){
        return mo;
    }
}
