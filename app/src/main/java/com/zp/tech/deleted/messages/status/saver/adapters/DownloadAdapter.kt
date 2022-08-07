package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemDownloadedStatusBinding
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.fragments.DownloadFragment
import com.zp.tech.deleted.messages.status.saver.ui.fragments.StatusFragment

class DownloadAdapter(
    val context: DownloadFragment,
    val list: List<StatusModel>,
) :
    RecyclerView.Adapter<DownloadAdapter.Holder>() {

    class Holder(itemView: ItemDownloadedStatusBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_downloaded_status,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val uri = list[position]
        Glide.with(context).load(if (uri.usesUri) uri else uri.path).into(holder.binding.image)
        holder.binding.videoIcon.visibility = if (uri.usesUri) {
            if (uri.toString().contains(".mp4")) View.VISIBLE else View.GONE
        } else  if (uri.path.contains(".mp4")) View.VISIBLE else View.GONE
        holder.binding.image.setOnClickListener {
            context.onItemClick(uri,false)
            context.showInterstitial()
        }
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) 0 else list.size
    }
}