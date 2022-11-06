package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemLanguageBinding
import com.zp.tech.deleted.messages.status.saver.ui.activities.LanguageActivity
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.Language
import com.zp.tech.deleted.messages.status.saver.utils.PreferenceManager

class LanguageAdapter(
    val context: LanguageActivity,
    val list: List<Language>,
    var langCode: String,
) :
    RecyclerView.Adapter<LanguageAdapter.Holder>() {


    inner class Holder(itemView: ItemLanguageBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(language: Language) {
            binding.txtLanguage.text = language.language
            binding.imgCheck.setImageResource(if (language.code == langCode) {
                R.drawable.ic_baseline_radio_button_checked_24
            } else {
                R.drawable.ic_baseline_radio_button_unchecked_24
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_language,
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])

        holder.binding.rootView.setOnClickListener {
            langCode = list[position].code
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}