package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.MediaAdapter
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentMediaBinding
import com.zp.tech.deleted.messages.status.saver.ui.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecorator
import com.zp.tech.deleted.messages.status.saver.viewModels.SharedViewModel
import com.zp.tech.deleted.messages.status.saver.widget.EmptyDataObserver


class MediaFragment : BaseFragment() {

    private var binding: FragmentMediaBinding? = null
    private var viewModel: SharedViewModel? = null
    private var adapter: MediaAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel!!.getDeletedMedia()
        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        addGesture(binding!!.recyclerView)
        binding!!.recyclerView.addItemDecoration(ItemDecorator(resources.getDimensionPixelOffset(R.dimen._10sdp)))
        binding!!.emptyView.buttonRoot.setBackgroundColor(resources.getColor(R.color.white))
        binding!!.emptyView.txtEmptyPlaceHolder.text =
            resources.getString(R.string.media_placeholder)
        binding!!.refreshLayout.setOnRefreshListener {
            viewModel!!.getDeletedMedia()
        }
        adapter = MediaAdapter(this,arrayListOf())
        adapter!!.registerAdapterDataObserver(
            EmptyDataObserver(
                binding!!.recyclerView,
                binding!!.emptyView.root
            )
        )
        binding!!.recyclerView.adapter = adapter
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel!!.observeDeletedMedia().observe(requireActivity(), {
            adapter!!.setData(it)

            if (binding!!.refreshLayout.isRefreshing) {
                binding!!.refreshLayout.isRefreshing = false
            }

        })
    }

    fun showInterstitial(){
        (activity as MainActivity).showAd()
    }
}