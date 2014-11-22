package com.casual_dev.casualmessenger.user_defined;

import com.casual_dev.casualmessenger.user_types.SerializableImage;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Hashtable used for storage and for transfering data
 * Created by Adam on 11/20/2014.
 */
public class MessageDefinition extends Hashtable<MessageDefinition.ITEMS, Object> implements Serializable {

    /**
     * enum of TAGS and Object.class
     * Add serializable classes as needed under user_types
     */
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
