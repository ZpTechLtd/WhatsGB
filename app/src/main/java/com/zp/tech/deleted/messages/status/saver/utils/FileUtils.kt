package com.zp.tech.deleted.messages.status.saver.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.io.*

suspend fun Context.copyFileOrDirectory(statusModel: StatusModel) {
    try {
        val src = File(statusModel.path)
        val dst = File(
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/StatusSaver 2022",
            src.getName()
        )
        if (!src.getParentFile().exists()) dst.getParentFile().mkdirs()
        if (!src.exists()) {
            dst.createNewFile()
        }
        FileInputStream(src).use { `in` ->
            FileOutputStream(dst).use { out ->

                // Copy the bits from instream to outstream
                val buf = ByteArray(2048)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
            }
        }
        withContext(Dispatchers.Main) {
            showToast("Save Successfully")
            Constants.isStatusSaved = true

        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            showToast("Failed to save...")
            Constants.isStatusSaved = false
        }
    } catch (exp:IllegalArgumentException){
        exp.printStackTrace()
    } catch (ep:FileNotFoundException){
        ep.printStackTrace()
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
suspend fun Context.copyFileR(inputFile: String, sourceUri: Uri) {
    try {
        val values = ContentValues()
        val destinationUri: Uri
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, inputFile)
        values.put(
            MediaStore.MediaColumns.MIME_TYPE,
            if (inputFile.endsWith(".mp4")) "video/*" else "image/*"
        )
        values.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS + "/StatusSaver 2022"
        )
        destinationUri =
            this.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)!!

        val inputStream: InputStream = this.contentResolver.openInputStream(sourceUri)!!
        val outputStream: OutputStream =
            this.contentResolver.openOutputStream(destinationUri)!!
        IOUtils.copy(inputStream, outputStream)
        withContext(Dispatchers.Main) {
            showToast("Save Successfully")
            Constants.isStatusSaved = true
        }

    } catch (exception: Exception) {
        exception.printStackTrace()
        withContext(Dispatchers.Main) {
            showToast("Failed to save...")
            Constants.isStatusSaved = false
        }
    }
    catch (exp:IllegalArgumentException){
        exp.printStackTrace()
    } catch (ep:FileNotFoundException){
        ep.printStackTrace()
    }
}

private fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
