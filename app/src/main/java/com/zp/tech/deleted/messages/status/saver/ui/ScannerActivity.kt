package com.zp.tech.deleted.messages.status.saver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.ActivityScannerBinding
import com.zp.tech.deleted.messages.status.saver.ui.scanner.CreateFragment
import com.zp.tech.deleted.messages.status.saver.ui.scanner.HistoryFragment
import com.zp.tech.deleted.messages.status.saver.ui.scanner.ScannerFragment


class ScannerActivity : ScannerBaseActivity<ActivityScannerBinding>() {

    private val fragmentScanner: Fragment = ScannerFragment()
    private val fragmentHistory: Fragment = HistoryFragment()
    private val fragmentCreated: Fragment = CreateFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentScanner
    val adsManager = AdsManager(this)

    private var viewModel: ScannerSharedViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.activity_scanner)
        viewModel = ViewModelProvider(this)[ScannerSharedViewModel::class.java]
        binding!!.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.fragment_scanner -> {
                    fm.beginTransaction().hide(active).show(fragmentScanner).commit()
                    active = fragmentScanner
                }
                R.id.fragment_history -> {
                    fm.beginTransaction().hide(active).show(fragmentHistory).commit()
                    active = fragmentHistory
                }
                R.id.fragment_create -> {
                    fm.beginTransaction().hide(active).show(fragmentCreated).commit()
                    active = fragmentCreated
                }

            }
            true
        }



        fm.beginTransaction().add(R.id.frameLayout, fragmentCreated, "3").hide(fragmentCreated)
            .commit()
        fm.beginTransaction().add(R.id.frameLayout, fragmentHistory, "2").hide(fragmentHistory)
            .commit()
        fm.beginTransaction().add(R.id.frameLayout, fragmentScanner, "1").commit()

        adsManager.loadAdmobInterstitialMain()
    }

    fun showAd() {
        adsManager.showInterstitial()
    }
}
