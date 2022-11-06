package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityVideoBinding
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.utils.ShareUtils
import com.zp.tech.deleted.messages.status.saver.utils.copyFileOrDirectory
import com.zp.tech.deleted.messages.status.saver.utils.copyFileR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VideoActivity : BaseActivity<ActivityVideoBinding>() {

    private var statusMode: StatusModel? = null
    private var simpleExoplayer: ExoPlayer? = null
    private var mediaItem: MediaItem? = null
    private var imgDownload: ImageView? = null
    private var imgShare: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_video)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        statusMode = intent.getParcelableExtra(modelIntent) as? StatusModel
        imgDownload = findViewById(R.id.downloaded)
        imgShare = findViewById(R.id.share)

        binding!!.backToolbar.txtTitle.text = getString(R.string.video)
        imgDownload!!.visibility =
            if (intent.getBooleanExtra(showDownloadButton, false)) View.VISIBLE else View.GONE

        imgShare!!.visibility =
            if (intent.getBooleanExtra(showDownloadButton, false)) View.GONE else View.VISIBLE

        binding!!.backToolbar.btnBack.setOnClickListener(this::onClick)
        imgDownload!!.setOnClickListener(this::onClick)
        imgShare!!.setOnClickListener(this::onClick)


        simpleExoplayer = ExoPlayer.Builder(this).build()

        mediaItem = if (statusMode!!.usesUri) {
            MediaItem.fromUri(statusMode!!.uri)
        } else {
            MediaItem.fromUri(statusMode!!.path)
        }
        simpleExoplayer!!.addMediaItem(mediaItem!!)
        simpleExoplayer!!.playWhenReady = true
        binding!!.videoView.player = simpleExoplayer
        simpleExoplayer!!.prepare()
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                finish()
            }
            R.id.downloaded -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val stringUri=statusMode!!.uri.toString()
                        val name= stringUri.substringAfter(".Statuses%2F")
                        copyFileR(name,statusMode!!.uri)
                    } else {
                        copyFileOrDirectory(statusMode!!)
                    }
                }
            }
            R.id.share -> {
                ShareUtils.shareMedia(this,statusMode!!.path,"video/*")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (simpleExoplayer != null && simpleExoplayer!!.isPlaying) {
            simpleExoplayer!!.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (simpleExoplayer != null) {
            simpleExoplayer!!.playWhenReady = false
            simpleExoplayer!!.release()
        }
    }
}