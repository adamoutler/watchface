package com.casual_dev.casualmessenger;

import com.casual_dev.casualmessenger.Serialization.SerializableImage;

import java.util.Hashtable;

/**
 * Created by adamoutler on 11/10/14.
 */
public class Message extends Hashtable<Message.ITEMS, Object> {
    /**
     * gets an object fom the table
     *
     * @param i item to fetch
     * @param c used for type casting
     * @return Object corresponding to Item, casted as i.type()
     */

    @SuppressWarnings("unchecked unused")  //very well checked, thanks Android Studio
    public <T> T get(ITEMS i, Class<T> c) {
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

    public Object putObject(ITEMS itemName, Object itemValue) {
        if (values().contains(itemName)) {
            remove(itemName);
        }
        if (!itemValue.getClass().getName().equals(itemName.type().getName())) {
            throw new ClassCastException("itemName was " + itemName.type().getName() + " but the value passed in was " + itemValue.getClass().getName());
        }
        return put(itemName, itemValue);
    }

    private boolean nullTest(Object o) {
        return o == null || o.hashCode() == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ITEMS i : this.keySet()) {
            sb.append("Item:").append(i).append(" Value:").append(get(i));
        }
        return sb.toString();
    }


    public enum ITEMS {
        BACKGROUNDIMAGE(SerializableImage.class),
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
