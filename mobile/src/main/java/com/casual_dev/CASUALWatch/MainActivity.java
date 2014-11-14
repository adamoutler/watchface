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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.casual_dev.casualmessager.WatchMessaging;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.casual_dev.casualmessager.Message.ITEMS;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "CASUALMainActivity";
    Button sendText;
    ImageView backgroundImage;
    EditText topText;
    EditText bottomText;
    WatchMessaging mo;
    ProgressBar pb;
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
        bottomText = (EditText) findViewById(R.id.userMessage2);
        topText = (EditText) findViewById(R.id.userMessage);
        pb=(ProgressBar) findViewById(R.id.progressBar);
        topText.setText(mo.getObject(ITEMS.TOPTEXTTAG, String.class));
        bottomText.setText(mo.getObject(ITEMS.BOTTOMTEXTTAG, String.class));
        pb.setProgress(0);
        pb.setVisibility(View.GONE);
        findViewById(R.id.backgroundpreview).setOnClickListener(handler);
        findViewById(R.id.sendtext).setOnClickListener(handler);
        Log.d(TAG, "text:" + mo.getObject(ITEMS.TOPTEXTTAG, String.class));
        backgroundImage=(ImageView) findViewById(R.id.backgroundpreview);

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

    Thread sendAction(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mApiClient, encodeDataRequest(mo));
                DataApi.DataItemResult dr=pendingResult.await();
                DataItem di= dr.getDataItem();
                Log.d(TAG,"DATA ITEM. Listing data items.");
                Map<String, DataItemAsset> m=di.getAssets();


                for (String item: m.keySet()){

                    Log.d(TAG,"DATA ITEM"+item +"value:"+m.get(item));
                }
                Log.d(TAG,"DATA ITEM"+di.toString());
                try {
                    mo.store(); //store the data
                    runOnUiThread(  //for setting the progress bar
                            new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(0);
                                    pb.setVisibility(View.GONE);

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.sendtext:
                    pb.setProgress(-1);
                    pb.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Sending:"+ topText.getText().toString() +" and "+ bottomText.getText().toString());

                    mo.putObject(ITEMS.BOTTOMTEXTTAG, bottomText.getText().toString());
                    mo.putObject(ITEMS.TOPTEXTTAG, topText.getText().toString());
                    sendAction().start();
                    break;
                case R.id.backgroundpreview:
                    // doStuff
                    Intent intent=new Intent(MainActivity.this, ImageGrabber.class);
                    Bundle bundle = new Bundle();
                    Uri uri=Uri.parse(new File(getFilesDir(),"tempFile").getAbsolutePath());

                    intent.setDataAndType(uri,"image/*");

                    MainActivity.this.startActivityForResult(intent, 0, bundle);


                    break;
            }
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && data != null)
        {
            Log.v("TAG", data.getStringExtra("Note"));
            if(resultCode == RESULT_OK)
            {
                Log.d(TAG,"result given");
            }
            if (resultCode == RESULT_CANCELED)
            {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
