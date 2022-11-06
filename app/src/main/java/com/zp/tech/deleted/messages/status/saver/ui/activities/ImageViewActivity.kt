package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityImageViewBinding
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.utils.ShareUtils
import com.zp.tech.deleted.messages.status.saver.utils.copyFileOrDirectory
import com.zp.tech.deleted.messages.status.saver.utils.copyFileR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val modelIntent = "modelIntent"
const val showDownloadButton = "showDownloadButton"

class ImageViewActivity : BaseActivity<ActivityImageViewBinding>() {
    private var statusMode: StatusModel? = null
    val adsManager = AdsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_image_view)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        statusMode = intent.getParcelableExtra(modelIntent) as? StatusModel

        binding!!.backToolbar.txtTitle.text = getString(R.string.image)

        Glide.with(this).load(if (statusMode!!.usesUri) statusMode!!.uri else statusMode!!.path)
            .into(binding!!.image)

        binding!!.btndownload.visibility =
            if (intent.getBooleanExtra(
                    showDownloadButton,
                    false
                )
            ) View.VISIBLE else View.GONE
        binding!!.btnshare.visibility =
            if (intent.getBooleanExtra(showDownloadButton, false)) View.GONE else View.VISIBLE


        binding!!.backToolbar.btnBack.setOnClickListener(this::onClick)
        binding!!.btndownload.setOnClickListener(this::onClick)
        binding!!.btnshare.setOnClickListener(this::onClick)

        adsManager.loadNativeBannerMax(binding!!.relAds)

    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                finish()
            }
            R.id.btndownload -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val stringUri = statusMode!!.uri.toString()
                        val name = stringUri.substringAfter(".Statuses%2F")
                        copyFileR(name, statusMode!!.uri)
                    } else {
                        copyFileOrDirectory(statusMode!!)
                    }
                }
            }

            R.id.btnshare -> {
                if (statusMode!!.path.contains(".mp4", ignoreCase = true)) {
                    ShareUtils.shareMedia(this,statusMode!!.path,"video/*")
                } else {
                    ShareUtils.shareMedia(this,statusMode!!.path,"image/*")
                }
            }
        }

    }
}