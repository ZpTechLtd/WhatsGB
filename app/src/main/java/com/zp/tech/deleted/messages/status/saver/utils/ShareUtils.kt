package com.zp.tech.deleted.messages.status.saver.utils

import android.content.Intent

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri

import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

import androidx.core.content.FileProvider
import com.zp.tech.deleted.messages.status.saver.BuildConfig
import com.zp.tech.deleted.messages.status.saver.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.NullPointerException


object ShareUtils {

    fun shareMedia(context: Context, path: String?, mimeType: String?) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = mimeType
            val file = File(path)
            val uri: Uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(intent)
        } catch (exception: IllegalStateException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: IllegalArgumentException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: NullPointerException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareMedia(context: Context, path: Uri?, mimeType: String?) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = mimeType
            intent.putExtra(Intent.EXTRA_STREAM, path)
            context.startActivity(intent)
        } catch (exception: IllegalStateException) {
            CoroutineScope(Dispatchers.IO).launch {
                showToast(context)
            }
        } catch (exception: IllegalArgumentException) {
            CoroutineScope(Dispatchers.IO).launch {
                showToast(context)
            }
        } catch (exception: ActivityNotFoundException) {
            CoroutineScope(Dispatchers.IO).launch {
                showToast(context)
            }
        } catch (exception: NullPointerException) {
            CoroutineScope(Dispatchers.IO).launch {
                showToast(context)
            }

        }
    }


   suspend fun showToast(context: Context) {
        withContext(Dispatchers.Main){
        Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()

        }
    }

    fun moreApps(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=pub:AD+Mobile+Apps+Studio")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/developer?id=AD+Mobile+Apps+Studio")
                )
            )
        }
    }

    fun rateUs(context: Context) {
        val pck: String = context.getPackageName()
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pck")))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$pck")
                )
            )
        }
    }


    fun shareText(context: Context, message: String) {
        try {


            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share")
            context.startActivity(shareIntent)
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }

    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "Share"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }
}