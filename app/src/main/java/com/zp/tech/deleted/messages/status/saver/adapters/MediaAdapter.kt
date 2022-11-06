package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemMediaBinding
import com.zp.tech.deleted.messages.status.saver.models.DeletedFileModel
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.activities.ImageViewActivity
import com.zp.tech.deleted.messages.status.saver.ui.activities.VideoActivity
import com.zp.tech.deleted.messages.status.saver.ui.fragments.MediaFragment
import com.zp.tech.deleted.messages.status.saver.ui.activities.modelIntent
import com.zp.tech.deleted.messages.status.saver.ui.activities.showDownloadButton
import androidx.core.content.FileProvider
import com.zp.tech.deleted.messages.status.saver.BuildConfig
import com.zp.tech.deleted.messages.status.saver.utils.ShareUtils
import java.io.File


class MediaAdapter(val context: MediaFragment, var list: ArrayList<DeletedFileModel>) :
    RecyclerView.Adapter<MediaAdapter.MediaHolder>() {

    inner class MediaHolder(itemView: ItemMediaBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView

        init {
            binding.share.setOnClickListener {
                val statusModel = list[absoluteAdapterPosition]
                ShareUtils.shareMedia(context.requireActivity(), statusModel.path, "*/*")
            }

            binding.card.setOnClickListener {
                val statusModel = list[absoluteAdapterPosition]
                val extension = statusModel.extension
                var statusModelCustom: StatusModel? = null
                if ((extension.equals("mp4", ignoreCase = true)) || (extension.equals(
                        "png",
                        ignoreCase = true
                    )) || (extension.equals("jpg", ignoreCase = true)) || (extension.equals(
                        "jpeg",
                        ignoreCase = true
                    )) || (extension.equals("webp", ignoreCase = true))
                ) {
                    statusModelCustom =
                        StatusModel(statusModel.path.toUri(), statusModel.path, false)
                    onItemClick(statusModelCustom, false)
                } else {
                    openIntent(statusModel.path)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_media,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        val model = list[position]
        holder.binding.model = model
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) 0 else list.size
    }

    fun setData(it: ArrayList<DeletedFileModel>) {
        this.list = it
        notifyDataSetChanged()

    }

    fun onItemClick(model: StatusModel, show: Boolean) {
        val intent = if (model.usesUri) {
            if (model.uri.toString().contains(".mp4")) {
                Intent(context.activity, VideoActivity::class.java)
            } else {
                Intent(context.activity, ImageViewActivity::class.java)
            }
        } else {
            if (model.path.contains(".mp4")) {
                Intent(context.activity, VideoActivity::class.java)
            } else {
                Intent(context.activity, ImageViewActivity::class.java)
            }
        }
        intent.putExtra(modelIntent, model)
        intent.putExtra(showDownloadButton, show)
        context.startActivity(intent)
        context.showInterstitial()
    }

    fun openIntent(path: String) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val photoURI: Uri = FileProvider.getUriForFile(
                context.requireActivity(), BuildConfig.APPLICATION_ID.toString() + ".fileprovider",
                File(path)
            )
            intent.setDataAndType(photoURI, "*/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(intent, "View using"))
        } catch (activityNotFound: ActivityNotFoundException) {
            Toast.makeText(
                context.requireActivity(),
                "Not activity found to handle intent",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}