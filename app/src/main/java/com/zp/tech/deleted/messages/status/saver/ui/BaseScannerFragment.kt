package com.zp.tech.deleted.messages.status.saver.ui

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import java.io.ByteArrayOutputStream


open class BaseScannerFragment<Binding : ViewDataBinding> : Fragment() {
    var binding: Binding? = null
    var viewModel: ScannerSharedViewModel? = null
    fun setLayoutResource(resource: Int, inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, resource, container, false)
        viewModel = ViewModelProvider(requireActivity())[ScannerSharedViewModel::class.java]
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        300,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        300,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                val vibrator = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(300)
            }

        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }

    fun convertBitmapToByte(bmp: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        bmp.recycle()
        return byteArray
    }

     fun showImage(imageView: ImageView, bitmap: ByteArray?) {
        if (bitmap != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size))
        }
    }
}