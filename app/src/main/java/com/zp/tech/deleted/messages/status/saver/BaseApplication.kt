package com.zp.tech.deleted.messages.status.saver

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.applovin.sdk.AppLovinSdk
import com.zp.tech.deleted.messages.status.saver.database.repository.MyDatabase
import com.zp.tech.deleted.messages.status.saver.database.repository.MyRepository

import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider

class BaseApplication:MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        EmojiManager.install(GoogleEmojiProvider())
        AppLovinSdk.getInstance( this ).mediationProvider = "max"
        AppLovinSdk.initializeSdk( this)
    }

    val database by lazy { MyDatabase.getDatabase(this) }
    val repository by lazy { MyRepository(database.ChatDao()) }
}