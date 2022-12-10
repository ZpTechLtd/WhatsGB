package com.zp.tech.deleted.messages.status.saver

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.*
import androidx.multidex.MultiDexApplication
import com.applovin.sdk.AppLovinSdk
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import com.zp.tech.deleted.messages.status.saver.ads.PreferenceManager
import com.zp.tech.deleted.messages.status.saver.database.ScannerDB
import com.zp.tech.deleted.messages.status.saver.database.repository.MyDatabase
import com.zp.tech.deleted.messages.status.saver.database.repository.MyRepository
import com.zp.tech.deleted.messages.status.saver.notificationService.MediaService
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted
import java.util.*

private const val LOG_TAG = "MyApplication"

class BaseApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver, LifecycleEventObserver {

    private var scannerDB: ScannerDB? = null
    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null
    private val lifecycleEventObserver = LifecycleEventObserver { source, event ->
        if (event == Lifecycle.Event.ON_START) {
            currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
        }
    }

    override fun onCreate() {
        super.onCreate()
        this.registerActivityLifecycleCallbacks(this)
        EmojiManager.install(GoogleEmojiProvider())
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this)
        MobileAds.initialize(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
        appOpenAdManager = AppOpenAdManager()

    }

    val database by lazy { MyDatabase.getDatabase(this) }
    val repository by lazy { MyRepository(database.ChatDao()) }

    fun getScannerDB(): ScannerDB {
        if (scannerDB == null) {
            scannerDB = ScannerDB.getInstance(this@BaseApplication)
        }
        return scannerDB!!
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                PreferenceManager(this@BaseApplication).admobOpen,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
//                        Log.d(LOG_TAG, "onAdLoaded.")
//                        Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show()
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
//                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
//                        Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                }
            )
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener,
        ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

//            Log.d(LOG_TAG, "Will show ad.")

            appOpenAd!!.setFullScreenContentCallback(
                object : FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
//                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent.")
//                        Toast.makeText(activity,
//                            "onAdDismissedFullScreenContent",
//                            Toast.LENGTH_SHORT).show()

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    /** Called when fullscreen content failed to show. */
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowingAd = false
//                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)
//                        Toast.makeText(activity,
//                            "onAdFailedToShowFullScreenContent",
//                            Toast.LENGTH_SHORT).show()

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    /** Called when fullscreen content is shown. */
                    override fun onAdShowedFullScreenContent() {
//                        Log.d(LOG_TAG, "onAdShowedFullScreenContent.")
//                        Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
            )
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                Thread.sleep(5000)
                if (this@BaseApplication.isPermissionGranted()) {
                    if (!isServiceRunning(MediaService::class.java)) {
                        when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                                this.startForegroundService(Intent(this, MediaService::class.java))
                            }
                            else -> {
                                this.startService(Intent(this, MediaService::class.java))
                            }
                        }
                    }
                }
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Int.MAX_VALUE)
        for (runningServiceInfo in services) {
            if (runningServiceInfo.service.className == serviceClass.name) {
                return true
            }
        }
        return false
    }
}