package com.casual_dev.casualmessenger.observer;

import android.net.Uri;
import android.util.Log;

import com.casual_dev.casualmessenger.WatchMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by adamoutler on 11/3/14.
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "CASUALDataLayerService";
    public static DataLayerListenerService dataListener;


    /**
     * constructor establishes connection and sets the getInstance singleton.
     */
    public DataLayerListenerService() {
        dataListener = this;
    }


    public static DataLayerListenerService getInstance() {
        return dataListener;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
        ;

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        // Loop through the events and send a message
        /// to the node that created the data item.
        for (DataEvent event : events) {
            Uri uri = event.getDataItem().getUri();
            byte[] payload = uri.toString().getBytes();
            Log.d("DataItem", "payload:" + new String(payload));
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals(WatchMessaging.getMessageDataPath())) {
                new MessageReceiver().receiveMessage(googleApiClient, event, getFilesDir().getAbsolutePath(), payload);
            }
        }
    }
}