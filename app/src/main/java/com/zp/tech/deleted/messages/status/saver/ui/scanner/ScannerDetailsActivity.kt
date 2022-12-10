package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
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
import com.zp.tech.deleted.messages.status.saver.utils.convertTime
import kotlinx.coroutines.launch


class ScannerDetailsActivity : ScannerBaseActivity<ActivityScannerDetailsBinding>() {
    private var viewModel: ScannerSharedViewModel? = null
    private var stringCopy: StringBuilder? = null
    private var scanner: Scanner? = null
    private var intentAction: Intent? = null
    private var adManager: AdsManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_scanner_details)
        viewModel = ViewModelProvider(this)[ScannerSharedViewModel::class.java]
        adManager= AdsManager(this)

        lifecycleScope.launch {
            viewModel!!.getCode(intent.getIntExtra("id", -1)).collect {
                scanner = it
                binding!!.txtTitle.apply {
                    it.type?.let { type ->
                        text = type.toString()
                    }
                }
                inflate(it)

            }
        }

        binding!!.btnCopy.setOnClickListener { onClick(it) }
        binding!!.btnShare.setOnClickListener { onClick(it) }
        binding!!.btnGeneric.setOnClickListener { onClick(it) }
        binding!!.btnBack.setOnClickListener { onClick(it) }

        adManager!!.loadNativeLarge2(binding!!.addLayout,binding!!.relAds)
    }

    private fun inflate(scanner: Scanner) {
        stringCopy = StringBuilder()
        intentAction = Intent()
        when (scanner.type) {
            ParsedResultType.ADDRESSBOOK -> {
                binding!!.viewStub.viewStub!!.layoutResource = R.layout.item_qr_address_book
                binding!!.viewStub.setOnInflateListener { _, inflated ->
                    binding!!.btnGeneric.visibility = View.VISIBLE
                    binding!!.btnGeneric.text = getString(R.string.add_to_contact_qr)
                    val itemQrAddressBookBinding: ItemQrAddressBookBinding =
                        DataBindingUtil.bind(inflated)!!
                    intentAction!!.action = Intent.ACTION_INSERT
                    intentAction!!.type = ContactsContract.Contacts.CONTENT_TYPE
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
                            stringCopy!!.append(getString(R.string.name_qr))
                                .append(name).append("\n")

                            intentAction!!.putExtra(ContactsContract.Intents.Insert.NAME, name)
                        }

                    }

                    itemQrAddressBookBinding.txttitle.apply {
                        if (!address.title.isNullOrEmpty()) {
                            text = address.title
                            stringCopy!!.append(getString(R.string.title_qr))
                                .append(address.title).append("\n")
                            intentAction!!.putExtra(
                                ContactsContract.Intents.Insert.JOB_TITLE,
                                address.title
                            )
                        }
                    }

                    itemQrAddressBookBinding.txtNicknames.apply {
                        if (!address.nicknames.isNullOrEmpty()) {
                            val nickNames = getStringFromList(address.nicknames)
                            text = nickNames
                            stringCopy!!.append(getString(R.string.nick_name_qr))
                                .append(nickNames).append("\n")
                        }
                    }

                    itemQrAddressBookBinding.txtpronunciation.apply {
                        if (!address.pronunciation.isNullOrEmpty()) {
                            text = address.pronunciation
                            stringCopy!!.append(getString(R.string.pronounciation_qr))
                                .append(address.pronunciation).append("\n")
                        }
                    }
                    itemQrAddressBookBinding.txtphoneNumbers.apply {
                        if (!address.phoneNumbers.isNullOrEmpty()) {
                            val numbers = getStringFromList(address.phoneNumbers)
                            text = numbers
                            stringCopy!!.append(getString(R.string.phone_numbers_qr))
                                .append(numbers).append("\n")
                            for (i in address.phoneNumbers.indices) {
                                when (i) {
                                    0 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.PHONE,
                                            address.phoneNumbers[0]
                                        )
                                    }
                                    1 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.SECONDARY_PHONE,
                                            address.phoneNumbers[1]
                                        )
                                    }
                                    2 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.TERTIARY_PHONE,
                                            address.phoneNumbers[2]
                                        )
                                    }
                                }

                            }

                        }
                    }

                    itemQrAddressBookBinding.txtemails.apply {
                        if (!address.emails.isNullOrEmpty()) {
                            val emails = getStringFromList(address.emails)
                            text = emails
                            stringCopy!!.append(getString(R.string.emails_qr))
                                .append(emails).append("\n")
                            for (i in address.emails.indices) {
                                when (i) {
                                    0 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.EMAIL,
                                            address.emails[0]
                                        )
                                    }
                                    1 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.SECONDARY_EMAIL,
                                            address.emails[1]
                                        )
                                    }
                                    2 -> {
                                        intentAction!!.putExtra(
                                            ContactsContract.Intents.Insert.TERTIARY_EMAIL,
                                            address.emails[2]
                                        )
                                    }
                                }

                            }
                        }
                    }


                    itemQrAddressBookBinding.txtinstantMessenger.apply {
                        if (!address.instantMessenger.isNullOrEmpty()) {
                            text = address.instantMessenger
                            stringCopy!!.append(getString(R.string.instantMessenger_qr))
                                .append(address.instantMessenger).append("\n")
                        }
                    }

                    itemQrAddressBookBinding.txtnote.apply {
                        if (!address.note.isNullOrEmpty()) {
                            text = address.note
                            stringCopy!!.append(getString(R.string.note_qr))
                                .append(address.note).append("\n")
                            intentAction!!.putExtra(
                                ContactsContract.Intents.Insert.NOTES,
                                address.instantMessenger
                            )
                        }
                    }

                    itemQrAddressBookBinding.txtaddresses.apply {
                        if (!address.addresses.isNullOrEmpty()) {
                            val addresses = getStringFromList(address.addresses)
                            text = addresses
                            stringCopy!!.append(getString(R.string.addresses_qr))
                                .append(addresses).append("\n")
                            intentAction!!.putExtra(
                                ContactsContract.Intents.Insert.POSTAL,
                                addresses
                            )
                        }
                    }

                    itemQrAddressBookBinding.txtorg.apply {
                        if (!address.org.isNullOrEmpty()) {
                            text = address.org
                            stringCopy!!.append(getString(R.string.org_qr))
                                .append(address.org).append("\n")
                            intentAction!!.putExtra(
                                ContactsContract.Intents.Insert.COMPANY,
                                address.org
                            )
                        }
                    }

                    itemQrAddressBookBinding.txtbirthday.apply {
                        if (!address.birthday.isNullOrEmpty()) {
                            text = address.birthday
                            stringCopy!!.append(getString(R.string.birthday_qr))
                                .append(address.birthday).append("\n")
                        }
                    }

                    itemQrAddressBookBinding.txturls.apply {
                        if (!address.urLs.isNullOrEmpty()) {
                            val urLs = getStringFromList(address.urLs)
                            text = urLs
                            stringCopy!!.append(getString(R.string.urls_qr))
                                .append(urLs).append("\n")
                            val data = ArrayList<ContentValues>()

                            for (i in address.urLs.indices) {
                                when (i) {
                                    0 -> {
                                        val row1 = ContentValues()
                                        row1.put(
                                            ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                                        )
                                        row1.put(
                                            ContactsContract.CommonDataKinds.Website.URL,
                                            address.urLs[0]
                                        )
                                        data.add(row1)
                                    }
                                    1 -> {
                                        val row1 = ContentValues()
                                        row1.put(
                                            ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                                        )
                                        row1.put(
                                            ContactsContract.CommonDataKinds.Website.URL,
                                            address.urLs[1]
                                        )
                                        data.add(row1)
                                    }
                                    2 -> {
                                        val row1 = ContentValues()
                                        row1.put(
                                            ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                                        )
                                        row1.put(
                                            ContactsContract.CommonDataKinds.Website.URL,
                                            address.urLs[2]
                                        )
                                        data.add(row1)
                                    }
                                }

                            }

                            intentAction!!.putParcelableArrayListExtra(
                                ContactsContract.Intents.Insert.DATA,
                                data
                            )
                        }
                    }

                    itemQrAddressBookBinding.txtgeo.apply {
                        if (!address.geo.isNullOrEmpty()) {
                            val geo = getStringFromList(address.geo)
                            text = geo
                            stringCopy!!.append(getString(R.string.geo_qr))
                                .append(geo).append("\n")
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
                            stringCopy!!.append(getString(R.string.tos_qr))
                                .append(tos).append("\n")
                        }

                    }

                    itemQrEmailBinding.txtccs.apply {
                        if (!email.cCs.isNullOrEmpty()) {
                            val cCs = getStringFromList(email.cCs)
                            text = cCs
                            stringCopy!!.append(getString(R.string.ccs_qr))
                                .append(cCs).append("\n")
                        }
                    }

                    itemQrEmailBinding.txtbccs.apply {
                        if (!email.bcCs.isNullOrEmpty()) {
                            val bcCs = getStringFromList(email.bcCs)
                            text = bcCs
                            stringCopy!!.append(getString(R.string.bccs_qr))
                                .append(bcCs).append("\n")
                        }
                    }

                    itemQrEmailBinding.txtsubject.apply {
                        if (!email.subject.isNullOrEmpty()) {
                            text = email.subject
                            stringCopy!!.append(getString(R.string.subject_qr))
                                .append(email.subject).append("\n")
                        }
                    }
                    itemQrEmailBinding.txtbody.apply {
                        if (!email.body.isNullOrEmpty()) {
                            text = email.body
                            stringCopy!!.append(getString(R.string.body_qr))
                                .append(email.body).append("\n")
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
                            stringCopy!!.append(getString(R.string.product_id_qr))
                                .append(product.productID).append("\n")
                        }
                    }
                    itemQrProductBinding.txtnormalizedProductID.apply {
                        if (!product.normalizedProductID.isNullOrEmpty()) {
                            text = product.normalizedProductID
                            stringCopy!!.append(getString(R.string.normalized_id_qr))
                                .append(product.normalizedProductID).append("\n")
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
                        stringCopy!!
                            .append(uri.uri).append("\n")
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
                            stringCopy!!
                                .append(textParsedResult.text).append("\n")
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
                            stringCopy!!.append(getString(R.string.query_qr))
                                .append(geoParsedResult.query).append("\n")
                        }
                    }

                    itemQrGeoBinding.txtlatitude.apply {
                        if (geoParsedResult.latitude != 0.0) {
                            text = geoParsedResult.latitude.toString()
                            stringCopy!!.append(getString(R.string.latitude_qr))
                                .append(geoParsedResult.latitude).append("\n")
                        }
                    }

                    itemQrGeoBinding.txtlongitude.apply {
                        if (geoParsedResult.longitude != 0.0) {
                            text = geoParsedResult.longitude.toString()
                            stringCopy!!.append(getString(R.string.longitude_qr))
                                .append(geoParsedResult.longitude).append("\n")
                        }
                    }

                    itemQrGeoBinding.txtAltitude.apply {
                        if (geoParsedResult.altitude != 0.0) {
                            text = geoParsedResult.altitude.toString()
                            stringCopy!!.append(getString(R.string.altitude_qr))
                                .append(geoParsedResult.altitude).append("\n")
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
                            stringCopy!!.append(getString(R.string.title_qr))
                                .append(telParsedResult.title).append("\n")
                        }
                    }

                    itemQrTelBinding.txtnumber.apply {
                        if (!telParsedResult.number.isNullOrEmpty()) {
                            text = telParsedResult.number
                            stringCopy!!.append(getString(R.string.number_qr))
                                .append(telParsedResult.number).append("\n")
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
                            stringCopy!!.append(getString(R.string.subject_qr))
                                .append(sMSParsedResult.subject).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtbody.apply {
                        if (!sMSParsedResult.body.isNullOrEmpty()) {
                            text = sMSParsedResult.body
                            stringCopy!!.append(getString(R.string.body_qr))
                                .append(sMSParsedResult.body).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtnumbers.apply {
                        if (!sMSParsedResult.numbers.isNullOrEmpty()) {
                            val numbers = getStringFromList(sMSParsedResult.numbers)
                            text = numbers
                            stringCopy!!.append(getString(R.string.number_qr))
                                .append(numbers).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtvias.apply {
                        if (!sMSParsedResult.vias.isNullOrEmpty()) {
                            val vias = getStringFromList(sMSParsedResult.vias)
                            text = vias
                            stringCopy!!.append(getString(R.string.vias_qr))
                                .append(vias).append("\n")
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
                            stringCopy!!.append(getString(R.string.summary_qr))
                                .append(calendarParsedResult.summary).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtstart.apply {
                        if (calendarParsedResult.startTimestamp == 0L) {
                            val date = calendarParsedResult.startTimestamp.convertTime()
                            text = date
                            stringCopy!!.append(getString(R.string.start_qr))
                                .append(date).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtend.apply {
                        if (calendarParsedResult.endTimestamp == 0L) {
                            val date = calendarParsedResult.endTimestamp.convertTime()
                            text = date
                            stringCopy!!.append(getString(R.string.end_qr))
                                .append(date).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtlocation.apply {
                        if (!calendarParsedResult.location.isNullOrEmpty()) {
                            text = calendarParsedResult.location
                            stringCopy!!.append(getString(R.string.location_qr))
                                .append(calendarParsedResult.location).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtorganizer.apply {
                        if (!calendarParsedResult.organizer.isNullOrEmpty()) {
                            text = calendarParsedResult.organizer
                            stringCopy!!.append(getString(R.string.organizer_qr))
                                .append(calendarParsedResult.organizer).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtattendees.apply {
                        if (!calendarParsedResult.attendees.isNullOrEmpty()) {
                            val attendies = getStringFromList(calendarParsedResult.attendees)
                            text = attendies
                            stringCopy!!.append(getString(R.string.attendees_qr))
                                .append(attendies).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtdescription.apply {
                        if (!calendarParsedResult.description.isNullOrEmpty()) {
                            text = calendarParsedResult.description
                            stringCopy!!.append(getString(R.string.description_qr))
                                .append(calendarParsedResult.description).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtlatitude.apply {
                        if (calendarParsedResult.latitude != 0.0) {
                            text = calendarParsedResult.latitude.toString()
                            stringCopy!!.append(getString(R.string.latitude_qr))
                                .append(calendarParsedResult.latitude.toString()).append("\n")
                        }
                    }

                    itemQrSmsBinding.txtlongitude.apply {
                        if (calendarParsedResult.longitude != 0.0) {
                            text = calendarParsedResult.longitude.toString()
                            stringCopy!!.append(getString(R.string.longitude_qr))
                                .append(calendarParsedResult.longitude.toString()).append("\n")
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
                            stringCopy!!.append(getString(R.string.network_name_qr))
                                .append(wifi.ssid).append("\n")
                        }
                    }

                    itemQrWifiBinding.txtnetworkEncryption.apply {
                        if (!wifi.networkEncryption.isNullOrEmpty()) {
                            text = wifi.networkEncryption
                            stringCopy!!.append(getString(R.string.security_type_qr))
                                .append(wifi.networkEncryption).append("\n")
                        }
                    }

                    itemQrWifiBinding.txtpassword.apply {
                        if (!wifi.password.isNullOrEmpty()) {
                            text = wifi.password
                            stringCopy!!.append(wifi.password)
                            binding!!.btnGeneric.text = getString(R.string.copy_password_qr)
                            binding!!.btnGeneric.visibility = View.VISIBLE
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
                            stringCopy!!
                                .append(iSBNParsedResult.isbn).append("\n")
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
                            stringCopy!!.append(getString(R.string.vin_qr))
                                .append(vinParsedResult.vin).append("\n")
                        }
                    }

                    itemQrVinBinding.txtworldManufacturerID.apply {
                        if (!vinParsedResult.worldManufacturerID.isNullOrEmpty()) {
                            text = vinParsedResult.worldManufacturerID
                            stringCopy!!.append(getString(R.string.worldManufacturerID_qr))
                                .append(":")
                                .append(vinParsedResult.worldManufacturerID).append("\n")
                        }
                    }

                    itemQrVinBinding.txtvehicleDescriptorSection.apply {
                        if (!vinParsedResult.vehicleDescriptorSection.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleDescriptorSection
                            stringCopy!!.append(getString(R.string.vehicleDescriptorSection_qr))
                                .append(":")
                                .append(vinParsedResult.vehicleDescriptorSection).append("\n")
                        }
                    }

                    itemQrVinBinding.txtvehicleIdentifierSection.apply {
                        if (!vinParsedResult.vehicleIdentifierSection.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleIdentifierSection
                            stringCopy!!.append(getString(R.string.vehicleIdentifierSection_qr))
                                .append(":")
                                .append(vinParsedResult.vehicleIdentifierSection).append("\n")
                        }
                    }

                    itemQrVinBinding.txtcountryCode.apply {
                        if (!vinParsedResult.countryCode.isNullOrEmpty()) {
                            text = vinParsedResult.countryCode
                            stringCopy!!.append(getString(R.string.countryCode_qr))
                                .append(vinParsedResult.countryCode).append("\n")
                        }
                    }

                    itemQrVinBinding.txtvehicleAttributes.apply {
                        if (!vinParsedResult.vehicleAttributes.isNullOrEmpty()) {
                            text = vinParsedResult.vehicleAttributes
                            stringCopy!!.append(getString(R.string.vehicleAttributes_qr))
                                .append(":")
                                .append(vinParsedResult.vehicleAttributes).append("\n")
                        }
                    }

                    itemQrVinBinding.txtmodelYear.apply {
                        if (vinParsedResult.modelYear == 0) {
                            text = vinParsedResult.modelYear.toString()
                            stringCopy!!.append(getString(R.string.modelYear_qr))
                                .append(vinParsedResult.modelYear.toString()).append("\n")
                        }
                    }

                    itemQrVinBinding.txtplantCode.apply {
                        if (!vinParsedResult.plantCode.toString().isNullOrEmpty()) {
                            text = vinParsedResult.plantCode.toString()
                            stringCopy!!.append(getString(R.string.plantCode_qr))
                                .append(vinParsedResult.plantCode.toString()).append("\n")
                        }
                    }

                    itemQrVinBinding.txtsequentialNumber.apply {
                        if (!vinParsedResult.sequentialNumber.isNullOrEmpty()) {
                            text = vinParsedResult.sequentialNumber
                            stringCopy!!.append(getString(R.string.sequentialNumber_qr))
                                .append(vinParsedResult.sequentialNumber).append("\n")
                        }
                    }

                }
                binding!!.viewStub.viewStub!!.inflate()
            }
            else -> {

                Log.d("TAG", "inflate:default ")
            }
        }
        binding!!.lnActions.below(binding!!.viewStub.root)
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

        } else if (view.id == R.id.btnCopy) {
            copy(stringCopy.toString())

        } else if (view.id == R.id.btnGeneric) {
            try {
                scanner?.let { result ->
                    when (result.type) {
                        ParsedResultType.ADDRESSBOOK -> {
                            intentAction?.let {
                                startActivity(intentAction)
                            }
                        }
                        ParsedResultType.WIFI -> {
                            val wifi =
                                (ResultParser.parseResult(result.barcodeResult) as WifiParsedResult)
                            copy(wifi.password)
                        }
                        else -> {
                            Log.d("TAG", "onClick: else")
                        }
                    }
                }
            } catch (exp: Exception) {
                Log.d("TAG", "onClick: $exp")
            }
        }

    }

    private infix fun View.below(view: View) {
        (this.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.BELOW, view.id)
    }
}