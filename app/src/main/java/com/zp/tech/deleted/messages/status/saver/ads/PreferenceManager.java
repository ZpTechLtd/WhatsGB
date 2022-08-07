package com.zp.tech.deleted.messages.status.saver.ads;

import android.content.Context;
import android.content.SharedPreferences;
import com.zp.tech.deleted.messages.status.saver.R;

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private static final String PRIVACY_POLICY = "privacy_policy";

    private static final String MAX_INTERSTITIAL="max_interstitial";
    private static final String MAX_NATIVE_SMALL="max_native_small";
    private static final String MAX_BANNER="max_banner";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("tune", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setMaxInterstitial(String maxInterstitial){
        editor.putString(MAX_INTERSTITIAL,maxInterstitial).commit();
    }

    public String getMaxInterstitial(){
        return sharedPreferences.getString(MAX_INTERSTITIAL,context.getString(R.string.max_interstitial));
    }

    public void setMaxNativeSmall(String maxNativeSmall){
        editor.putString(MAX_NATIVE_SMALL,maxNativeSmall).commit();
    }

    public String getMaxNativeSmall(){
        return sharedPreferences.getString(MAX_NATIVE_SMALL,context.getString(R.string.max_native_small));
    }

    public void setMaxBanner(String maxBanner){
        editor.putString(MAX_BANNER,maxBanner).commit();
    }

    public String getMaxBanner(){
        return sharedPreferences.getString(MAX_BANNER,context.getString(R.string.max_banner));
    }
}
