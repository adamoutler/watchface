package com.casual_dev.casualmessenger.observer;

import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.WatchMessaging;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

/**
 * Created by adamoutler on 11/14/14.
 */
public class MessageReceiver {
    public final static String TAG = "MessageReceiver";

    public void receiveMessage(GoogleApiClient googleApiClient, DataEvent event, String filesDir, byte[] payload) {
        WatchMessaging delivery = new WatchMessaging(filesDir);
        try {
            delivery.load();
        } catch (ClassNotFoundException e) {

        }
        decodeDataRequest(DataMapItem.fromDataItem(event.getDataItem()), delivery);
        try {
            delivery.store();

        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageObserver.newMessage(delivery);
        // Send the RPC
        event.getDataItem().getUri().getHost();
        Wearable.MessageApi.sendMessage(googleApiClient, event.getDataItem().getUri().getHost(),
                WatchMessaging.getMessageDataPath(), payload);
    }

    Message decodeDataRequest(DataMapItem delivery, WatchMessaging wm) {
        if (null != delivery.getDataMap().getString(WatchMessaging.TABLENAME)) {
            wm.setTableJSON(delivery.getDataMap().getString(WatchMessaging.TABLENAME));
        }
        return wm.getMessage();

    }
}
