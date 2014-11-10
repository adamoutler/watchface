package com.casual_dev.CASUALWatch.service;

import android.net.Uri;
import android.util.Log;

import com.casual_dev.CASUALWatch.digital.DigitalWatchfaceActivity;
import com.casual_dev.WatchMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by adamoutler on 11/3/14.
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "CASUALDataLayerSample";
    private static final String START_ACTIVITY_PATH = "CDEVMessage";
    private static final String DATA_ITEM_RECEIVED_PATH = "/CDEVMessage";


    @Override

    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);


        final List<DataEvent> events = FreezableUtils
                .freezeIterable(dataEvents);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        // Loop through the events and send a message
        /// to the node that created the data item.
        for (DataEvent event : events) {
            Uri uri = event.getDataItem().getUri();

            // Get the node id from the host value of the URI
            String nodeId = uri.getHost();
            // Set the data of the message to be the bytes of the URI.
            byte[] payload = uri.toString().getBytes();


            Log.d("DataItem", "payload:" + new String(payload));
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals(WatchMessaging.getMessageDataPath())) {
                WatchMessaging delivery = new WatchMessaging(getFilesDir().getAbsolutePath());
                decodeDataRequest(DataMapItem.fromDataItem(event.getDataItem()), delivery);
                try {
                    delivery.store();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "top text "+delivery.getObject(WatchMessaging.ITEMS.TOPTEXTTAG, String.class));
                if (DigitalWatchfaceActivity.getInstance() != null) {
                    updateDigitalWatchFaceText(delivery);
                }
            }

            // Send the RPC
            Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
                    DATA_ITEM_RECEIVED_PATH, payload);
        }
    }

    private void updateDigitalWatchFaceText(final WatchMessaging delivery) {
        DigitalWatchfaceActivity.
                getInstance().
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                DigitalWatchfaceActivity d = DigitalWatchfaceActivity.getInstance();
                                d.setPrimaryText(delivery.getObject(WatchMessaging.ITEMS.TOPTEXTTAG, String.class));
                                d.setSecondaryText(delivery.getObject(WatchMessaging.ITEMS.BOTTOMTEXTTAG, String.class));
                            }
                        }

                );
    }

    public WatchMessaging decodeDataRequest(DataMapItem delivery, WatchMessaging mo) {
        if (null != delivery.getDataMap().getString(mo.TABLENAME)) mo.setTableJSON(delivery.getDataMap().getString(mo.TABLENAME));
        for (WatchMessaging.ITEMS i: WatchMessaging.ITEMS.values()){
            Log.d(TAG,"item:"+i.name()+" value:"+mo.getObject(i,String.class));
        }
        return mo;
    }
}