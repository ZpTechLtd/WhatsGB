package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zp.tech.deleted.messages.status.saver.R
import java.lang.Math.abs

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
        MaterialAlertDialogBuilder(requireActivity()).setTitle(resources.getString(R.string.message)).setMessage(message)
            .setPositiveButton(
                "Ok"
            ) { _, _ -> }.show().getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.purple_500))
    }

    fun addGesture(recyclerView: RecyclerView) {
        val gestureDetector =
            GestureDetector(requireActivity(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    recyclerView.parent.requestDisallowInterceptTouchEvent(true)
                    return super.onDown(e)
                }

                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    if (abs(distanceX) > abs(distanceY)) {
                        recyclerView.parent.requestDisallowInterceptTouchEvent(false)
                    } else if (abs(distanceY) > 10) {
                        recyclerView.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })

        recyclerView.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }
        })
    }
}