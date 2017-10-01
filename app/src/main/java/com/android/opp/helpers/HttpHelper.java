package com.android.opp.helpers;

/**
 * Created by camilo on 1/7/17.
 */

public class HttpHelper {
    String urlApi ="http://13.59.147.124/opp/public/api/";
    String urlAsset ="http://13.59.147.124/opp/public/uploads/";
    String urlAssetThumbail ="http://13.59.147.124/opp/public/thumbnail/";
    String urlUserAssetThumbail ="http://13.59.147.124/opp/public/user/thumbnail/";

    public String getUrlApi(){
        return urlApi;
    }
    public String getAssetUrl(){
        return urlAsset;
    }

    public String getUrlThumbail(){
        return urlAssetThumbail;
    }
    public String getUserUrlThumbail(){
        return urlUserAssetThumbail;
    }
}
