package com.zp.tech.deleted.messages.status.saver.ui

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
import com.zp.tech.deleted.messages.status.saver.ads.PreferenceManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivitySplashBinding
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var handlerConfiguration: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_splash)


        val preferenceManager= PreferenceManager(this@SplashActivity)
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(0)
            .addOnSuccessListener(OnSuccessListener<Void?> {
                remoteConfig.fetchAndActivate()
                handlerConfiguration = Handler(Looper.getMainLooper())
                handlerConfiguration!!.postDelayed(Runnable {
                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media"))){
                        preferenceManager.maxInterstitial =
                            remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("MAX_INTERSTITIAL_Noti_and_media")} ")

                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media"))){
                        preferenceManager.maxNativeSmall =
                            remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("MAX_NATIVE_SMALL_Noti_and_media")} ")

                    if (!TextUtils.isEmpty(remoteConfig.getString("max_banner_Noti_and_media"))){
                        preferenceManager.maxBanner =
                            remoteConfig.getString("max_banner_Noti_and_media")
                    }
//                    Log.d("TAG", "onCreate:remote== ${remoteConfig.getString("max_banner_Noti_and_media")} ")

                }, 3000)
            })

        Handler(Looper.myLooper()!!).postDelayed({
            if (isNotificationServiceEnabled() && isPermissionGranted()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, PermissionsActivity::class.java))
            }
            finish()
        }, 8000)
    }
}