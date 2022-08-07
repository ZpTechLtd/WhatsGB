package com.zp.tech.deleted.messages.status.saver.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zp.tech.deleted.messages.status.saver.database.Messages
import com.zp.tech.deleted.messages.status.saver.database.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM users where packageName=:packageName order by row_id desc")
    fun getUsers(packageName: String): Flow<List<Users>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: Users)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(messages: Messages)

    @Query("SELECT * FROM messages WHERE title=:title AND packageName=:packageName AND date=:date order by row_id desc")
    fun getMessages(title: String, packageName: String,date:String): List<Messages>

    @Query("select * from messages where packageName=:packageName AND  title=:title group by date order by row_id desc")
    fun getMessageDates(title: String, packageName: String): List<Messages>

    @Query("SELECT * FROM messages WHERE title=:title AND isDeleted=1")
    fun getDeletedMessages(title: String): LiveData<List<Messages>>

    @Query("Delete  from messages where title=:title AND row_id=:rowId")
    fun deleteMessage(title: String, rowId: Long)

    @Query("UPDATE messages SET isDeleted=:isDeleted WHERE row_id=:rowId AND title=:title")
    fun updateMessage(isDeleted: Boolean, rowId: Long, title: String)

    @Query("Select text from messages where title=:title AND row_id=:rowId")
    fun getDeletedMessage(title: String, rowId: Long): String
}

