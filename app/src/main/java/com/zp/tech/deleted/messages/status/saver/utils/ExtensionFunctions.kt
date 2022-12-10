package com.zp.tech.deleted.messages.status.saver.utils

import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.result.*
import com.zp.tech.deleted.messages.status.saver.R
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

fun getFormats(): Collection<BarcodeFormat> =
    listOf(
        BarcodeFormat.AZTEC,
        BarcodeFormat.CODABAR,
        BarcodeFormat.CODE_39,
        BarcodeFormat.CODE_93,
        BarcodeFormat.CODE_128,
        BarcodeFormat.DATA_MATRIX,
        BarcodeFormat.EAN_8,
        BarcodeFormat.EAN_13,
        BarcodeFormat.ITF,
        BarcodeFormat.MAXICODE,
        BarcodeFormat.PDF_417,
        BarcodeFormat.RSS_14,
        BarcodeFormat.RSS_EXPANDED,
        BarcodeFormat.UPC_A,
        BarcodeFormat.UPC_E,
        BarcodeFormat.UPC_EAN_EXTENSION
    )


fun Long.convertTime(): String? {
    val date = Date(this)
    val format: Format = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
    return format.format(date)
}

fun Long.formatDateTime(): String? {
    val date = Date(this)
    val format: Format = SimpleDateFormat("MMM dd  hh:mm")
    return format.format(date)
}

fun ImageView.scannerIcon(type: ParsedResultType?) {

    when (type) {
        ParsedResultType.ADDRESSBOOK -> {
            setImageResource(R.drawable.ic_phone_book_svgrepo_com)
        }
        ParsedResultType.EMAIL_ADDRESS -> {
            setImageResource(R.drawable.ic_email_svgrepo_com)
        }
        ParsedResultType.PRODUCT -> {
            setImageResource(R.drawable.ic_product_item_svgrepo_com)
        }
        ParsedResultType.URI -> {
            setImageResource(R.drawable.ic_attachment_svgrepo_com)
        }
        ParsedResultType.TEXT -> {
            setImageResource(R.drawable.ic_text_svgrepo_com)
        }
        ParsedResultType.GEO -> {
            setImageResource(R.drawable.ic_location_svgrepo_com)
        }
        ParsedResultType.TEL -> {
            setImageResource(R.drawable.ic_telephone_phone_svgrepo_com)
        }
        ParsedResultType.SMS -> {
            setImageResource(R.drawable.ic_baseline_sms_24)
        }
        ParsedResultType.CALENDAR -> {
            setImageResource(R.drawable.ic_calendar_svgrepo_com)
        }
        ParsedResultType.WIFI -> {
            setImageResource(R.drawable.ic_baseline_wifi_24)
        }
        ParsedResultType.ISBN -> {
            setImageResource(R.drawable.ic_bar_code_svgrepo_com)
        }
        ParsedResultType.VIN -> {
            setImageResource(R.drawable.ic_bar_code_svgrepo_com)
        }
        else -> {
            setImageResource(R.drawable.ic_text_svgrepo_com)
        }
    }

}