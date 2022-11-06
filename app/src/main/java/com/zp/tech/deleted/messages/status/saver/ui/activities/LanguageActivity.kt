package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.LanguageAdapter
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityLanguageBinding
import com.zp.tech.deleted.messages.status.saver.utils.IS_FROM_PERMISSIONS_SCREEN
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecorator
import com.zp.tech.deleted.messages.status.saver.utils.PreferenceManager
import com.zp.tech.deleted.messages.status.saver.utils.getLanguages

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private var adapter: LanguageAdapter? = null
    private var preferenceManager: PreferenceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_language)
        preferenceManager = PreferenceManager(this)
        binding!!.backToolbar.txtTitle.text = getString(R.string.languages)
        binding!!.backToolbar.txtTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding!!.backToolbar.btnBack.setColorFilter(R.color.black)
        binding!!.backToolbar.btnBack.setOnClickListener { onClick(it) }
        binding!!.btnStart.setOnClickListener { onClick(it) }

        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.addItemDecoration(ItemDecorator(resources.getDimensionPixelOffset(R.dimen._5sdp)))
        adapter = LanguageAdapter(this@LanguageActivity, getLanguages(),
            preferenceManager!!.getLanguageCode()!!)
        binding!!.recyclerView.adapter = adapter

        AdsManager(this@LanguageActivity).loadNativeBannerMax(binding!!.relAds)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                if (intent.getBooleanExtra(IS_FROM_PERMISSIONS_SCREEN, false)) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    finish()
                }
            }
            R.id.btnStart -> {
                preferenceManager?.setLanguageCode(adapter!!.langCode)
                setLanguage(adapter!!.langCode)
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }

    }
}