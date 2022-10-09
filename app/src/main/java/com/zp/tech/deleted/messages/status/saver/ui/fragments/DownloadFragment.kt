package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.DownloadAdapter
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentDownloadBinding
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.*
import com.zp.tech.deleted.messages.status.saver.utils.Constants
import com.zp.tech.deleted.messages.status.saver.utils.GridSpacingItemDecoration
import com.zp.tech.deleted.messages.status.saver.viewModels.SharedViewModel


class DownloadFragment : BaseFragment() {
    private var binding: FragmentDownloadBinding? = null
    private var viewModel: SharedViewModel? = null
    private var downloadAdapter: DownloadAdapter? = null
    private var intent: Intent? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        viewModel!!.loadDownloadedStatuses()
        binding!!.recyclerviewDownloaded.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireActivity(), 3)
        binding!!.recyclerviewDownloaded.layoutManager = gridLayoutManager
        addGesture(binding!!.recyclerviewDownloaded)
        binding!!.recyclerviewDownloaded.addItemDecoration(GridSpacingItemDecoration(3,resources.getDimensionPixelOffset(R.dimen._10sdp),true))

        viewModel!!.observeDownloadStatuses().observe(requireActivity(), {
            downloadAdapter = DownloadAdapter(this, it)
            binding!!.recyclerviewDownloaded.adapter = downloadAdapter
        })


        return binding!!.root
    }

    fun onItemClick(model: StatusModel, show: Boolean) {
        intent = if (model.usesUri) {
            if (model.uri.toString().contains(".mp4")) {
                Intent(requireActivity(), VideoActivity::class.java)
            } else {
                Intent(requireActivity(), ImageViewActivity::class.java)
            }
        } else {
            if (model.path.contains(".mp4")) {
                Intent(requireActivity(), VideoActivity::class.java)
            } else {
                Intent(requireActivity(), ImageViewActivity::class.java)
            }
        }
        intent!!.putExtra(modelIntent, model)
        intent!!.putExtra(showDownloadButton, show)
        launchIntent(intent!!)
    }

    private fun launchIntent(intent: Intent) {
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (Constants.isStatusSaved == true) {
            viewModel!!.loadDownloadedStatuses()
            Constants.isStatusSaved = false
        }
    }


    fun showInterstitial() {
        (activity as MainActivity).showAd()
    }
}