package com.zp.tech.deleted.messages.status.saver.notificationService

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted

class BootReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (!isServiceRunning(context, NotificationMediaService::class.java)) {
                context.startService(Intent(context, NotificationMediaService::class.java))
            }

            if (context.isPermissionGranted()){
                context.startService(Intent(context,MediaService::class.java))
            }
        }
    }

    private fun isServiceRunning(context: Context?, serviceClass: Class<*>): Boolean {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Int.MAX_VALUE)
        for (runningServiceInfo in services) {
            if (runningServiceInfo.service.className == serviceClass.name) {
                return true
            }
        }
        return false
    }
}

