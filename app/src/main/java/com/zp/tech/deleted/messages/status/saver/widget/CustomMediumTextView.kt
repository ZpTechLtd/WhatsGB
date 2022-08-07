package com.zp.tech.deleted.messages.status.saver.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomMediumTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {

    init {
        val typeFace= Typeface.createFromAsset(context.assets,"Roboto-Medium.ttf")
        typeface = typeFace
    }
}