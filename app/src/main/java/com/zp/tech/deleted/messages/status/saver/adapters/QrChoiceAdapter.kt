package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.client.result.ParsedResultType
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemQrCreationOptionsBinding
import com.zp.tech.deleted.messages.status.saver.ui.scanner.CreateFragment
import com.zp.tech.deleted.messages.status.saver.ui.scanner.QRCreationActivity
import com.zp.tech.deleted.messages.status.saver.utils.scannerIcon
import java.io.Serializable


data class QrOptions(val title: String, val type: ParsedResultType)
class QrChoiceAdapter(val context: CreateFragment, val list: ArrayList<QrOptions>) :
    RecyclerView.Adapter<QrChoiceAdapter.Holder>() {

    inner class Holder(itemView: ItemQrCreationOptionsBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView

        init {
            binding.icon.setOnClickListener { onClick(it) }
        }

        fun bind(qrOptions: QrOptions) {
            binding.txtTitle.text = qrOptions.title
            binding.icon.scannerIcon(qrOptions.type)
        }

        private fun onClick(view: View) {
            view.context.startActivity(
                Intent(view.context, QRCreationActivity::class.java).putExtra(
                    "type",
                    list[absoluteAdapterPosition].type as Serializable
                ).putExtra("title", list[absoluteAdapterPosition].title)
            )
            context.showInterstitial()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_qr_creation_options,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) 0 else list.size
    }


}