package com.casual_dev;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

;


/**
 * s
 * Created by adamoutler on 11/4/14.
 */
public class WatchMessaging {
    private static final String TAG = "CASUALWatchMessageObject";
    public final static String STORAGE = "STORAGE.obj";
    public final static String MESSAGEPATH = "/CDEVMessage";
    public final static String TABLENAME = "Fuzzywuzzy";
    public final String dataFolder;

    public enum ITEMS {
        TOPTEXTTAG,
        BOTTOMTEXTTAG,
        BACKGROUNDIMAGE,
    }

    Hashtable<ITEMS, String> table = new Hashtable<>();

    public WatchMessaging(String dataFolder) {
        this.dataFolder = dataFolder;
        createBlankTable();
        try {
            if (new File(dataFolder, STORAGE).exists() && new File(dataFolder, STORAGE).length() > 1) {
                Storage.loadMap(this);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createBlankTable() {
        for (ITEMS t : ITEMS.values()) {
            table.put(t, "");
        }
    }




    public Hashtable<ITEMS, String> getTable() {
        return table;
    }
    void setTable(Hashtable<ITEMS, String> table) {
        this.table=table;
    }

    public void store() throws IOException {
        Storage.storeMap(this);
    }
    public void load() throws IOException, ClassNotFoundException {
        Storage.loadMap(this);
    }




    public static String getMessageDataPath() {
        return MESSAGEPATH;
    }


    public void putObject(ITEMS itemName, Object itemValue) {
        table.put(itemName, new Gson().toJson(itemValue));
    }

    public String getTableJSON(){
        return new Gson().toJson(table);
    }

    public void setTableJSON(String json){
        table=new Gson().fromJson(json,Hashtable.class);
    }

    public <T> T getObject(ITEMS i, Class<T> c){
        try {
           return c.cast(new Gson().fromJson(table.get(i.name()), c));

        } catch (ClassCastException ex){
            ex.printStackTrace();
            return null;
        }


    }
}