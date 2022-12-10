package com.zp.tech.deleted.messages.status.saver.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.Result

class ResultTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun resultToString(barcodeResult: Result): String {
        return gson.toJson(barcodeResult)
    }

    @TypeConverter
    fun stringToResult(barcodeResult: String): Result {
        val objectType = object : TypeToken<Result>() {}.type
        return gson.fromJson(barcodeResult, objectType)
    }
}