package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.MessagesAdapter
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityMessagesBinding
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecorator
import com.zp.tech.deleted.messages.status.saver.viewModels.MessageViewModel

class MessagesActivity : BaseActivity<ActivityMessagesBinding>() {
    private var title: String? = null
    private var intentpackageName: String? = null
    private var isGroup: Boolean? = null
    private var viewModel: MessageViewModel? = null
    private var messagesAdapter: MessagesAdapter? = null
    val adsManager = AdsManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_messages)
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]

        title = intent.getStringExtra("title")
        intentpackageName = intent.getStringExtra("package")
        isGroup = intent.getBooleanExtra("isGroup", false)
        binding!!.title.text = title
        binding!!.imgIcon.setImageResource(if (isGroup!!) R.drawable.ic_user_groups else R.drawable.ic_user)
        binding!!.recyclerview.setHasFixedSize(true)
        binding!!.recyclerview.addItemDecoration(ItemDecorator(resources.getDimensionPixelOffset(R.dimen._3sdp)))
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        binding!!.recyclerview.addItemDecoration(ItemDecorator(resources.getDimensionPixelOffset(R.dimen._5sdp)))
        binding!!.recyclerview.layoutManager = linearLayoutManager
        (binding!!.recyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        messagesAdapter = MessagesAdapter(this)
        messagesAdapter!!.emptyList()
        binding!!.recyclerview.adapter = messagesAdapter
        viewModel!!.getDates(title!!, intentpackageName!!)

        viewModel!!.observerSortedList().observe(this, {
            messagesAdapter!!.submitList(it)
        })

        binding!!.imgBack.setOnClickListener {
            finish()
        }

        adsManager.loadNativeBannerMax(binding!!.relAds)
    }
}