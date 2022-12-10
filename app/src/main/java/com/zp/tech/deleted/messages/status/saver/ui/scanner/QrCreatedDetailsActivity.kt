package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.zxing.client.result.*
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.database.Scanner
import com.zp.tech.deleted.messages.status.saver.databinding.*
import com.zp.tech.deleted.messages.status.saver.ui.ScannerBaseActivity
import com.zp.tech.deleted.messages.status.saver.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QrCreatedDetailsActivity : ScannerBaseActivity<ActivityQrCreatedDetailsBinding>() {
    private var viewModel: ScannerSharedViewModel? = null
    private var scanner: Scanner? = null
    private var adManager: AdsManager? = null
    private var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_qr_created_details)
        viewModel = ViewModelProvider(this)[ScannerSharedViewModel::class.java]

        adManager = AdsManager(this)

        lifecycleScope.launch {
            viewModel!!.getCode(intent.getIntExtra("id", -1)).collect {
                scanner = it
                binding!!.txtTitle.apply {
                    it.type?.let { type ->
                        text = type.toString()
                    }
                }

                showImage(binding!!.imgQr, Base64.decode(it.bitmap, Base64.DEFAULT))
                inflate(it)

            }
        }

        binding!!.btnBack.setOnClickListener { onClick(it) }
        binding!!.btnSave.setOnClickListener { onClick(it) }
        binding!!.btnShare.setOnClickListener { onClick(it) }
        adManager!!.loadNativeLarge2(binding!!.addLayout, binding!!.relAds)
    }


    private fun inflate(scanner: Scanner) {

        when (scanner.type) {
            ParsedResultType.ADDRESSBOOK -> {
                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_address_book
                binding!!.viewStub.setOnInflateListener { _, inflated ->

                    val itemQrAddressBookBinding: ItemQrAddressBookBinding =
                        DataBindingUtil.bind(inflated)!!
                    val address =
                        ResultParser.parseResult(scanner.barcodeResult) as AddressBookParsedResult
                    itemQrAddressBookBinding.lnNames.visibility =
                        if (address.names.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lntitle.visibility =
                        if (address.title.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnNicknames.visibility =
                        if (address.nicknames.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnpronunciation.visibility =
                        if (address.pronunciation.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnphoneNumbers.visibility =
                        if (address.phoneNumbers.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnemails.visibility =
                        if (address.emails.isNullOrEmpty()) View.GONE else View.VISIBLE


                    itemQrAddressBookBinding.lninstantMessenger.visibility =
                        if (address.instantMessenger.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnnote.visibility =
                        if (address.note.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnaddresses.visibility =
                        if (address.addresses.isNullOrEmpty()) View.GONE else View.VISIBLE


                    itemQrAddressBookBinding.lnorg.visibility =
                        if (address.org.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnbirthday.visibility =
                        if (address.birthday.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lnurls.visibility =
                        if (address.urLs.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.lngeo.visibility =
                        if (address.geo.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrAddressBookBinding.txtNames.apply {
                        if (!address.names.isNullOrEmpty()) {
                            val name = getStringFromList(address.names)
                            text = name
                        }

                    }

                    itemQrAddressBookBinding.txttitle.apply {
                        if (!address.title.isNullOrEmpty()) {
                            text = address.title
                        }
                    }

                    itemQrAddressBookBinding.txtNicknames.apply {
                        if (!address.nicknames.isNullOrEmpty()) {
                            val nickNames = getStringFromList(address.nicknames)
                            text = nickNames
                        }
                    }

                    itemQrAddressBookBinding.txtpronunciation.apply {
                        if (!address.pronunciation.isNullOrEmpty()) {
                            text = address.pronunciation
                        }
                    }
                    itemQrAddressBookBinding.txtphoneNumbers.apply {
                        if (!address.phoneNumbers.isNullOrEmpty()) {
                            val numbers = getStringFromList(address.phoneNumbers)
                            text = numbers

                        }
                    }

                    itemQrAddressBookBinding.txtemails.apply {
                        if (!address.emails.isNullOrEmpty()) {
                            val emails = getStringFromList(address.emails)
                            text = emails
                        }
                    }


                    itemQrAddressBookBinding.txtinstantMessenger.apply {
                        if (!address.instantMessenger.isNullOrEmpty()) {
                            text = address.instantMessenger
                        }
                    }

                    itemQrAddressBookBinding.txtnote.apply {
                        if (!address.note.isNullOrEmpty()) {
                            text = address.note
                        }
                    }

                    itemQrAddressBookBinding.txtaddresses.apply {
                        if (!address.addresses.isNullOrEmpty()) {
                            val addresses = getStringFromList(address.addresses)
                            text = addresses
                        }
                    }

                    itemQrAddressBookBinding.txtorg.apply {
                        if (!address.org.isNullOrEmpty()) {
                            text = address.org
                        }
                    }

                    itemQrAddressBookBinding.txtbirthday.apply {
                        if (!address.birthday.isNullOrEmpty()) {
                            text = address.birthday
                        }
                    }

                    itemQrAddressBookBinding.txturls.apply {
                        if (!address.urLs.isNullOrEmpty()) {
                            val urLs = getStringFromList(address.urLs)
                            text = urLs
                        }
                    }

                    itemQrAddressBookBinding.txtgeo.apply {
                        if (!address.geo.isNullOrEmpty()) {
                            val geo = getStringFromList(address.geo)
                            text = geo
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()
            }

            ParsedResultType.EMAIL_ADDRESS -> {

                val email =
                    ResultParser.parseResult(scanner.barcodeResult) as EmailAddressParsedResult

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_email
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrEmailBinding: ItemQrEmailBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrEmailBinding.lnTos.visibility =
                        if (email.tos.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrEmailBinding.lnccs.visibility =
                        if (email.cCs.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrEmailBinding.lnbccs.visibility =
                        if (email.bcCs.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrEmailBinding.lnsubject.visibility =
                        if (email.subject.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrEmailBinding.lnbody.visibility =
                        if (email.body.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrEmailBinding.txtTos.apply {
                        if (!email.tos.isNullOrEmpty()) {
                            val tos = getStringFromList(email.tos)
                            text = tos
                        }

                    }

                    itemQrEmailBinding.txtccs.apply {
                        if (!email.cCs.isNullOrEmpty()) {
                            val cCs = getStringFromList(email.cCs)
                            text = cCs
                        }
                    }

                    itemQrEmailBinding.txtbccs.apply {
                        if (!email.bcCs.isNullOrEmpty()) {
                            val bcCs = getStringFromList(email.bcCs)
                            text = bcCs
                        }
                    }

                    itemQrEmailBinding.txtsubject.apply {
                        if (!email.subject.isNullOrEmpty()) {
                            text = email.subject
                        }
                    }
                    itemQrEmailBinding.txtbody.apply {
                        if (!email.body.isNullOrEmpty()) {
                            text = email.body
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()

            }
            ParsedResultType.PRODUCT -> {

                val product =
                    ResultParser.parseResult(scanner.barcodeResult) as ProductParsedResult

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_product
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrProductBinding: ItemQrProductBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrProductBinding.lnproductID.visibility =
                        if (product.productID.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrProductBinding.lnnormalizedProductID.visibility =
                        if (product.normalizedProductID.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrProductBinding.txtproductID.apply {
                        if (!product.productID.isNullOrEmpty()) {
                            text = product.productID
                        }
                    }
                    itemQrProductBinding.txtnormalizedProductID.apply {
                        if (!product.normalizedProductID.isNullOrEmpty()) {
                            text = product.normalizedProductID
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()

            }
            ParsedResultType.URI -> {

                val uri = (ResultParser.parseResult(scanner.barcodeResult) as URIParsedResult)
                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_text
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrTextBinding: ItemQrTextBinding =
                        DataBindingUtil.bind(inflated)!!
                    itemQrTextBinding.txtText.apply {

                        text = uri.uri
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            ParsedResultType.TEXT -> {

                val textParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as TextParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_text
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrTextBinding: ItemQrTextBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrTextBinding.txtText.apply {
                        if (!textParsedResult.text.isNullOrEmpty()) {
                            text = textParsedResult.text
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            ParsedResultType.GEO -> {

                val geoParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as GeoParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_geo
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrGeoBinding: ItemQrGeoBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrGeoBinding.lnquery.visibility =
                        if (geoParsedResult.query.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrGeoBinding.lnlatitude.visibility =
                        if (geoParsedResult.latitude == 0.0) View.GONE else View.VISIBLE

                    itemQrGeoBinding.lnlongitude.visibility =
                        if (geoParsedResult.longitude == 0.0) View.GONE else View.VISIBLE

                    itemQrGeoBinding.lnaltitude.visibility =
                        if (geoParsedResult.latitude == 0.0) View.GONE else View.VISIBLE


                    itemQrGeoBinding.txtquery.apply {
                        if (!geoParsedResult.query.isNullOrEmpty()) {
                            text = geoParsedResult.query
                        }
                    }

                    itemQrGeoBinding.txtlatitude.apply {
                        if (geoParsedResult.latitude != 0.0) {
                            text = geoParsedResult.latitude.toString()
                        }
                    }

                    itemQrGeoBinding.txtlongitude.apply {
                        if (geoParsedResult.longitude != 0.0) {
                            text = geoParsedResult.longitude.toString()
                        }
                    }

                    itemQrGeoBinding.txtAltitude.apply {
                        if (geoParsedResult.altitude != 0.0) {
                            text = geoParsedResult.altitude.toString()
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()

            }
            ParsedResultType.TEL -> {
                val telParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as TelParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_tel
                binding!!.viewStub.setOnInflateListener { stub, inflated ->
                    val itemQrTelBinding: ItemQrTelBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrTelBinding.lntitle.visibility =
                        if (telParsedResult.title.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrTelBinding.lnnumber.visibility =
                        if (telParsedResult.number.isNullOrEmpty()) View.GONE else View.VISIBLE



                    itemQrTelBinding.txttitle.apply {
                        if (!telParsedResult.title.isNullOrEmpty()) {
                            text = telParsedResult.title
                        }
                    }

                    itemQrTelBinding.txtnumber.apply {
                        if (!telParsedResult.number.isNullOrEmpty()) {
                            text = telParsedResult.number
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()

            }
            ParsedResultType.SMS -> {
                val sMSParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as SMSParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_sms
                binding!!.viewStub.setOnInflateListener { stub, inflated ->

                    val itemQrSmsBinding: ItemQrSmsBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrSmsBinding.lnsubject.visibility =
                        if (sMSParsedResult.subject.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnbody.visibility =
                        if (sMSParsedResult.body.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnnumbers.visibility =
                        if (sMSParsedResult.numbers.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnvias.visibility =
                        if (sMSParsedResult.vias.isNullOrEmpty()) View.GONE else View.VISIBLE



                    itemQrSmsBinding.txtsubject.apply {
                        if (!sMSParsedResult.subject.isNullOrEmpty()) {
                            text = sMSParsedResult.subject
                        }
                    }

                    itemQrSmsBinding.txtbody.apply {
                        if (!sMSParsedResult.body.isNullOrEmpty()) {
                            text = sMSParsedResult.body
                        }
                    }

                    itemQrSmsBinding.txtnumbers.apply {
                        if (!sMSParsedResult.numbers.isNullOrEmpty()) {
                            val numbers = getStringFromList(sMSParsedResult.numbers)
                            text = numbers
                        }
                    }

                    itemQrSmsBinding.txtvias.apply {
                        if (!sMSParsedResult.vias.isNullOrEmpty()) {
                            val vias = getStringFromList(sMSParsedResult.vias)
                            text = vias
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            ParsedResultType.CALENDAR -> {

                val calendarParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as CalendarParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_calendar
                binding!!.viewStub.setOnInflateListener { stub, inflated ->

                    val itemQrSmsBinding: ItemQrCalendarBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrSmsBinding.lnsummary.visibility =
                        if (calendarParsedResult.summary.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnstart.visibility =
                        if (calendarParsedResult.startTimestamp == 0L) View.GONE else View.VISIBLE


                    itemQrSmsBinding.lnend.visibility =
                        if (calendarParsedResult.endTimestamp == 0L) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnlocation.visibility =
                        if (calendarParsedResult.location.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnorganizer.visibility =
                        if (calendarParsedResult.organizer.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnattendees.visibility =
                        if (calendarParsedResult.attendees.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lndescription.visibility =
                        if (calendarParsedResult.description.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnlongitude.visibility =
                        if (calendarParsedResult.longitude == 0.0) View.GONE else View.VISIBLE

                    itemQrSmsBinding.lnlatitude.visibility =
                        if (calendarParsedResult.latitude == 0.0) View.GONE else View.VISIBLE

                    itemQrSmsBinding.txtsummary.apply {
                        if (!calendarParsedResult.summary.isNullOrEmpty()) {
                            text = calendarParsedResult.summary
                        }
                    }

                    itemQrSmsBinding.txtstart.apply {
                        if (calendarParsedResult.startTimestamp == 0L) {
                            val date = calendarParsedResult.startTimestamp.convertTime()
                            text = date
                        }
                    }

                    itemQrSmsBinding.txtend.apply {
                        if (calendarParsedResult.endTimestamp == 0L) {
                            val date = calendarParsedResult.endTimestamp.convertTime()
                            text = date
                        }
                    }

                    itemQrSmsBinding.txtlocation.apply {
                        if (!calendarParsedResult.location.isNullOrEmpty()) {
                            text = calendarParsedResult.location
                        }
                    }

                    itemQrSmsBinding.txtorganizer.apply {
                        if (!calendarParsedResult.organizer.isNullOrEmpty()) {
                            text = calendarParsedResult.organizer
                        }
                    }

                    itemQrSmsBinding.txtattendees.apply {
                        if (!calendarParsedResult.attendees.isNullOrEmpty()) {
                            val attendies = getStringFromList(calendarParsedResult.attendees)
                            text = attendies
                        }
                    }

                    itemQrSmsBinding.txtdescription.apply {
                        if (!calendarParsedResult.description.isNullOrEmpty()) {
                            text = calendarParsedResult.description
                        }
                    }

                    itemQrSmsBinding.txtlatitude.apply {
                        if (calendarParsedResult.latitude != 0.0) {
                            text = calendarParsedResult.latitude.toString()
                        }
                    }

                    itemQrSmsBinding.txtlongitude.apply {
                        if (calendarParsedResult.longitude != 0.0) {
                            text = calendarParsedResult.longitude.toString()
                        }
                    }
                }
                binding!!.viewStub.viewStub!!.inflate()

            }
            ParsedResultType.WIFI -> {

                val wifi =
                    (ResultParser.parseResult(scanner.barcodeResult) as WifiParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_wifi
                binding!!.viewStub.setOnInflateListener { stub, inflated ->

                    val itemQrWifiBinding: ItemQrWifiBinding =
                        DataBindingUtil.bind(inflated)!!


                    itemQrWifiBinding.lnssid.visibility =
                        if (wifi.ssid.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrWifiBinding.lnnetworkEncryption.visibility =
                        if (wifi.networkEncryption.isNullOrEmpty()) View.GONE else View.VISIBLE


                    itemQrWifiBinding.lnpassword.visibility =
                        if (wifi.password.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrWifiBinding.txtssid.apply {
                        if (!wifi.ssid.isNullOrEmpty()) {
                            text = wifi.ssid
                        }
                    }

                    itemQrWifiBinding.txtnetworkEncryption.apply {
                        if (!wifi.networkEncryption.isNullOrEmpty()) {
                            text = wifi.networkEncryption
                        }
                    }

                    itemQrWifiBinding.txtpassword.apply {
                        if (!wifi.password.isNullOrEmpty()) {
                            text = wifi.password
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            ParsedResultType.ISBN -> {

                val iSBNParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as ISBNParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_text
                binding!!.viewStub.setOnInflateListener { stub, inflated ->

                    val itemQrWifiBinding: ItemQrTextBinding =
                        DataBindingUtil.bind(inflated)!!

                    itemQrWifiBinding.txtText.apply {
                        if (!iSBNParsedResult.isbn.isNullOrEmpty()) {
                            text = iSBNParsedResult.isbn
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            ParsedResultType.VIN -> {
                val vinParsedResult =
                    (ResultParser.parseResult(scanner.barcodeResult) as VINParsedResult)

                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_vin
                binding!!.viewStub.setOnInflateListener { _, inflated ->

                    val itemQrVinBinding: ItemQrVinBinding =
                        DataBindingUtil.bind(inflated)!!


                    itemQrVinBinding.lnvin.visibility =
                        if (vinParsedResult.vin.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnworldManufacturerID.visibility =
                        if (vinParsedResult.worldManufacturerID.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnvehicleDescriptorSection.visibility =
                        if (vinParsedResult.vehicleDescriptorSection.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnvehicleIdentifierSection.visibility =
                        if (vinParsedResult.vehicleIdentifierSection.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lncountryCode.visibility =
                        if (vinParsedResult.countryCode.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnvehicleAttributes.visibility =
                        if (vinParsedResult.vehicleAttributes.isNullOrEmpty()) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnmodelYear.visibility =
                        if (vinParsedResult.modelYear == 0) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnplantCode.visibility =
                        if (vinParsedResult.plantCode.toString()
                                .isNullOrEmpty()
                        ) View.GONE else View.VISIBLE

                    itemQrVinBinding.lnsequentialNumber.visibility =
                        if (vinParsedResult.sequentialNumber.isNullOrEmpty()) View.GONE else View.VISIBLE


                    itemQrVinBinding.txtvin.apply {
                        if (!vinParsedResult.vin.isNullOrEmpty()) {
                            text = vinParsedResult.vin
                        }
                    }

                    itemQrVinBinding.txtworldManufacturerID.apply {
                        if (!vinParsedResult.worldManufacturerID.isNullOrEmpty()) {
                            text = vinParsedResult.worldManufacturerID
                        }
                    }

                    itemQrVinBinding.txtvehicleDescriptorSection.apply {
                        if (!vinParsedResult.vehicleDescriptorSection.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleDescriptorSection
                        }
                    }

                    itemQrVinBinding.txtvehicleIdentifierSection.apply {
                        if (!vinParsedResult.vehicleIdentifierSection.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleIdentifierSection
                        }
                    }

                    itemQrVinBinding.txtcountryCode.apply {
                        if (!vinParsedResult.countryCode.isNullOrEmpty()) {
                            text = vinParsedResult.countryCode
                        }
                    }

                    itemQrVinBinding.txtvehicleAttributes.apply {
                        if (!vinParsedResult.vehicleAttributes.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleAttributes
                        }
                    }

                    itemQrVinBinding.txtmodelYear.apply {
                        if (vinParsedResult.modelYear == 0) {
                            text = vinParsedResult.modelYear.toString()
                        }
                    }

                    itemQrVinBinding.txtplantCode.apply {
                        if (!vinParsedResult.plantCode.toString().isNullOrEmpty()) {
                            text = vinParsedResult.plantCode.toString()
                        }
                    }

                    itemQrVinBinding.txtsequentialNumber.apply {
                        if (!vinParsedResult.sequentialNumber.isNullOrEmpty()) {
                            text = vinParsedResult.sequentialNumber
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            else -> {

                Log.d("TAG", "inflate:default ")
            }
        }
    }

    private fun getStringFromList(list: Array<String>): String {
        val string = StringBuilder()
        for (i in list.indices) {
            if (i == list.size - 1) {
                string.append(list[i])
            } else {
                string.append(list[i]).append(",")
            }

        }
        return string.toString()
    }

    private fun onClick(view: View) {

        if (view.id == R.id.btnBack) {
            finish()
        } else if (view.id == R.id.btnShare) {
            if (uri == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val path = saveQrCodeToCache(this@QrCreatedDetailsActivity,
                        base64ToBitmap(scanner!!.bitmap))
                    uri = path
                    ShareUtils.shareMedia(this@QrCreatedDetailsActivity, path!!, "image/*")
                }
            } else {
                ShareUtils.shareMedia(this@QrCreatedDetailsActivity, uri!!, "image/*")
            }

        } else if (view.id == R.id.btnSave) {
            CoroutineScope(Dispatchers.IO).launch {
                saveQrCode(this@QrCreatedDetailsActivity,
                    base64ToBitmap(scanner!!.bitmap))
            }

        }
    }

        private infix fun View.below(view: View) {
            (this.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.BELOW,
                view.id)
        }
    }