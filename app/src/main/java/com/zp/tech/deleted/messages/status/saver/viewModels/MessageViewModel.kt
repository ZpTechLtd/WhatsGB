package com.zp.tech.deleted.messages.status.saver.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zp.tech.deleted.messages.status.saver.BaseApplication
import com.zp.tech.deleted.messages.status.saver.adapters.VIEW_TYPE_Footer
import com.zp.tech.deleted.messages.status.saver.adapters.VIEW_TYPE_HEADER
import com.zp.tech.deleted.messages.status.saver.database.repository.MyRepository
import com.zp.tech.deleted.messages.status.saver.models.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: MyRepository? = null
    private var list: ArrayList<Messages> = ArrayList()
    private var  sortedList:MutableLiveData<List<Messages>> = MutableLiveData()

    init {
        repository = (application as BaseApplication).repository
    }

    fun getDates(title: String, packageName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            list = ArrayList()
            val datesList = repository!!.getMessageDates(title, packageName)
            datesList.forEach { dates ->
                val sortedMessages: List<Messages> =getSortedMessagesList( repository!!.getMessages(title, packageName, dates.date),dates)
                list.addAll(sortedMessages)
                sortedList.postValue(list)
            }
        }
    }
     private fun getSortedMessagesList(list: List<com.zp.tech.deleted.messages.status.saver.database.Messages>, date: com.zp.tech.deleted.messages.status.saver.database.Messages):List<Messages>{
        val sortedList:ArrayList<Messages> = ArrayList()
        list.forEach {
            sortedList.add(Messages(VIEW_TYPE_Footer,it))
        }
         sortedList.add(Messages(VIEW_TYPE_HEADER,date))
         return sortedList
    }

    fun observerSortedList() = sortedList
}