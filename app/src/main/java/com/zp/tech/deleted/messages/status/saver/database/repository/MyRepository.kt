package com.zp.tech.deleted.messages.status.saver.database.repository

import androidx.lifecycle.LiveData
import com.zp.tech.deleted.messages.status.saver.database.Messages
import com.zp.tech.deleted.messages.status.saver.database.Users
import com.zp.tech.deleted.messages.status.saver.database.dao.ChatDao
import kotlinx.coroutines.flow.Flow

public class MyRepository(private val chatDao: ChatDao) {

    fun insertUser(users: Users) {
        chatDao.insertUser(users)
    }


    fun insertMessages(messages: Messages) {
        chatDao.insertMessages(messages)
    }


    fun updateMessages(isDeleted: Boolean, row_id: Long, title: String) {
        chatDao.updateMessage(isDeleted, row_id, title)
    }

     fun getUsers(packageName:String): Flow<List<Users>> {
        return chatDao.getUsers(packageName)
    }

     fun getMessages(title: String,packageName: String,date:String): List<Messages> {
        return chatDao.getMessages(title,packageName,date)
    }

    fun  getMessageDates(title: String,packageName: String):List<Messages>{
      return  chatDao.getMessageDates(title,packageName)
    }

    fun getDeletedMesssages(title: String): LiveData<List<Messages>> {
        return chatDao.getDeletedMessages(title)
    }

    fun deleteMessage(title: String, rowId: Long) {
        chatDao.deleteMessage(title, rowId)
    }

    fun getDeletedMessage(title: String,rowId: Long):String{
        return chatDao.getDeletedMessage(title,rowId)
    }
}