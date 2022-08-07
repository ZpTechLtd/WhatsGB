package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zp.tech.deleted.messages.status.saver.R

abstract class BaseFragment : Fragment() {
    fun isAppInstalled(packageName: String?): Boolean {
        try {
            requireActivity().packageManager.getPackageInfo(
                packageName!!,
                PackageManager.GET_ACTIVITIES
            )
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }


    fun showDialog(message: String) {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Message").setMessage(message)
            .setPositiveButton(
                "Ok"
            ) { _, _ -> }.show().getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.purple_500))
    }
}