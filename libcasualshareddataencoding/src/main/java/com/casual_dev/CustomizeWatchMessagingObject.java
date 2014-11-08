package com.casual_dev;


import com.sun.jndi.toolkit.url.Uri;

import java.util.AbstractMap;
import java.util.Hashtable;

;


/**
 * s
 * Created by adamoutler on 11/4/14.
 */
public class CustomizeWatchMessagingObject {

    private String topText = "";
    private String bottomText = "";
    private Uri backgroundImage = null;
    Hashtable table = new Hashtable();

    public enum TAG{
        TOPTEXTTAG,
        BOTTOMTEXTTAG,
        BACKGROUNDIMAGE,
    }

    public CustomizeWatchMessagingObject() {
    }


    public static String getMessageDataPath(){
        return "/CDEVMessage";
    }

    public AbstractMap.SimpleEntry<String,String> getTopText() {
        return new AbstractMap.SimpleEntry<String, String>(TAG.TOPTEXTTAG.name(), topText);
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public AbstractMap.SimpleEntry<String,String> getBottomText() {
        return new AbstractMap.SimpleEntry<String, String>(TAG.BOTTOMTEXTTAG.name(),bottomText);

    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public AbstractMap.SimpleEntry<String,Uri>  getBackgroundImage() {
        return new AbstractMap.SimpleEntry<String, Uri>(TAG.BACKGROUNDIMAGE.name(), backgroundImage);
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
