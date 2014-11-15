package com.casual_dev.casualmessenger;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static com.casual_dev.casualmessenger.Message.ITEMS;

;


/**
 * s
 * Created by adamoutler on 11/4/14.
 */
public class WatchMessaging {
    public final static String STORAGE = "STORAGE.obj";
    public final static String MESSAGEPATH = "/CDEVMessage";
    public final static String TABLENAME = "Fuzzywuzzy";
    private static final String TAG = "CASUALWatchMessageObject";
    public final String dataFolder;
    Message m = new Message();


    public WatchMessaging(String dataFolder) {
        this.dataFolder = dataFolder;
        m = createBlankTable();
        try {
            if (new File(dataFolder, STORAGE).exists() && new File(dataFolder, STORAGE).length() > 1) {
                Storage.loadMap(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Hashtable<ITEMS, Object> getTableJson(String tableJson) {
        Gson gson = new Gson();
        return (Hashtable<ITEMS, Object>) gson.fromJson(tableJson, Hashtable.class);
    }

    public static String getMessageDataPath() {
        return MESSAGEPATH;
    }

    private Message createBlankTable() {
        Message tempTable = new Message();
        for (ITEMS t : ITEMS.values()) {
            tempTable.put(t, "");
        }
        return tempTable;
    }

    public Message getMessage() {
        return m;
    }

    public Hashtable<ITEMS, Object> getTable() {
        return m;
    }

    void setTable(Message m) {
        this.m = m;
    }

    public void store() throws IOException {
        Storage.storeMap(this);
    }

    public void load() throws ClassNotFoundException {
        try {
            Storage.loadMap(this);
        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (m == null) this.createBlankTable();
    }

    public String getTableJSON() {
        return new Gson().toJson(m);
    }

    public void setTableJSON(String json) {
        m = new Gson().fromJson(json, Message.class);
    }

    public <T> T getObject(ITEMS i, Class<T> c) {
        return m.getObject(i, c);
    }

    public void setObject(ITEMS i, Object o) {
        m.putObject(i, o);
    }


    public Thread send(final Context c, final WatchMessaging watchMessaging, final Runnable onFinished) {

        return new Thread(new Runnable() {
            @Override
            public void run() {
                GoogleApiClient apiClient = new GoogleApiClient.Builder(c)
                        .addApi(Wearable.API)
                        .build();

                apiClient.connect();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(apiClient, encodeDataRequest(watchMessaging));
                DataApi.DataItemResult dr = pendingResult.await();
                DataItem di = dr.getDataItem();
                Log.d(TAG, "DATA ITEM. Listing data items.");
                Map<String, DataItemAsset> m = di.getAssets();


                for (String item : m.keySet()) {

                    Log.d(TAG, "DATA ITEM" + item + "value:" + m.get(item));
                }
                Log.d(TAG, "DATA ITEM" + di.toString());
                onFinished.run();


            }
        });
    }

    public PutDataRequest encodeDataRequest(WatchMessaging mo) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(WatchMessaging.getMessageDataPath());
        dataMap.getDataMap().putString(WatchMessaging.TABLENAME, mo.getTableJSON());
        Log.d(TAG, "Table" + dataMap.getDataMap().get(WatchMessaging.TABLENAME));
        return dataMap.asPutDataRequest();
    }

}