package com.zp.tech.deleted.messages.status.saver.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "row_id") val row_id: Long,
    @ColumnInfo(name = "packageName") val packageName: String,
    @ColumnInfo(name = "isGroupUser") val isGroupUser: Boolean
)