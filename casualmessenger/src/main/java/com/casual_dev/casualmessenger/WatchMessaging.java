package com.casual_dev.casualmessenger;


import android.content.Context;
import android.util.Log;

import com.casual_dev.casualmessenger.Serialization.Json;
import com.casual_dev.casualmessenger.Serialization.Serializer;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import static com.casual_dev.casualmessenger.Message.ITEMS;


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

    @SuppressWarnings("unused")
    public static Message getTableJson(String tableJson) {
        return Json.decode(tableJson);
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
        return Json.encode(m);
    }

    public void setTableAsset(InputStream is) {

        Message message = new Message();

        try {
            byte[] b = convertInputStreamToByteArray(is);
            message = (Message) Serializer.deserialize(b);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (ITEMS i : message.keySet()) {
            if (m.keySet().contains(i)) {
                m.remove(i);
            }
            m.put(i, message.get(i));
        }

    }

    public byte[] convertInputStreamToByteArray(InputStream inputStream) {
        byte[] bytes = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte data[] = new byte[1024];
            int count;

            while ((count = inputStream.read(data)) != -1) {
                bos.write(data, 0, count);
            }

            bos.flush();
            bos.close();
            inputStream.close();

            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public <T> T getObject(ITEMS i, Class<T> c) {
        return m.get(i, c);
    }

    @SuppressWarnings("unused")
    public void setObject(ITEMS i, Object o) {
        m.putObject(i, o);
    }


    public Thread send(final Context c, final Message message, final Runnable onFinished) {

        return new Thread(new Runnable() {
            @Override
            public void run() {
                GoogleApiClient apiClient = new GoogleApiClient.Builder(c)
                        .addApi(Wearable.API)
                        .build();

                apiClient.connect();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(apiClient, encodeDataRequest(message));
                DataApi.DataItemResult dr = pendingResult.await();
                DataItem di = dr.getDataItem();
                Log.d(TAG, "DATA ITEM. Listing data items.");
                Map<String, DataItemAsset> m = di.getAssets();
                for (Message.ITEMS item : message.keySet()) {

                    Log.d(TAG, "DATA ITEM" + item + "value:" + m.get(item));
                }
                Log.d(TAG, "DATA ITEM" + di.toString());
                onFinished.run();


            }
        });
    }

    public PutDataRequest encodeDataRequest(Message message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(WatchMessaging.getMessageDataPath());
        try {
            dataMap.getDataMap().putAsset(WatchMessaging.TABLENAME, Asset.createFromBytes(Serializer.serialize(message)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (ITEMS i : message.keySet()) {
            sb.append("Item:").append(i).append(" Value:").append(message.get(i)).append("\n");
        }
        Log.d(TAG, "sending message" + sb.toString());
        return dataMap.asPutDataRequest();
    }

}