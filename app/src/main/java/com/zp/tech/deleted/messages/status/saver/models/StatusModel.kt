package com.zp.tech.deleted.messages.status.saver.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusModel(val uri: Uri, val path: String, val usesUri: Boolean) : Parcelable