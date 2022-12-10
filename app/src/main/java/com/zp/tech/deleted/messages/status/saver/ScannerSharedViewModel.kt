package com.zp.tech.deleted.messages.status.saver

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.zxing.Result
import com.google.zxing.client.result.*
import com.zp.tech.deleted.messages.status.saver.database.Scanner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScannerSharedViewModel(application: Application) : AndroidViewModel(application) {
    private val scannerDao = (application as BaseApplication).getScannerDB().scannerDao()
    var idLiveData = MutableLiveData<Int>()
    var historyLiveData = MutableLiveData<List<Scanner>>()


    fun saveCode(result: Result, isCreated: Boolean, bitmap: String) {
        viewModelScope.launch {
            val scanner = Scanner()
            scanner.barcodeResult = result
            scanner.time = result.timestamp
            scanner.bitmap = bitmap
            var title = ""
            try {
                when (ResultParser.parseResult(result).type) {
                    ParsedResultType.ADDRESSBOOK -> {
                        val address =
                            ResultParser.parseResult(result) as AddressBookParsedResult
                        title =
                            if (address.names.isNullOrEmpty()) address.title else address.names[0]
                        scanner.type = ParsedResultType.ADDRESSBOOK

                    }
                    ParsedResultType.EMAIL_ADDRESS -> {
                        val email =
                            ResultParser.parseResult(result) as EmailAddressParsedResult
                        title = if (email.tos.isNullOrEmpty()) email.emailAddress else email.tos[0]
                        scanner.type = ParsedResultType.EMAIL_ADDRESS

                    }
                    ParsedResultType.PRODUCT -> {
                        title =
                            (ResultParser.parseResult(result) as ProductParsedResult).productID

                        scanner.type = ParsedResultType.PRODUCT
                    }
                    ParsedResultType.URI -> {
                        title = (ResultParser.parseResult(result) as URIParsedResult).uri
                        scanner.type = ParsedResultType.URI
                    }
                    ParsedResultType.TEXT -> {
                        title = (ResultParser.parseResult(result) as TextParsedResult).text
                        scanner.type = ParsedResultType.TEXT
                    }
                    ParsedResultType.GEO -> {
                        val geoLocation = ResultParser.parseResult(result) as GeoParsedResult
                        title = "${geoLocation.altitude},${geoLocation.longitude}"
                        scanner.type = ParsedResultType.GEO
                    }
                    ParsedResultType.TEL -> {
                        title = (ResultParser.parseResult(result) as TelParsedResult).number
                        scanner.type = ParsedResultType.TEL
                    }
                    ParsedResultType.SMS -> {
                        val smsResultParser =
                            ResultParser.parseResult(result) as SMSParsedResult
                        title =
                            if (smsResultParser.numbers.isNullOrEmpty()) smsResultParser.body else smsResultParser.numbers[0]
                        scanner.type = ParsedResultType.SMS
                    }
                    ParsedResultType.CALENDAR -> {
                        val calendar =
                            (ResultParser.parseResult(result) as CalendarParsedResult)
                        title =
                            if (calendar.organizer.isNullOrEmpty()) calendar.summary else calendar.organizer
                        scanner.type = ParsedResultType.CALENDAR
                    }
                    ParsedResultType.WIFI -> {
                        val wifi =
                            (ResultParser.parseResult(result) as WifiParsedResult)
                        title = if (wifi.ssid.isNullOrEmpty()) wifi.password else wifi.ssid
                        scanner.type = ParsedResultType.WIFI
                    }
                    ParsedResultType.ISBN -> {
                        title =
                            (ResultParser.parseResult(result) as ISBNParsedResult).isbn
                        scanner.type = ParsedResultType.ISBN
                    }
                    ParsedResultType.VIN -> {
                        title =
                            (ResultParser.parseResult(result) as VINParsedResult).vin
                        scanner.type = ParsedResultType.VIN
                    }
                    else -> {
                        title = result.text
                        scanner.type = ParsedResultType.TEXT
                    }
                }
            } catch (exp: Exception) {
                exp.printStackTrace()
                title = result.text
                scanner.type = ParsedResultType.TEXT
            }
            scanner.title = title
            scanner.isCreated = isCreated
            val id = scannerDao.saveCode(scanner)
            idLiveData.postValue(id.toInt())
        }
    }

    fun getAllCodes(isCreated: Boolean) {
        viewModelScope.launch {
            val list = scannerDao.getAllCodes(isCreated)
            list.collect {
                historyLiveData.postValue(it)
            }
        }
    }

    fun getCode(id: Int): Flow<Scanner> = scannerDao.getCode(id)

    fun delete(rowId: Int) {
        viewModelScope.launch {
            scannerDao.delete(rowId)
        }
    }
}