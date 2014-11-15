package com.casual_dev.casualmessenger.observer;

import com.casual_dev.casualmessenger.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adamoutler on 11/13/14.
 */
public class MessageObserver {

    static List<MessageInterface> connectedActivities = new ArrayList<>();

    static public void connect(MessageInterface messageInterface) {
        connectedActivities.add(messageInterface);
    }

    static public void disconnect(MessageInterface messageInterface) {
        connectedActivities.remove(messageInterface);
    }

    static public void newMessage(Message messaging) {
        for (MessageInterface activity : connectedActivities) {
            try {
                activity.onMessageReceived(messaging);
            } catch (Exception e) {
                e.printStackTrace();
                connectedActivities.remove(activity);
            }
        }
    }


    public interface MessageInterface {

        public void onMessageReceived(Message message);

    }


}
