package com.zp.tech.deleted.messages.status.saver.ads;

import android.content.Context;
import android.content.SharedPreferences;
import com.zp.tech.deleted.messages.status.saver.R;

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String ADMOB_INTERSTITIAL = "admob_interstitial";
    private static final String ADMOB_native = "admob_native";
    private static final String ADMOB_OPEN = "admob_open";

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

    public void setAdmobInterstitial(String admobInterstitial) {
        editor.putString(ADMOB_INTERSTITIAL, admobInterstitial).commit();
    }

    public String getAdmobInterstitial() {
        return sharedPreferences.getString(ADMOB_INTERSTITIAL, context.getString(R.string.admob_interstitial));
    }

    public void setADMOB_native(String admob_native) {
        editor.putString(ADMOB_native, admob_native).commit();
    }

    public String getADMOB_native() {
        return sharedPreferences.getString(ADMOB_native, context.getString(R.string.admob_native));
    }

    public void setAdmobOpen(String admobOpen) {
        editor.putString(ADMOB_OPEN, admobOpen).commit();
    }

    public String getAdmobOpen() {
        return sharedPreferences.getString(ADMOB_OPEN, context.getString(R.string.admob_open));
    }
}
