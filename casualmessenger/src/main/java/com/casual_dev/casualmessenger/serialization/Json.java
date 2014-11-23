package com.casual_dev.casualmessenger.serialization;

import com.casual_dev.casualmessenger.Message;
import com.google.gson.Gson;

/**
 * Created by adamoutler on 11/15/14.
 */
public class Json {
    static Gson gson = new Gson();

    public static String encode(Message ht) {
        return gson.toJson(ht);
    }

    public static Message decode(String s) {
        return gson.fromJson(s, Message.class);
    }
}
