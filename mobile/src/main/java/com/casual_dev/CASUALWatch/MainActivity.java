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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.casual_dev.libshareddatacasualwatch.CustomizeWatchMessagingObject;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    Button sendText;
    EditText topText;
    EditText bottomText;
    Uri backgroundURI;

    GoogleApiClient mApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
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
        topText = (EditText) findViewById(R.id.userMessage);
        bottomText = (EditText) findViewById(R.id.userMessage2);
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomizeWatchMessagingObject mo = new CustomizeWatchMessagingObject();
                mo.setTopText(topText.getText().toString());
                mo.setBottomText(bottomText.getText().toString());
                mo.setBackgroundImage(backgroundURI);

                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mApiClient, encodeDataRequest(mo));

            }
        });
    }

    public PutDataRequest encodeDataRequest(CustomizeWatchMessagingObject mo) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(CustomizeWatchMessagingObject.MESSAGEDATAPATH);
        dataMap.getDataMap().putString(mo.getBottomText().getName(), mo.getBottomText().getValue());
        dataMap.getDataMap().putString(mo.getTopText().getName(), mo.getTopText().getValue());
        dataMap.getDataMap().putString(mo.getBottomText().getName(), mo.getBottomText().getValue());
        return dataMap.asPutDataRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
