package com.zp.tech.deleted.messages.status.saver.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.zxing.Result
import com.google.zxing.client.result.ParsedResultType

@Entity(tableName = "Scanner")
class Scanner {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "type")
    var type: ParsedResultType? = null

    @ColumnInfo(name = "time")
    var time: Long = 0

    @ColumnInfo(name = "barcodeResult")
    var barcodeResult: Result? = null

    @ColumnInfo(name = "isCreated")
    var isCreated: Boolean = false

    @ColumnInfo(name = "bitmap")
    var bitmap: String =""
}