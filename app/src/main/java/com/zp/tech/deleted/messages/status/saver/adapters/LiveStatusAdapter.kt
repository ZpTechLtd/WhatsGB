package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemLiveStatusesBinding
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.fragments.StatusFragment

class LiveStatusAdapter(
    val context: StatusFragment,
    val list: List<StatusModel>,
) :
    RecyclerView.Adapter<LiveStatusAdapter.Holder>() {

    class Holder(itemView: ItemLiveStatusesBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_live_statuses,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val uri = list[position]
        Glide.with(context).load(if (uri.usesUri) uri.uri else uri.path).into(holder.binding.image)
        holder.binding.videoIcon.visibility = if (uri.usesUri) {
            if (uri.toString().contains(".mp4")) View.VISIBLE else View.GONE
        } else if (uri.path.contains(".mp4")) View.VISIBLE else View.GONE

        holder.binding.image.setOnClickListener {
            context.onItemClick(uri, true)
            context.showInterstitial()
        }
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) 0 else list.size
    }
}