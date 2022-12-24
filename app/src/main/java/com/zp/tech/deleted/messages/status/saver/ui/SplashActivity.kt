package com.zp.tech.deleted.messages.status.saver.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.ads.PreferenceManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivitySplashBinding
import com.zp.tech.deleted.messages.status.saver.ui.activities.BaseActivity
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.ui.activities.PermissionsActivity
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var handlerConfiguration: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_splash)

        val adsManager=AdsManager(this@SplashActivity)
        val preferenceManager = PreferenceManager(this@SplashActivity)
        com.zp.tech.deleted.messages.status.saver.utils.PreferenceManager(this@SplashActivity)
            .getLanguageCode()
            ?.let { setLanguage(it) }
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        adsManager.loadNativeLarge(binding!!.addLayout)
        adsManager.loadAdmobInterstitial()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(0)
            .addOnSuccessListener(OnSuccessListener<Void?> {
                remoteConfig.fetchAndActivate()
                handlerConfiguration = Handler(Looper.getMainLooper())
                handlerConfiguration!!.postDelayed(Runnable {
                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media"))) {
                        preferenceManager.maxInterstitial =
                            remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media")} ")

                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media"))) {
                        preferenceManager.maxNativeSmall =
                            remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media")} ")

                    if (!TextUtils.isEmpty(remoteConfig.getString("max_banner_Noti_and_media"))) {
                        preferenceManager.maxBanner =
                            remoteConfig.getString("max_banner_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("max_banner_Noti_and_media")} ")

                    if (!TextUtils.isEmpty(remoteConfig.getString("admob_inters_noti_and_media"))) {
                        preferenceManager.admobInterstitial =
                            remoteConfig.getString("admob_inters_noti_and_media")
                    }

//                    Log.d("TAG", "onCreate: admob_interstitial="+remoteConfig.getString("admob_inters_noti_and_media"))
                    if (!TextUtils.isEmpty(remoteConfig.getString("admob_open_noti_and_med"))) {
                        preferenceManager.admobOpen =
                            remoteConfig.getString("admob_open_noti_and_med")
                    }
//                    Log.d("TAG", "onCreate: Admob open="+remoteConfig.getString("admob_open_noti_and_med"))
                    if (!TextUtils.isEmpty(remoteConfig.getString("admob_natlar_noti_med"))) {
                        preferenceManager.admoB_native =
                            remoteConfig.getString("admob_natlar_noti_med")
                    }

//                    Log.d("TAG", "onCreate: native_large="+remoteConfig.getString("admob_natlar_noti_med"))

                    if (!TextUtils.isEmpty(remoteConfig.getString("admob_sb_noti_and_med"))) {
                        preferenceManager.admobSmartBanner =
                            remoteConfig.getString("admob_sb_noti_and_med")
                    }

//                    Log.d("TAG", "onCreate: admob_sb_noti_and_med="+remoteConfig.getString("admob_sb_noti_and_med"))

                }, 3000)
            })

        Handler(Looper.myLooper()!!).postDelayed({
            if (isNotificationServiceEnabled() && isPermissionGranted()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, PermissionsActivity::class.java))
            }
            adsManager.showAdmobInterstitial()
            finish()
        }, 8000)
    }
}