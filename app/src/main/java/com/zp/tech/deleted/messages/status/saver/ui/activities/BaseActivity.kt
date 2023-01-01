package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.zp.tech.deleted.messages.status.saver.notificationService.MediaService
import com.zp.tech.deleted.messages.status.saver.notificationService.NotificationMediaService
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted
import java.util.*


abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {

    var binding: Binding? = null
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    fun setLayoutResource(resourceId: Int) {
        binding = DataBindingUtil.setContentView(this, resourceId)
    }

    open fun launchNotificationIntent() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    open fun isWhatsNotiServiceRunning(): Boolean {
        for (equals in NotificationManagerCompat.getEnabledListenerPackages(this)) {
            if (equals == packageName) {
                return true
            }
        }
        return false
    }

    fun startNotificationService() {
        if (!isWhatsNotiServiceRunning()) {
            if (!isServiceRunning(NotificationMediaService::class.java)) {
                startService(Intent(this, NotificationMediaService::class.java))
            }
            tryReconnectService()
        }
    }

    private fun startMediaService() {
        if (isPermissionGranted()) {
            if (!isServiceRunning(MediaService::class.java)) {
               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this, MediaService::class.java))
                }
                else {
                    startService(Intent(this, MediaService::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startMediaService()
    }

    fun showDialog(message: String) {
        MaterialAlertDialogBuilder(this).setTitle("Message").setMessage(message)
            .setPositiveButton(
                "Ok"
            ) { _, _ -> }.show()
    }

    fun isAppInstalled(packageName: String?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            true
        } else {
            try {
                packageManager.getPackageInfo(packageName!!, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
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

    open fun tryReconnectService() {
        toggleNotificationListenerService()
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationListenerService.requestRebind(
                ComponentName(
                    applicationContext,
                    NotificationMediaService::class.java
                )
            )
        }
    }


    @SuppressLint("WrongConstant")
    private fun toggleNotificationListenerService() {
        val packageManager = packageManager
        packageManager.setComponentEnabledSetting(
            ComponentName(
                this,
                NotificationMediaService::class.java
            ), 2, 1
        )
        packageManager.setComponentEnabledSetting(
            ComponentName(
                this,
                NotificationMediaService::class.java
            ), 1, 1
        )
    }

    fun isNotificationServiceEnabled(): Boolean {
        try {
            val pkgName = packageName
            val flat: String = Settings.Secure.getString(
                contentResolver,
                ENABLED_NOTIFICATION_LISTENERS
            )
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":").toTypedArray()
                for (i in names.indices) {
                    val cn = ComponentName.unflattenFromString(names[i])
                    cn?.let {
                        if (TextUtils.equals(pkgName, it.packageName)) {
                            return true
                        }
                    }
                }
            }
            return false

        } catch (exp: Exception) {
            return false
        } catch (ep: NullPointerException) {
            return false
        }
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setLanguage(langCode: String) {
        val config: Configuration = resources.configuration
        val locale: Locale = if (langCode.contains("-")) {
            val split = langCode.split("-")
            Locale(split[0], split[1])
        } else {
            Locale(langCode)
        }
        Locale.setDefault(locale)
        config.setLocale(locale)
        this.resources.updateConfiguration(
            config,
            this.resources.displayMetrics
        )
    }

    fun logEvent(event:String){
        val params = Bundle()
        params.putString("SCREEN_NAME", event)
        mFirebaseAnalytics!!.logEvent("SCREEN_EVENT", params)
    }
}