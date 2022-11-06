package com.zp.tech.deleted.messages.status.saver.ui.dialogs

import android.R.attr
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.vanniktech.emoji.EmojiTextView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.utils.ShareUtils
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MessageViewDialog(val context: Context, val message: String) {

    fun showDialog() {
        val builder = MaterialAlertDialogBuilder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_message_dialog, null)
        val txtMessage = view.findViewById<EmojiTextView>(R.id.txtMessage)
        val btnShare = view.findViewById<ImageView>(R.id.imgShare)
        val btnCopy = view.findViewById<ImageView>(R.id.imgCopy)

        txtMessage.text = message

        btnShare.setOnClickListener {
            ShareUtils.shareText(context, message)
        }

        btnCopy.setOnClickListener {
            copyText()
        }
        builder.setView(view)
        builder.setTitle(context.getString(R.string.details))
        builder.show()
    }

    private fun copyText() {
        val clipboard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("message copied", message)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.message_copied),Toast.LENGTH_SHORT).show()
    }
}