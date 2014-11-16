package com.casual_dev.CASUALWatch.digital;
/*Copyright 2014 Adam Outler

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.casual_dev.CASUALWatch.R;
import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.Serialization.SerializableImage;
import com.casual_dev.casualmessenger.observer.MessageObserver;

public class DigitalWatchfaceApp extends DigitalWatchfaceActions implements MessageObserver.MessageInterface {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        MessageObserver.connect(this);
    }

    @Override
    public void onMessageReceived(final Message message) {
        Log.d("CASUALWEAR", message.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Message.ITEMS i : message.keySet()) {
                    switch (i) {
                        case TOPTEXTTAG:
                            setPrimaryText(message.get(Message.ITEMS.TOPTEXTTAG, String.class));
                            break;
                        case BOTTOMTEXTTAG:
                            setSecondaryText(message.get(Message.ITEMS.BOTTOMTEXTTAG, String.class));
                            break;
                        case BACKGROUNDIMAGE:
                            SerializableImage si = message.get(Message.ITEMS.BACKGROUNDIMAGE, SerializableImage.class);

                            if (si != null && si.length() > 1) {
                                mBackLogo.setImageBitmap(si.getImage());
                            } else {
                                mBackLogo.setImageBitmap(BitmapFactory
                                        .decodeResource(getBaseContext().getResources(), R.drawable.digital_background));
                            }

                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
