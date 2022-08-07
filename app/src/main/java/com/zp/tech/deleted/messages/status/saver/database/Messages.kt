package com.zp.tech.deleted.messages.status.saver.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Messages(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date: String,
    @PrimaryKey @ColumnInfo(name = "row_id") val row_id: Long,
    @ColumnInfo(name = "packageName") val packageName: String,
    @ColumnInfo(name = "isDeleted") val isDeleted: Boolean
)