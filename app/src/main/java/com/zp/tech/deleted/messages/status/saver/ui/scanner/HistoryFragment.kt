package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.TabAdapter
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentHistoryBinding
import com.zp.tech.deleted.messages.status.saver.ui.BaseScannerFragment

class HistoryFragment : BaseScannerFragment<FragmentHistoryBinding>() {

    private val tabTitles = listOf(R.string.scanned_qr, R.string.generated_qr)
    private var adsManager: AdsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        setLayoutResource(R.layout.fragment_history, inflater, container!!)

        val adapter = TabAdapter(requireActivity())
        adapter.add(HistoryScannedFragment())
        adapter.add(HistoryGeneratedFragment())
        binding!!.viewPager.adapter = adapter
        TabLayoutMediator(
            binding!!.tabs,
            binding!!.viewPager
        ) { tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()

        adsManager = AdsManager(requireActivity())
        adsManager!!.loadNativeBannerMax(binding!!.relAds)
        return binding!!.root
    }

}