package com.casual_dev.CASUALWatch.service;

import android.net.Uri;
import android.util.Log;

import com.casual_dev.CASUALWatch.digital.DigitalWatchfaceActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by adamoutler on 11/3/14.
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "DataLayerSample";
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
                    event.getDataItem().getUri().getPath().equals("/CDEVMessage")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                String words = dataMapItem.getDataMap().getString("CDEVMessage");
                Log.d("String Received:", words);
                if (DigitalWatchfaceActivity.getInstance() != null) {
                    updateDigitalWatchFaceText(words);
                }
            }

            // Send the RPC
            Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
                    DATA_ITEM_RECEIVED_PATH, payload);
        }
    }

    private void updateDigitalWatchFaceText(final String words) {
        DigitalWatchfaceActivity.
                getInstance().
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                DigitalWatchfaceActivity.getInstance().setPrimarySecondaryText(words.split(" "));

                            }
                        }

                );
    }
}