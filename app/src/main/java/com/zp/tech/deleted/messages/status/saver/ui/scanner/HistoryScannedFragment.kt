package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import com.zp.tech.deleted.messages.status.saver.adapters.QRHistoryAdapter
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentHistoryScannedBinding
import com.zp.tech.deleted.messages.status.saver.ui.ScannerActivity
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecorator


class HistoryScannedFragment : Fragment() {

    private var binding: FragmentHistoryScannedBinding? = null
    private var viewModel: ScannerSharedViewModel? = null

    private var adapter: QRHistoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_history_scanned, container, false)
        viewModel = ViewModelProvider(this)[ScannerSharedViewModel::class.java]
        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.recyclerView.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        binding!!.recyclerView.addItemDecoration(
            ItemDecorator(
                resources.getDimensionPixelOffset(
                    com.intuit.sdp.R.dimen._10sdp
                )
            )
        )
        adapter = QRHistoryAdapter(viewModel!!,this, listOf())
        binding!!.recyclerView.adapter = adapter
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel!!.getAllCodes(false)
        viewModel!!.historyLiveData.observe(
            requireActivity()
        ) { t ->
            if (t!!.isNotEmpty()) {
                adapter!!.setData(t)
            }
        }
    }

    fun showInterstitial(){
        (activity as ScannerActivity).showAd()
    }
}