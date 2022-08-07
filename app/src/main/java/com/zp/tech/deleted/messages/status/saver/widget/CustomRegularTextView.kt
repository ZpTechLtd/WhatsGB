package com.zp.tech.deleted.messages.status.saver.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomRegularTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {
    init {
        val typeFace=Typeface.createFromAsset(context.assets,"Roboto-Regular.ttf")
        typeface = typeFace
    }
}