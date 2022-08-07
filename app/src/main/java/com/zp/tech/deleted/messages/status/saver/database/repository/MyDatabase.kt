package com.zp.tech.deleted.messages.status.saver.database.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zp.tech.deleted.messages.status.saver.database.Messages
import com.zp.tech.deleted.messages.status.saver.database.Users
import com.zp.tech.deleted.messages.status.saver.database.dao.ChatDao

@Database(entities = [Users::class, Messages::class], version = 1)
public abstract class MyDatabase : RoomDatabase() {
    abstract fun ChatDao(): ChatDao

    companion object {
        private var mIntance:MyDatabase?=null

        @Synchronized
        fun getDatabase(context: Context): MyDatabase {
            if (mIntance==null) {
                mIntance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "chatdb"
                ).allowMainThreadQueries().build()
            }
            return mIntance as MyDatabase

        }
    }
}