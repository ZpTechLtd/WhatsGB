package com.zp.tech.deleted.messages.status.saver.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var whatsappUri: String = "whatsappuri"
    private var businessUri: String = "businessuri"

    init {
        sharedPreferences = context.getSharedPreferences("chatPreference", Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit();
    }

    fun setWhatsappUri(uri: String) {
        editor?.putString(whatsappUri, uri)?.commit()
    }

    fun getWhatsAppUri(): String? {
        return sharedPreferences?.getString(whatsappUri, "");
    }

    fun setBusinessUri(uri: String) {
        editor?.putString(businessUri, uri)?.commit()
    }

    fun getBusinessUri(): String? {
        return sharedPreferences?.getString(businessUri, "");
    }
}