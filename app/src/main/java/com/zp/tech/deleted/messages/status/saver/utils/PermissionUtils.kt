package com.zp.tech.deleted.messages.status.saver.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.zp.tech.deleted.messages.status.saver.R
import java.lang.Exception


class PermissionUtils constructor(private val context: Activity) {

    fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun isPermissionDenied(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        } else {
            return false
        }
    }

    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission")
            .setMessage("${context.resources.getString(R.string.app_name)} requires Camera permission, please allow the permission from settings to continue using the app.")
            .setPositiveButton("Settings"
            ) { _: DialogInterface?, _: Int ->
                try {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (exp: Exception) {
                    exp.printStackTrace()
                }


            }
            .setNegativeButton("Cancel"
            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

            .show()
    }
}