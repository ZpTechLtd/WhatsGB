package com.zp.tech.deleted.messages.status.saver.notificationService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MediaService : Service() {
    private var mediaObserverList: ArrayList<MediaFileObserver>? = null
    private var pathsList: ArrayList<String>? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        pathsList = ArrayList<String>()
        mediaObserverList = ArrayList<MediaFileObserver>()
        startObserving()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intent2: List<MediaFileObserver> = this.mediaObserverList!!
        if (intent2 != null) {
            for (mediaobserver in intent2) if (mediaobserver != null) {
                mediaobserver.stopWatching()
            }
        }
        startObserving()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "myServiceChannel",
                "My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "myServiceChannel")
            .setContentTitle("Service is running...")
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            //.setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }

    private fun startObserving() {
        if (this.isPermissionGranted()) {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/Whats Download/.cache/"
            )
            file.mkdirs()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                val whatsDirectories: List<String> = getPaths(NotificationMediaService.WHTAPP)
                val businessDirectories: List<String> =
                    getPaths(NotificationMediaService.WHTSBUSINESS)
                if (whatsDirectories.isNotEmpty()) {
                    pathsList!!.addAll(whatsDirectories)
                }
                if (businessDirectories.isNotEmpty()) {
                    pathsList!!.addAll(businessDirectories)
                }
            }
            if (!pathsList!!.isEmpty()) {
                for (directory in pathsList!!) {
                    Objects.requireNonNull(mediaObserverList!!)
                        .add(MediaFileObserver(directory, this))
                }
                if (!mediaObserverList!!.isEmpty()) {
                    for (observer in Objects.requireNonNull(mediaObserverList!!)) {
                        observer.startWatching()
                    }
                }
            }
        }
    }

    private fun getPaths(path: String): List<String> {
        val directoriesList: MutableList<String> = ArrayList()
        var file: File? = null
        file = if (path == NotificationMediaService.WHTAPP) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                File(Environment.getExternalStorageDirectory().absolutePath + "/Android/media/com.whatsapp/WhatsApp/Media/")
            } else {
                File(Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp/Media/")
            }
        } else {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                File(Environment.getExternalStorageDirectory().absolutePath + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/")
            } else {
                File(Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp Business/Media/")
            }
        }
        if (file.exists()) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (mediaPath in files) {
                    if (!mediaPath.absolutePath.contains("Statuses")) {
                        directoriesList.add(mediaPath.absolutePath + "/")
                    }

                    if (mediaPath.absolutePath.contains(
                            "Voice Notes",
                            ignoreCase = true
                        ) && mediaPath.isDirectory
                    ) {
                        val voiceNotes = mediaPath.listFiles()
                        if (!voiceNotes.isNullOrEmpty()) {
                            for (notes in voiceNotes) {
                                directoriesList.add(notes.absolutePath + "/")
                            }
                        }
                    }
                }
            }
        }
        return directoriesList
    }
}