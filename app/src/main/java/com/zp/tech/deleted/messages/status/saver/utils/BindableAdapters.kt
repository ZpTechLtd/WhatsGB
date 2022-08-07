package com.zp.tech.deleted.messages.status.saver.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.models.DeletedFileModel
import com.zp.tech.deleted.messages.status.saver.widget.CustomRegularTextView

object BindableAdapters {

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(imageView: ImageView, statusModel: DeletedFileModel) {
        val extension = statusModel.extension
        if ((extension.equals("mp4", ignoreCase = true)) || (extension.equals(
                "png",
                ignoreCase = true
            )) || (extension.equals("jpg", ignoreCase = true)) || (extension.equals(
                "jpeg",
                ignoreCase = true
            )) || (extension.equals("webp", ignoreCase = true))
        ) {

            Glide.with(imageView.context).load(statusModel.path).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("setIcon")
    fun setIcon(imageView: ImageView, statusModel: DeletedFileModel) {
        val extension = statusModel.extension

        if (!extension.equals(
                "png",
                ignoreCase = true
            ) && !extension.equals("jpg", ignoreCase = true) && !extension.equals(
                "jpeg",
                ignoreCase = true
            ) && !extension.equals("webp", ignoreCase = true)
        ) {


            if ((extension.equals("mp3", ignoreCase = true) || extension.equals(
                    "opus",
                    ignoreCase = true
                ))
            ) {
                imageView.setImageResource(R.drawable.ic_music_svgrepo_com)

            } else if (extension.equals("mp4", ignoreCase = true)) {
                imageView.setImageResource(R.drawable.ic_video_svgrepo_com)
            } else if (extension.equals("pdf", ignoreCase = true)) {
                imageView.setImageResource(R.drawable.ic_pdf_svgrepo_com)

            } else if (extension.equals("doc", ignoreCase = true) || extension.equals(
                    "docx",
                    ignoreCase = true
                )
            ) {
                imageView.setImageResource(R.drawable.ic_doc_svgrepo_com)

            } else if (extension.equals("ppt", ignoreCase = true) || extension.equals(
                    "pptx",
                    ignoreCase = true
                )
            ) {
                imageView.setImageResource(R.drawable.ic_ppt_svgrepo_com)

            } else if (extension.equals("xls", ignoreCase = true)) {
                imageView.setImageResource(R.drawable.ic_xls_svgrepo_com)

            } else if (extension.equals("xlsx", ignoreCase = true)) {
                imageView.setImageResource(R.drawable.ic_xls_svgrepo_com)
            } else {
                imageView.setImageResource(R.drawable.ic_document_svgrepo_com)
            }

        }
    }

    @JvmStatic
    @BindingAdapter("setFileName")
    fun setFileName(customRegularTextView: CustomRegularTextView, statusModel: DeletedFileModel) {
        val name = statusModel.path.substringAfterLast("/")
        customRegularTextView.text = name
    }
}