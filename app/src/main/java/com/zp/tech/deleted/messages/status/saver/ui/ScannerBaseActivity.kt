package com.zp.tech.deleted.messages.status.saver.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zp.tech.deleted.messages.status.saver.R

import java.io.ByteArrayOutputStream

abstract class ScannerBaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {

    protected var binding: Binding? = null

    fun setLayout(resource: Int) {
        binding = DataBindingUtil.setContentView(this, resource)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun copy(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        showToast(getString(R.string.copied_qr))
    }

    fun convertBitmapToByte(bmp: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        bmp.recycle()
        return byteArray
    }

    fun base64ToBitmap(encoded: String): Bitmap {
        val bitmap = Base64.decode(encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
    }

    fun showImage(imageView: ImageView, bitmap: ByteArray?) {
        bitmap?.let {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, it.size))
        }


    }
}