package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityPermissionsBinding
import com.zp.tech.deleted.messages.status.saver.utils.IS_FROM_PERMISSIONS_SCREEN
import com.zp.tech.deleted.messages.status.saver.utils.isPermissionGranted
import com.zp.tech.deleted.messages.status.saver.utils.requestStoragePermission

class PermissionsActivity : BaseActivity<ActivityPermissionsBinding>() {
    val adsManager = AdsManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_permissions)

        binding!!.btnStart.setOnClickListener {
            if (isNotificationServiceEnabled() && isPermissionGranted()) {
                startActivity(Intent(this, LanguageActivity::class.java).putExtra(
                    IS_FROM_PERMISSIONS_SCREEN, true))
                finish()
            } else {
                showMessage(getString(R.string.please_grant_permissions))
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 1 || !grantResults.isNotEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            showMessage(getString(R.string.permission_denied))
        }
    }

    override fun onResume() {
        super.onResume()
        binding!!.btnNotification.isEnabled = !isNotificationServiceEnabled()
        binding!!.btnNotification.text =
            if (isNotificationServiceEnabled()) getString(R.string.granted) else getString(R.string.grant)
        binding!!.btnStorage.isEnabled = !isPermissionGranted()
        binding!!.btnStorage.text =
            if (isPermissionGranted()) getString(R.string.granted) else getString(R.string.grant)
    }
}