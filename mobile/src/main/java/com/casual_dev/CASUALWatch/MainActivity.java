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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.casual_dev.mobilewearmessaging.WatchMessaging;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

import static com.casual_dev.mobilewearmessaging.Message.ITEMS;



public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "CASUALMainActivity";
    WatchMessaging mo;
    Button sendText;
    EditText topText;
    EditText bottomText;
    //Uri backgroundURI;

    GoogleApiClient mApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        mo = new WatchMessaging(getFilesDir().getAbsolutePath());

        setWidgets();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initGoogleApiClient();

    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    private void setWidgets() {
        sendText = (Button) findViewById(R.id.sendtext);
        bottomText = (EditText) findViewById(R.id.userMessage2);
        topText = (EditText) findViewById(R.id.userMessage);

        try {
            Log.d(TAG,"Loading data into table");
            mo.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Object o:mo.getTable().keySet()){
            Log.d(TAG,"Table Information: " +o.toString()+" mo.getTable.get+--  +mo.getObject-"+mo.getObject((ITEMS)o,String.class)+"-");
        }
        topText.setText(mo.getObject(ITEMS.TOPTEXTTAG,String.class));
        bottomText.setText(mo.getObject(ITEMS.BOTTOMTEXTTAG,String.class));



        Log.d(TAG, "text:" + mo.getObject(ITEMS.TOPTEXTTAG,String.class));
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG , topText.getText().toString());
                Log.d(TAG , bottomText.getText().toString());

                mo.putObject(ITEMS.BOTTOMTEXTTAG, bottomText.getText().toString());
                mo.putObject(ITEMS.TOPTEXTTAG,topText.getText().toString());

                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mApiClient, encodeDataRequest(mo));
                try {
                    mo.store();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public PutDataRequest encodeDataRequest(WatchMessaging mo) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(WatchMessaging.getMessageDataPath());
        dataMap.getDataMap().putString(WatchMessaging.TABLENAME,mo.getTableJSON());
        Log.d(TAG ,"Table"+dataMap.getDataMap().get(WatchMessaging.TABLENAME));
        return dataMap.asPutDataRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mo.store();
        } catch (IOException e) {
        }
    }
}
