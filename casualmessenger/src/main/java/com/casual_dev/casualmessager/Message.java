package com.casual_dev.casualmessenger;

import android.graphics.Bitmap;

import java.util.Hashtable;

/**
 * Created by adamoutler on 11/10/14.
 */
public class Message extends Hashtable<Message.ITEMS, Object> {

    /**
     * gets an object fom the table
     *
     * @param i item to fetch
     * @return
     */
    public <T> T getObject(ITEMS i, Class<T> c) {

        Class expectedClass = i.type();
        Object actualObject = get(i);
        try {

            if (nullTest(actualObject)) {
                actualObject = get(i.name()); //json screws with table
                if (nullTest(actualObject)) {
                    return null;
                }
            }
            if (!actualObject.getClass().equals(expectedClass)) {
                throw new ClassCastException("itemName " + i + " is classified as a " + i.type().getName() + " but the object passed in was a " + actualObject.getClass().getName());
            }
            return (T) expectedClass.cast(actualObject);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    public void putObject(ITEMS itemName, Object itemValue) {
        if (values().contains(itemName)) {
            remove(itemName);
        }
        if (!itemValue.getClass().getName().equals(itemName.type().getName())) {
            throw new ClassCastException("itemName was " + itemName.type().getName() + " but the value passed in was " + itemValue.getClass().getName());
        }
        put(itemName, itemValue);
    }

    private boolean nullTest(Object o) {
        return o == null || o.hashCode() == 0;
    }

    public enum ITEMS {
        BACKGROUNDIMAGE(Bitmap.class),
        BOTTOMTEXTTAG(String.class),
        TOPTEXTTAG(String.class);

        Class myClass;

        ITEMS(Class c) {
            myClass = c;
        }

        public Class type() {
            return myClass;
        }
    }

}
