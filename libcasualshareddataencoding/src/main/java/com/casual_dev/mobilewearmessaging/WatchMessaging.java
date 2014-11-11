package com.casual_dev.mobilewearmessaging;


import com.google.gson.Gson;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import static com.casual_dev.mobilewearmessaging.Message.ITEMS;

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


    Hashtable<Message.ITEMS, Object> table = new Hashtable<>();

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




    public Hashtable<ITEMS, Object> getTable() {
        return table;
    }
    void setTable(Hashtable<ITEMS, Object> table) {
        this.table=table;
    }

    public void store() throws IOException {
        Storage.storeMap(this);
    }
    public void load() throws IOException, ClassNotFoundException {
        Storage.loadMap(this);
        if (table==null)this.createBlankTable();
    }




    public static String getMessageDataPath() {
        return MESSAGEPATH;
    }


    public String getTableJSON(){
        return new Gson().toJson(table);
    }

    public void setTableJSON(String json){
        table=new Gson().fromJson(json,Hashtable.class);
    }



    public void putObject(ITEMS itemName, Object itemValue) {
        if (! itemValue.getClass().getName().equals(Message.getClassOfItem(itemName).getName())){
            throw new ClassCastException("itemName was "+ Message.getClassOfItem(itemName).getName() +" but the value passed in was "+itemValue.getClass().getName());
        }
        table.put(itemName, itemValue);
    }

    /**
     * gets an object fom the table
     * @param i item to fetch
     * @param c class to return
     * @return
     */
    public <T>T getObject(ITEMS i, Class<T> c){

        try {
            Object o=table.get(i);

            if (o==null||o.hashCode()==0){
                 o=table.get(i.name());
                if (o==null||o.hashCode()==0) {
                    return null;
                }
            }
            if (!c.getName().equals(o.getClass().getName())){
                throw new ClassCastException("itemName is classified as a "+ Message.getClassOfItem(i).getName() +" but the Class passed in was "+ c.getName());
            }
            return (T) c.cast(o);
        } catch (ClassCastException ex){
            ex.printStackTrace();
            return null;
        }




    }


}