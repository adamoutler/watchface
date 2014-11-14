package com.casual_dev.casualmessager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/**
 * Created by adamoutler on 11/9/14.
 */
public class Storage {
    static void storeMap(WatchMessaging watchMessaging) throws IOException {
        ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(new File(watchMessaging.dataFolder, WatchMessaging.STORAGE)));
        file.writeObject(watchMessaging.getTable());
    }

    static Hashtable<Message.ITEMS, Object> loadMap(WatchMessaging watchMessaging) throws IOException, ClassNotFoundException {
        ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File(watchMessaging.dataFolder, WatchMessaging.STORAGE)));
        watchMessaging.setTable((Hashtable<Message.ITEMS,Object>) file.<Hashtable<Message.ITEMS,String>>readObject());
        return watchMessaging.getTable();
    }
}
