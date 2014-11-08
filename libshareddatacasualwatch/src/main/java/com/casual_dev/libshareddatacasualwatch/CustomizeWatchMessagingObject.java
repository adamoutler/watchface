package com.casual_dev.libshareddatacasualwatch;


import android.net.Uri;

import org.apache.http.NameValuePair;


/**
 * s
 * Created by adamoutler on 11/4/14.
 */
public class CustomizeWatchMessagingObject {
    final public static String MESSAGEDATAPATH = "/CDEVMessage";
    final public static String TOPTEXTTAG = "TOPTEXT";
    final public static String BOTTOMTEXTTAG = "BOTTOMTEXT";
    final public static String BACKGROUNDIMAGE = "BACKGROUNDIMAGE";
    private String topText = "";
    private String bottomText = "";
    private Uri backgroundImage = null;

    public CustomizeWatchMessagingObject() {

    }


    public NameValuePair getTopText() {
        return new NameValuePair() {
            @Override
            public String getName() {
                return TOPTEXTTAG;
            }

            @Override
            public String getValue() {
                return topText;
            }
        };
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public NameValuePair getBottomText() {
        return new NameValuePair() {
            @Override
            public String getName() {
                return BOTTOMTEXTTAG;
            }

            @Override
            public String getValue() {
                return bottomText;
            }
        };
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public Uri getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Uri backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
/**

 public PutDataRequest encodeDataRequest(){
 PutDataMapRequest dataMap = PutDataMapRequest.create(MESSAGEDATAPATH);
 dataMap.getDataMap().putString(getBottomText().getName(),getBottomText().getValue() );
 dataMap.getDataMap().putString(getTopText().getName(), getTopText().getValue());
 dataMap.getDataMap().putString(getBottomText().getName(),getBottomText().getValue());
 return dataMap.asPutDataRequest();
 }


 public CustomizeWatchMessagingObject decodeDataRequest(DataMapItem delivery){
 if (null!=delivery.getDataMap().getString(BOTTOMTEXTTAG)) setBottomText(delivery.getDataMap().getString(BOTTOMTEXTTAG));
 if (null!=delivery.getDataMap().getString(BACKGROUNDIMAGE)) setTopText(delivery.getDataMap().getString(TOPTEXTTAG));
 //if (null!=delivery.getDataMap().getAsset(TOPTEXTTAG).getUri())setBackgroundImage(delivery.getDataMap().getAsset(BACKGROUNDIMAGE).getUri());

 return this;
 }
 **/
}
