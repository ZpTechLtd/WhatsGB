package com.zp.tech.deleted.messages.status.saver.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityPermissionsBinding
import com.zp.tech.deleted.messages.status.saver.notificationService.NotificationMediaService.OBSERVER_INTENT
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted
import com.zp.tech.deleted.messages.status.saver.utils.requestStoragePermission

class PermissionsActivity : BaseActivity<ActivityPermissionsBinding>() {
    val adsManager = AdsManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_permissions)

        binding!!.btnStart.setOnClickListener {
            if (isNotificationServiceEnabled() && isPermissionGranted()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showMessage("Please grant the permissions")
            }
        }

        binding!!.btnNotification.setOnClickListener {
            if (!isNotificationServiceEnabled()) {
                launchNotificationIntent()
            }
        }

        binding!!.btnStorage.setOnClickListener {
            if (!isPermissionGranted()) {
                requestStoragePermission()
            }
        }
        adsManager.loadNativeBannerMax(binding!!.relAds)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            showMessage("Permission Denied by user")
        }
    }

    override fun onResume() {
        super.onResume()
        binding!!.btnNotification.isEnabled = !isNotificationServiceEnabled()
        binding!!.btnNotification.text = if (isNotificationServiceEnabled()) "Granted" else "Grant"
        binding!!.btnStorage.isEnabled = !isPermissionGranted()
        binding!!.btnStorage.text = if (isPermissionGranted()) "Granted" else "Grant"
    }
}