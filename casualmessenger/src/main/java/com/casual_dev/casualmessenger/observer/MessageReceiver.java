package com.casual_dev.casualmessenger.observer;

import android.util.Log;

import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.WatchMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Handles message receipt from DataListenerService.  This class is instantiated upon validation of
 * messages from the Listner service.
 * Created by adamoutler on 11/14/14.
 */
public class MessageReceiver {
    public final static String TAG = "MessageReceiver";

    void receiveMessage(GoogleApiClient googleApiClient, DataEvent event, String filesDir, byte[] payload) {
        WatchMessaging delivery = new WatchMessaging(filesDir);
        try {
            delivery.load();
        } catch (ClassNotFoundException unused) {
            //unused   This wont happen
        }
        Message message = decodeDataRequest(googleApiClient, DataMapItem.fromDataItem(event.getDataItem()), delivery);
        try {
            delivery.store();

        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageObserver.newMessage(message);
        // Send the RPC
        event.getDataItem().getUri().getHost();
        Wearable.MessageApi.sendMessage(googleApiClient, event.getDataItem().getUri().getHost(),
                WatchMessaging.getMessageDataPath(), payload);
    }

    Message decodeDataRequest(GoogleApiClient googleApiClient, DataMapItem delivery, WatchMessaging wm) {


        if (null != delivery.getDataMap().getAsset((WatchMessaging.TABLENAME))) {
            Asset asset = delivery.getDataMap().getAsset(WatchMessaging.TABLENAME);
            if (asset == null) {
                throw new IllegalArgumentException("Asset must be non-null");
            }
            ConnectionResult result =
                    googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                    googleApiClient, asset).await().getInputStream();
            googleApiClient.disconnect();
            if (assetInputStream == null) {
                Log.w(TAG, "Requested an unknown Asset.");
                return null;
            }
            wm.setTableAsset(assetInputStream);
            if (!result.isSuccess()) {
                return null;
            }

        }
        return wm.getMessage();

    }
}
