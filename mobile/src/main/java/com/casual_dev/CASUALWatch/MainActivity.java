package com.casual_dev.CASUALWatch; /**
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


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.casual_dev.casualmessenger.WatchMessaging;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;

import java.io.File;

import static com.casual_dev.casualmessenger.Message.ITEMS;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "CASUALMainActivity";
    Button sendText;
    ImageView backgroundImage;
    EditText topText;
    EditText bottomText;
    TextView mDefault;
    WatchMessaging watchMessaging;
    ProgressBar pb;
    //Uri backgroundURI;
    View.OnClickListener handler = new View.OnClickListener() {

        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.sendtext:
                    sendText();
                    break;
                case R.id.backgroundpreview:
                    // doStuff
                    Intent intent = new Intent(MainActivity.this, ImageGrabber.class);
                    Bundle bundle = new Bundle();
                    Uri uri = Uri.parse(new File(getFilesDir(), "tempFile").getAbsolutePath());

                    intent.setDataAndType(uri, "image/*");

                    MainActivity.this.startActivityForResult(intent, 0, bundle);


                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        watchMessaging = new WatchMessaging(getFilesDir().getAbsolutePath());


        setWidgets();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    private void setWidgets() {
        bottomText = (EditText) findViewById(R.id.userMessage2);
        topText = (EditText) findViewById(R.id.userMessage);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        topText.setText(watchMessaging.getObject(ITEMS.TOPTEXTTAG, String.class));
        bottomText.setText(watchMessaging.getObject(ITEMS.BOTTOMTEXTTAG, String.class));
        pb.setProgress(0);
        pb.setVisibility(View.GONE);
        findViewById(R.id.backgroundpreview).setOnClickListener(handler);
        findViewById(R.id.sendtext).setOnClickListener(handler);
        Log.d(TAG, "text:" + watchMessaging.getObject(ITEMS.TOPTEXTTAG, String.class));
        backgroundImage = (ImageView) findViewById(R.id.backgroundpreview);
        topText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendText();
            }
        });
    }

    public PutDataRequest encodeDataRequest(WatchMessaging mo) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(WatchMessaging.getMessageDataPath());
        dataMap.getDataMap().putString(WatchMessaging.TABLENAME, mo.getTableJSON());
        Log.d(TAG, "Table" + dataMap.getDataMap().get(WatchMessaging.TABLENAME));
        return dataMap.asPutDataRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    Thread sendAction() {
        return watchMessaging.send(this.getApplicationContext(), watchMessaging,
                new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(0);
                                pb.setVisibility(View.GONE);

                            }
                        });

                    }
                });


    }

    void sendText() {
        pb.setProgress(-1);
        pb.setVisibility(View.VISIBLE);
        Log.d(TAG, "Sending:" + topText.getText().toString() + " and " + bottomText.getText().toString());

        watchMessaging.setObject(ITEMS.BOTTOMTEXTTAG, bottomText.getText().toString());
        watchMessaging.setObject(ITEMS.TOPTEXTTAG, topText.getText().toString());
        sendAction().start();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            backgroundImage.setImageBitmap((Bitmap) data.getExtras().get("image"));

            Log.d(TAG, "result given");
        }
        if (resultCode == RESULT_CANCELED) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
