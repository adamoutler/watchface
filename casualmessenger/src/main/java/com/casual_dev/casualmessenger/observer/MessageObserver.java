package com.casual_dev.casualmessenger.observer;

import com.casual_dev.casualmessenger.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adamoutler on 11/13/14.
 */
public class MessageObserver {

    static List<MessageInterface> connectedActivities = new ArrayList<>();

    /**
     * Register with the Observer
     *
     * @param messageInterface The implementing class to be notified upon receipt of a message.
     */
    static public void connect(MessageInterface messageInterface) {
        connectedActivities.add(messageInterface);
    }

    /**
     * A messageInterface may be manually disconnected. However, it will automatically disconnect.
     * @param messageInterface
     */
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
        /**
         * Upon receipt of a Message Object, onMessageReceived will pass a message object to any
         * implementing class which is registered with the MessageObserver.
         * @param message a Message Object containing the message passed from the other side of the
         *                connection.
         */
        public void onMessageReceived(Message message);

    }


}
