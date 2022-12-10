package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.zxing.client.result.ParsedResultType
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.QrChoiceAdapter
import com.zp.tech.deleted.messages.status.saver.adapters.QrOptions
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentCreateBinding
import com.zp.tech.deleted.messages.status.saver.ui.BaseScannerFragment
import com.zp.tech.deleted.messages.status.saver.ui.ScannerActivity
import com.zp.tech.deleted.messages.status.saver.utils.GridSpacingItemDecoration

class CreateFragment : BaseScannerFragment<FragmentCreateBinding>() {
    private var adManager: AdsManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setLayoutResource(R.layout.fragment_create, inflater, container!!)
        adManager= AdsManager(requireActivity())

        val list = ArrayList<QrOptions>()
        list.add(
            QrOptions(
                getString(R.string.wifi_cr),
                ParsedResultType.WIFI
            )
        )
        list.add(
            QrOptions(
                getString(R.string.website_cr),
                ParsedResultType.URI
            )
        )
        list.add(
            QrOptions(
                getString(R.string.text_cr),
                ParsedResultType.TEXT
            )
        )
        list.add(
            QrOptions(
                getString(R.string.contatcs_cr),
                ParsedResultType.ADDRESSBOOK
            )
        )
        list.add(
            QrOptions(
                getString(R.string.tel_cr),
                ParsedResultType.TEL
            )
        )
        list.add(
            QrOptions(
                getString(R.string.email_cr),
                ParsedResultType.EMAIL_ADDRESS
            )
        )
        list.add(
            QrOptions(
                getString(R.string.sms_cr),
                ParsedResultType.SMS
            )
        )
//        list.add(
//            QrOptions(
//                getString(R.string.calendar_cr),
//                ParsedResultType.CALENDAR
//            )
//        )

        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding!!.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                3, resources.getDimensionPixelOffset(
                    com.intuit.sdp.R.dimen._20sdp
                ), true
            )
        )
        val adapter = QrChoiceAdapter(this,list)
        binding!!.recyclerView.adapter = adapter

        adManager!!.loadNativeBannerMax(binding!!.relAds)

        return binding!!.root
    }

    fun showInterstitial(){
        (activity as ScannerActivity).showAd()
    }
}