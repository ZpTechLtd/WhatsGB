package com.zp.tech.deleted.messages.status.saver.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.client.result.ParsedResultType

class ParseResultConverter {

    private val gson = Gson()

    @TypeConverter
    fun resultToString(parsedResultType: ParsedResultType): String {
        return gson.toJson(parsedResultType)
    }

    @TypeConverter
    fun stringToResult(parsedResultType: String): ParsedResultType {
        val objectType = object : TypeToken<ParsedResultType>() {}.type
        return gson.fromJson(parsedResultType, objectType)
    }
}