package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.embeded.BarcodeEncoder
import com.google.zxing.*
import com.google.zxing.client.result.ParsedResultType
import com.google.zxing.common.HybridBinarizer
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import com.zp.tech.deleted.messages.status.saver.databinding.*
import com.zp.tech.deleted.messages.status.saver.ui.ScannerBaseActivity
import com.zp.tech.deleted.messages.status.saver.utils.formatDateTime


class QRCreationActivity : ScannerBaseActivity<ActivityQrcreationBinding>() {
    private var viewModel: ScannerSharedViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_qrcreation)
        viewModel = ViewModelProvider(this)[ScannerSharedViewModel::class.java]

        binding!!.txtTitle.text = intent.getStringExtra("title")
        inflate(intent.getSerializableExtra("type") as ParsedResultType)
        viewModel!!.idLiveData.observe(this) {
            startActivity(Intent(this, QrCreatedDetailsActivity::class.java).putExtra("id", it))
            finish()
        }

        binding!!.btnBack.setOnClickListener {
            finish()
        }
    }

    fun inflate(parsedResultType: ParsedResultType) {
        try {
            when (parsedResultType) {
                ParsedResultType.WIFI -> {
                    binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_wifi_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrWifiCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.rg.setOnCheckedChangeListener { p0, p1 ->
                            itemWifi.edPasswordInput.visibility =
                                if (p0!!.checkedRadioButtonId == R.id.none) View.INVISIBLE else View.VISIBLE
                        }

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edNetworkName.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_network_name))
                                return@setOnClickListener
                            }

                            var securityType = ""
                            securityType = when (itemWifi.rg.checkedRadioButtonId) {
                                R.id.none -> {
                                    "None"
                                }
                                R.id.wpa -> {
                                    "WPA/WPA2"
                                }
                                else -> {
                                    "WEP"
                                }
                            }

                            val content = StringBuilder()
                            content.append("WIFI:")
                            if (itemWifi.rg.checkedRadioButtonId == R.id.none) {
                                content.append("T:$securityType").append(";")
                                    .append("S:${itemWifi.edNetworkName.text.toString()}")
                                    .append(";;")
                            } else {
                                content.append("T:$securityType").append(";")
                                    .append("S:${itemWifi.edNetworkName.text.toString()}")
                                    .append(";")
                                    .append("P:${itemWifi.edPassword.text.toString()}").append(";;")
                            }

                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.URI -> {

                    binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_creation_url
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrCreationUrlBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edUrl.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_url))
                                return@setOnClickListener
                            }

                            val content = StringBuilder()
                            content.append("URL:").append(itemWifi.edUrl.text.toString())
                                .append(";")
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()

                }
                ParsedResultType.TEXT -> {
                    binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_text_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrTextCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edUrl.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_text))
                                return@setOnClickListener
                            }

                            val content = StringBuilder()
                            content.append(itemWifi.edUrl.text.toString())
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.ADDRESSBOOK -> {

                    binding!!.viewStub.viewStub!!.layoutResource =
                        R.layout.item_qr_contacts_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrContactsCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edName.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_name))
                                return@setOnClickListener
                            } else if (itemWifi.edNumber.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_number))
                                return@setOnClickListener
                            }
                            val content = StringBuilder()
                            content.append("MECARD:").append("N:${itemWifi.edName.text.toString()}")
                                .append(";").append("TEL:${itemWifi.edNumber.text.toString()}")
                                .append(";")
                                .append("EMAIL:${itemWifi.edEmail.text.toString()}").append(";;")
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.TEL -> {
                    binding!!.viewStub.viewStub!!.layoutResource =
                        R.layout.item_tel_qr_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemTelQrCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edTel.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_number))
                                return@setOnClickListener
                            }
                            val content = StringBuilder()
                            content.append("tel:${itemWifi.edTel.text.toString()}")
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.EMAIL_ADDRESS -> {
                    binding!!.viewStub.viewStub!!.layoutResource =
                        R.layout.item_qr_email_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrEmailCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edTel.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_email_address_qr))
                                return@setOnClickListener
                            }
                            val content = StringBuilder()
                            content.append("mailto:${itemWifi.edTel.text.toString()}")
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.SMS -> {
                    binding!!.viewStub.viewStub!!.layoutResource =
                        R.layout.item_qr_sms_creation
                    binding!!.viewStub.setOnInflateListener { _, inflated ->
                        val itemWifi: ItemQrSmsCreationBinding =
                            DataBindingUtil.bind(inflated)!!

                        itemWifi.btnCreate.setOnClickListener {
                            if (itemWifi.edNumber.text!!.isEmpty()) {
                                showToast(getString(R.string.please_enter_number))
                                return@setOnClickListener
                            } else if(itemWifi.edMessage.text!!.isEmpty()){
                                showToast(getString(R.string.please_enter_message))
                                return@setOnClickListener
                            }
                            val content = StringBuilder()
                            content.append("smsto:${itemWifi.edNumber.text.toString()}")
                                .append(":")
                                .append(itemWifi.edMessage.text.toString())
                            try {
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                    content.toString(),
                                    BarcodeFormat.QR_CODE,
                                    400,
                                    400
                                )
                                create(bitmap)


                            } catch (e: Exception) {
                                Log.d("TAG", "inflate: ${e.message}")
                            }
                        }

                    }
                    binding!!.viewStub.viewStub!!.inflate()
                }
                ParsedResultType.CALENDAR -> {
//                    binding!!.viewStub.viewStub!!.layoutResource =
//                        R.layout.item_qr_creation_calender
//                    binding!!.viewStub.setOnInflateListener { _, inflated ->
//                        val itemWifi: ItemQrCreationCalenderBinding =
//                            DataBindingUtil.bind(inflated)!!
//
//                        itemWifi.txtStart.text=System.currentTimeMillis().formatDateTime()
//                        itemWifi.txtEnd.text=System.currentTimeMillis().formatDateTime()
//
//
//                        itemWifi.txtStart.setOnClickListener {
//                            Date
//                        }
//
//                        itemWifi.btnCreate.setOnClickListener {
//                            if (itemWifi.edTitle.text!!.isEmpty()) {
//                                showToast(getString(R.string.please_enter_title))
//                                return@setOnClickListener
//                            }
//                            val content = StringBuilder()
////                            content.append("smsto:${itemWifi.edNumber.text.toString()}")
////                                .append(":")
////                                .append(itemWifi.edMessage.text.toString())
//                            try {
//                                val barcodeEncoder = BarcodeEncoder()
//                                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
//                                    content.toString(),
//                                    BarcodeFormat.QR_CODE,
//                                    400,
//                                    400
//                                )
//                                create(bitmap)
//
//
//                            } catch (e: Exception) {
//                                Log.d("TAG", "inflate: ${e.message}")
//                            }
//                        }
//
//                    }
//                    binding!!.viewStub.viewStub!!.inflate()
                }
                else -> {
                    Log.d("TAG", "inflate: else")
                }
            }
        } catch (exp: Exception) {
        }
    }

    private fun create(bitmap: Bitmap) {
        val encode = Base64.encodeToString(
            convertBitmapToByte(bitmap),
            Base64.DEFAULT
        )
        val array = Base64.decode(encode, Base64.DEFAULT)
        val resized = BitmapFactory.decodeByteArray(array, 0, array.size)
        val width = resized.width
        val height = resized.height
        val pixels = IntArray(width * height)
        resized.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val bBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bBitmap)
            if (!result.text.isNullOrEmpty()) {
                viewModel!!.saveCode(result, true, encode)
            }
        } catch (e: NotFoundException) {
            Log.e("TAG", "decode exception", e)
        }
    }
}