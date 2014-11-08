package com.casual_dev.CASUALWatch.service;

import android.net.Uri;
import android.util.Log;

import com.casual_dev.CASUALWatch.digital.DigitalWatchfaceActivity;
import com.casual_dev.CustomizeWatchMessagingObject;
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
                    event.getDataItem().getUri().getPath().equals(CustomizeWatchMessagingObject.getMessageDataPath())) {
                CustomizeWatchMessagingObject delivery = new CustomizeWatchMessagingObject();
                decodeDataRequest(DataMapItem.fromDataItem(event.getDataItem()), delivery);


                Log.d("String Received:", delivery.getTopText().getValue());
                if (DigitalWatchfaceActivity.getInstance() != null) {
                    updateDigitalWatchFaceText(delivery);
                }
            }

            // Send the RPC
            Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
                    DATA_ITEM_RECEIVED_PATH, payload);
        }
    }

    private void updateDigitalWatchFaceText(final CustomizeWatchMessagingObject delivery) {
        DigitalWatchfaceActivity.
                getInstance().
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                DigitalWatchfaceActivity d = DigitalWatchfaceActivity.getInstance();
                                d.setPrimaryText(delivery.getTopText().getValue());
                                d.setSecondaryText(delivery.getBottomText().getValue());
                            }
                        }

                );
    }

    public CustomizeWatchMessagingObject decodeDataRequest(DataMapItem delivery, CustomizeWatchMessagingObject mo) {
        if (null != delivery.getDataMap().getString(mo.getTopText().getKey())) mo.setTopText(delivery.getDataMap().getString(mo.getTopText().getKey()));
        if (null != delivery.getDataMap().getString(mo.getBottomText().getKey())) mo.setBottomText(delivery.getDataMap().getString(mo.getBottomText().getKey()));
        if (null != delivery.getDataMap().getString(mo.getBackgroundImage().getKey())) mo.setTopText(delivery.getDataMap().getString(mo.getBackgroundImage().getKey()));
        Log.d("CASUALWear","Top:"+ mo.getTopText().getKey()+ mo.getTopText().getValue()+", Bottom:"+mo.getBottomText().getKey()+mo.getBottomText().getValue());

        //if (null!=delivery.getDataMap().getAsset(TOPTEXTTAG).getUri())setBackgroundImage(delivery.getDataMap().getAsset(BACKGROUNDIMAGE).getUri());

        return mo;
    }
}