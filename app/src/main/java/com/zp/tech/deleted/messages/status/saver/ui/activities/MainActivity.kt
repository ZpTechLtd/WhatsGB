package com.zp.tech.deleted.messages.status.saver.ui.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.TabAdapter
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityMainBinding
import com.zp.tech.deleted.messages.status.saver.ui.fragments.DownloadFragment
import com.zp.tech.deleted.messages.status.saver.ui.fragments.MediaFragment
import com.zp.tech.deleted.messages.status.saver.ui.fragments.MessagesFragment
import com.zp.tech.deleted.messages.status.saver.ui.fragments.StatusFragment
import com.zp.tech.deleted.messages.status.saver.utils.IS_FROM_PERMISSIONS_SCREEN
import com.zp.tech.deleted.messages.status.saver.utils.ShareUtils
import com.zp.tech.deleted.messages.status.saver.viewModels.SharedViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {
    public enum class ChatType {
        WHATSAPP,
        BUSINESS
    }

    private var viewModel: SharedViewModel? = null
    private val titleList = arrayListOf(R.string.notifications,
        R.string.statuses,
        R.string.downloaded,
        R.string.recover_media)
    private val WHTSBUSINESS = "com.whatsapp.w4b"
    private val WHTAPP = "com.whatsapp"
    private var chatType: ChatType? = null
    val adsManager = AdsManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutResource(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        chatType = ChatType.WHATSAPP
        getUsers()
        binding!!.navigationView.setNavigationItemSelectedListener(this)
        binding!!.navigationView.itemIconTintList =
            ColorStateList.valueOf(resources.getColor(R.color.purple_500))
        binding!!.imgToggle.setOnClickListener(this::onClick)
        binding!!.imgLanguage.setOnClickListener { onClick(it) }

        viewModel!!.observeChatTypeMessages().observe(this, {
            if (it == ChatType.WHATSAPP) {
                chatType = ChatType.WHATSAPP
                getUsers()
            } else {
                chatType = ChatType.BUSINESS
                getUsers()
            }
        })

        val adapter = TabAdapter(this)
        adapter.add(MessagesFragment())
        adapter.add(StatusFragment())
        adapter.add(DownloadFragment())
        adapter.add(MediaFragment())
        binding!!.viewPager.adapter = adapter
        binding!!.viewPager.offscreenPageLimit = 4
        TabLayoutMediator(
            binding!!.tabLayout,
            binding!!.viewPager
        ) { tab, position -> tab.text = getString(titleList[position]) }.attach()

        adsManager.loadNativeBannerMax(binding!!.relAds)
        adsManager.loadMaxInterstitial()
        // AppLovinSdk.getInstance( this ).showMediationDebugger()
    }

    private fun getUsers() {
        if (chatType == ChatType.WHATSAPP) {
            viewModel!!.getUser(WHTAPP)
        } else {
            viewModel!!.getUser(WHTSBUSINESS)
        }
    }

    fun showAd() {
        adsManager.showInterstitial()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.restartService -> {
                launchNotificationIntent()
            }

            R.id.privacyPolicy -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sites.google.com/view/recover-deleted-messages-wa/home")
                )
                startActivity(browserIntent)

            }
            R.id.rateus -> {
                ShareUtils.rateUs(this)
            }
            R.id.share -> {
                ShareUtils.shareApp(this)
            }

        }
        binding!!.draweLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.imgToggle -> {
                if (binding!!.draweLayout.isDrawerOpen(GravityCompat.START)) {
                    binding!!.draweLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding!!.draweLayout.openDrawer(GravityCompat.START)
                }
            }
            R.id.imgLanguage -> {
                startActivity(Intent(this, LanguageActivity::class.java).putExtra(
                    IS_FROM_PERMISSIONS_SCREEN, false))
            }
        }

    }


    override fun onBackPressed() {
        if (binding!!.draweLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.draweLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        startNotificationService()
    }
}