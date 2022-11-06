package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.UsersAdapter
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentMessagesBinding
import com.zp.tech.deleted.messages.status.saver.notificationService.NotificationMediaService.WHTAPP
import com.zp.tech.deleted.messages.status.saver.notificationService.NotificationMediaService.WHTSBUSINESS
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecorator
import com.zp.tech.deleted.messages.status.saver.viewModels.SharedViewModel
import com.zp.tech.deleted.messages.status.saver.widget.EmptyDataObserver

class MessagesFragment : BaseFragment() {
    private var viewModel: SharedViewModel? = null
    private var binding: FragmentMessagesBinding? = null
    private var adapter: UsersAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for requireContext() fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding!!.bottomButtons.txtWhatsApp.setOnClickListener(this::onClick)
        binding!!.bottomButtons.txtBusiness.setOnClickListener(this::onClick)
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.recyclerView.hasFixedSize()
        addGesture(binding!!.recyclerView)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.recyclerView.addItemDecoration(ItemDecorator(resources.getDimensionPixelOffset(R.dimen._10sdp)))
		binding!!.emptyView.buttonRoot.setBackgroundColor(resources.getColor(R.color.white))
        adapter = UsersAdapter(this, ArrayList())
        adapter!!.registerAdapterDataObserver(EmptyDataObserver(binding!!.recyclerView, binding!!.emptyView.root))
        binding!!.recyclerView.adapter = adapter
        binding!!.bottomButtons.txtWhatsApp.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.purple_500
            )
        )
        binding!!.bottomButtons.txtWhatsApp.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding!!.bottomButtons.txtBusiness.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        binding!!.bottomButtons.txtBusiness.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.bottomButtonUnselected
            )
        )
        viewModel!!.observerUsers().observe(requireActivity(), { users ->
//            if (users.isNullOrEmpty()) {
//
//            } else {
            adapter!!.setData(users)
            // }
        })

    }

    private fun onClick(view: View) {
        when (view.id) {

            R.id.txtWhatsApp -> {
                if (isAppInstalled(WHTAPP)) {
                    binding!!.bottomButtons.txtWhatsApp.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_500
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.bottomButtonUnselected
                        )
                    )
                    viewModel!!.setChatTypeMessage(MainActivity.ChatType.WHATSAPP)
                } else {
                    showDialog(getString(R.string.what_not_installed_notification))
                }
            }

            R.id.txtBusiness -> {
                if (isAppInstalled(WHTSBUSINESS)) {
                    binding!!.bottomButtons.txtBusiness.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_500
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.bottomButtonUnselected
                        )
                    )
                    viewModel!!.setChatTypeMessage(MainActivity.ChatType.BUSINESS)
                } else {
                    showDialog(getString(R.string.bussiness_not_installed_notifications))
                }

            }
        }
    }

    fun showInterstitial(){
        (activity as MainActivity).showAd()
    }
}