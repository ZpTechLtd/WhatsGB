package com.zp.tech.deleted.messages.status.saver.viewModels

import android.app.Application
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zp.tech.deleted.messages.status.saver.BaseApplication
import com.zp.tech.deleted.messages.status.saver.database.Users
import com.zp.tech.deleted.messages.status.saver.database.repository.MyRepository
import com.zp.tech.deleted.messages.status.saver.models.DeletedFileModel
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.PreferenceManager
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import kotlin.collections.ArrayList

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: MyRepository? = null
    private var context: Application? = null
    private var userList: MutableLiveData<List<Users>> = MutableLiveData()
    private var chatTypeMessagesLiveData: MutableLiveData<MainActivity.ChatType> = MutableLiveData()
    private var statusTypeLiveData: MutableLiveData<MainActivity.ChatType> = MutableLiveData()
    private var preferenceManager: PreferenceManager? = null
    private var statusPermissionLiveData: MutableLiveData<MainActivity.ChatType> = MutableLiveData()
    private var statustype: MainActivity.ChatType? = null
    private var statusLiveData: MutableLiveData<List<StatusModel>> = MutableLiveData()
    private var downloadedStatusLiveData: MutableLiveData<List<StatusModel>> = MutableLiveData()
    private var deletedMediaLiveData: MutableLiveData<ArrayList<DeletedFileModel>> =
        MutableLiveData()

    init {
        repository = (application as BaseApplication).repository
        preferenceManager = PreferenceManager(application)
        statusTypeLiveData.postValue(MainActivity.ChatType.WHATSAPP)
        context = application
    }

    fun getUser(packageName: String) {
        viewModelScope.launch {
            repository!!.getUsers(packageName).collect {
                userList.postValue(it)
            }
        }
    }

    fun loadStatuses() {
        viewModelScope.launch {
            var directoryPath: File? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (statustype == MainActivity.ChatType.WHATSAPP) {
                    if (preferenceManager?.getWhatsAppUri().isNullOrEmpty()) {
                        statusPermissionLiveData.postValue(MainActivity.ChatType.WHATSAPP)
                    } else {
                        val uri: Uri = Uri.parse(preferenceManager!!.getWhatsAppUri())
                        val documentFile: DocumentFile? =
                            uri.let { DocumentFile.fromTreeUri(context!!.applicationContext, it) }
                        loadFilesR(documentFile!!)

                    }

                } else if (statustype == MainActivity.ChatType.BUSINESS) {
                    if (preferenceManager?.getBusinessUri().isNullOrEmpty()) {
                        statusPermissionLiveData.postValue(MainActivity.ChatType.BUSINESS)
                    } else {
                        val uri: Uri = Uri.parse(preferenceManager!!.getBusinessUri())
                        val documentFile: DocumentFile? =
                            uri.let { DocumentFile.fromTreeUri(context!!.applicationContext, it) }
                        loadFilesR(documentFile!!)
                    }

                }

            } else {
                if (statustype == MainActivity.ChatType.WHATSAPP) {
                    directoryPath = if (File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "WhatsApp/Media/.Statuses"
                        ).exists()
                    ) {
                        File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "WhatsApp/Media/.Statuses"
                        )
                    } else {
                        File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
                        )
                    }

                    listFiles(directoryPath)

                } else if (statustype == MainActivity.ChatType.BUSINESS) {
                    directoryPath = if (File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "WhatsApp Business/Media/.Statuses"
                        ).exists()
                    ) {
                        File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "WhatsApp Business/Media/.Statuses"
                        )
                    } else {
                        File(
                            Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp/Media/.Statuses"
                        )
                    }
                    listFiles(directoryPath)
                }

            }
        }
    }

    private fun listFiles(path: File) {
        val filesList: ArrayList<StatusModel> = ArrayList()
        val files = path.listFiles()
        if (!files.isNullOrEmpty()) {
            files.forEach { file ->
                if (!file.absolutePath.contains(".nomedia")) {
                    filesList.add(StatusModel(file.absolutePath.toUri(), file.absolutePath, false))
                }
            }
            statusLiveData.postValue(filesList)
        }
    }

    private fun loadFilesR(documentFile: DocumentFile) {
        val filesList: ArrayList<StatusModel> = ArrayList()
        if (!documentFile.listFiles().isNullOrEmpty()) {
            documentFile.listFiles().forEach {
                if (!it.uri.toString().contains(".nomedia")) {
                    filesList.add(StatusModel(it.uri, it.uri.toString(), true))
                }
            }
            statusLiveData.postValue(filesList)
        }
    }

    fun loadDownloadedStatuses() {
        viewModelScope.launch {
            val list: ArrayList<StatusModel> = ArrayList()
            val file =
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "StatusSaver 2022"
                )
            if (!file.exists()) {
                file.mkdirs()
            }

            val files = file.listFiles()
            if (files != null && files.isNotEmpty()) {
                files.forEach {
                    list.add(StatusModel(it.absolutePath.toUri(), it.absolutePath, false))
                }
                if (list.size > 1) {
                    list.reverse()
                }
                downloadedStatusLiveData.postValue(list)
            }
        }
    }

    fun getDeletedMedia() {

        viewModelScope.launch {
            try {
                val list: ArrayList<DeletedFileModel> = ArrayList()
                val file = File(
                    "${
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString()
                    }/Whats Download/"
                )
                if (!file.exists()) {
                    file.mkdirs()
                }
                val files = file.listFiles()
                if (!files.isNullOrEmpty()) {
                    files.forEach {
                        if (!it.isDirectory) {
                            list.add(
                                DeletedFileModel(
                                    it.absolutePath,
                                    it.absolutePath.substringAfterLast(".")
                                )
                            )
                        }
                    }
                }
                if (!list.isNullOrEmpty()){
                    list.reverse()
                }
                deletedMediaLiveData.postValue(list)
            } catch (exp: Exception) {
                exp.printStackTrace()
            }
        }

    }

    fun setAppUri(uri: String, chattype: MainActivity.ChatType) {
        if (chattype == MainActivity.ChatType.WHATSAPP) {
            preferenceManager?.setWhatsappUri(uri)
        } else {
            preferenceManager?.setBusinessUri(uri)
        }

    }

    fun setStatusType(type: MainActivity.ChatType) {
        statustype = type
    }

    fun setChatTypeMessage(chatType: MainActivity.ChatType) =
        chatTypeMessagesLiveData.postValue(chatType)

    fun observerUsers() = userList

    fun observeChatTypeMessages() = chatTypeMessagesLiveData

    fun observeStatusType() = statusTypeLiveData

    fun observeStatusPermissionLiveData() = statusPermissionLiveData

    fun observerStatusLiveData() = statusLiveData

    fun observeDownloadStatuses() = downloadedStatusLiveData

    fun observeDeletedMedia() = deletedMediaLiveData
}