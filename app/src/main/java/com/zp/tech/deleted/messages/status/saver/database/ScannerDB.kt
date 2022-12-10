package com.zp.tech.deleted.messages.status.saver.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zp.tech.deleted.messages.status.saver.database.converters.ParseResultConverter
import com.zp.tech.deleted.messages.status.saver.database.converters.ResultTypeConverter


@Database(entities = [Scanner::class], version = 1)
@TypeConverters(ResultTypeConverter::class, ParseResultConverter::class)
abstract class ScannerDB : RoomDatabase() {
    abstract fun scannerDao(): ScannerDao

    companion object {
        @Volatile
        private var scannerDB: ScannerDB? = null

        fun getInstance(context: Context): ScannerDB {
            if (scannerDB == null) {
                synchronized(ScannerDB::class.java) {
                    scannerDB = Room.databaseBuilder(
                        context.applicationContext,
                        ScannerDB::class.java,
                        "scanner.db"
                    ).build()
                }
            }
            return scannerDB!!
        }
    }
}