package com.zp.tech.deleted.messages.status.saver.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.adapters.DownloadAdapter
import com.zp.tech.deleted.messages.status.saver.adapters.LiveStatusAdapter
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentStatusBinding
import com.zp.tech.deleted.messages.status.saver.notificationService.NotificationMediaService
import com.zp.tech.deleted.messages.status.saver.viewModels.SharedViewModel
import com.zp.tech.deleted.messages.status.saver.models.StatusModel
import com.zp.tech.deleted.messages.status.saver.ui.*

import com.zp.tech.deleted.messages.status.saver.utils.Constants
import com.zp.tech.deleted.messages.status.saver.utils.GridSpacingItemDecoration
import com.zp.tech.deleted.messages.status.saver.utils.ItemDecoratorHorizontal

class StatusFragment : BaseFragment() {

    private var binding: FragmentStatusBinding? = null
    private var viewModel: SharedViewModel? = null
    private var statusType: MainActivity.ChatType? = null
    private val WHTSBUSINESS = "com.whatsapp.w4b"
    private val WHTAPP = "com.whatsapp"
    private var liveStatusAdapter: LiveStatusAdapter? = null
    private var downloadAdapter: DownloadAdapter? = null
    private var intent: Intent? = null

    val resultBusiness =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data!!.data
                uri?.let {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                val document = uri?.let { DocumentFile.fromTreeUri(requireActivity(), it) }
                if (document?.uri.toString()
                        .contains("com.whatsapp.w4b") && document?.uri.toString()
                        .endsWith(".Statuses")
                ) {
                    viewModel!!.setAppUri(
                        document?.uri.toString(),
                        MainActivity.ChatType.BUSINESS
                    )
                    hidePermissionViews()
                    viewModel!!.observeStatusType().value = MainActivity.ChatType.BUSINESS
                }
            }
        }

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data!!.data
                uri?.let {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                val document = uri?.let { DocumentFile.fromTreeUri(requireActivity(), it) }
                if (document?.uri.toString().contains("com.whatsapp") && document?.uri.toString()
                        .endsWith(".Statuses")
                ) {
                    viewModel!!.setAppUri(
                        document?.uri.toString(),
                        MainActivity.ChatType.WHATSAPP
                    )
                    hidePermissionViews()
                    viewModel!!.observeStatusType().value = MainActivity.ChatType.WHATSAPP
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

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

        binding!!.bottomButtons.txtWhatsApp.setOnClickListener(this::onClick)
        binding!!.bottomButtons.txtBusiness.setOnClickListener(this::onClick)
        binding!!.btnGrant.setOnClickListener(this::onClick)


        binding!!.recyclerviewStatuses.setHasFixedSize(true)
        addGesture(binding!!.recyclerviewStatuses)
        val gridLayoutManager = GridLayoutManager(requireActivity(),2)
        binding!!.recyclerviewStatuses.layoutManager = gridLayoutManager
        binding!!.recyclerviewStatuses.addItemDecoration(GridSpacingItemDecoration(2,resources.getDimensionPixelOffset(R.dimen._10sdp),true))

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.observeStatusType()?.observe(requireActivity(), {
            statusType = it
            viewModel!!.setStatusType(it)
            viewModel?.loadStatuses()
        })

        viewModel?.observeStatusPermissionLiveData()?.observe(requireActivity(), {
            if (it == MainActivity.ChatType.WHATSAPP) {
                if (isAppInstalled(WHTAPP)) {
                    showPermissionView()
                }
            } else {
                if (isAppInstalled(WHTSBUSINESS)) {
                    showPermissionView()
                }
            }

        })

        viewModel!!.observerStatusLiveData().observe(requireActivity(), {
            liveStatusAdapter =
                LiveStatusAdapter(this, it)
            binding!!.recyclerviewStatuses.adapter = liveStatusAdapter
        })


    }


    private fun onClick(view: View) {
        when (view.id) {

            R.id.txtWhatsApp -> {
                if (isAppInstalled(NotificationMediaService.WHTAPP)) {
                    hidePermissionViews()
                    binding!!.bottomButtons.txtWhatsApp.setBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.purple_500
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.black
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.bottomButtonUnselected
                        )
                    )
                    viewModel!!.setStatusType(MainActivity.ChatType.WHATSAPP)
                    viewModel!!.observeStatusType().value = MainActivity.ChatType.WHATSAPP

                } else {
                    showDialog("WhatsApp is not installed, Please install Whatsapp to get Statuses.")
                }
            }

            R.id.txtBusiness -> {
                if (isAppInstalled(NotificationMediaService.WHTSBUSINESS)) {
                    hidePermissionViews()
                    binding!!.bottomButtons.txtBusiness.setBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.purple_500
                        )
                    )
                    binding!!.bottomButtons.txtBusiness.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.black
                        )
                    )
                    binding!!.bottomButtons.txtWhatsApp.setBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.bottomButtonUnselected
                        )
                    )
                    viewModel!!.setStatusType(MainActivity.ChatType.BUSINESS)
                    viewModel!!.observeStatusType().value = MainActivity.ChatType.BUSINESS
                } else {
                    showDialog("WhatsApp Business is not installed, Please install Whatsapp Business to get Statuses.")
                }

            }

            R.id.btnGrant -> {
                if (statusType == MainActivity.ChatType.WHATSAPP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        openWhatsAppDirectory()
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        openBusinessAppDirectory()
                    }
                }
            }
        }
    }

    private fun launchIntent(intent: Intent) {
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun openWhatsAppDirectory() {
        val startDir: String
        val finalDirPath: String
        startDir =
//            "Android%2Fmedia%2Fcom.whatsapp%2FWhatsaApp"
            "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"

        val sm = requireActivity().getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/document/")
        finalDirPath = "$scheme%3A$startDir"
        uri = Uri.parse(finalDirPath)
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        result.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun openBusinessAppDirectory() {
        val startDir: String
        val finalDirPath: String
        startDir =
//            "Android%2Fmedia%2Fcom.whatsapp%2FWhatsaApp"
            "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses"

        val sm = requireActivity().getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/document/")
        finalDirPath = "$scheme%3A$startDir"
        uri = Uri.parse(finalDirPath)
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)

        resultBusiness.launch(intent)
    }

    private fun hidePermissionViews() {
        binding!!.relStatuses.visibility = View.VISIBLE
        binding!!.relPermissions.visibility = View.GONE
    }

    private fun showPermissionView() {
        binding!!.relStatuses.visibility = View.GONE
        binding!!.relPermissions.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        if (Constants.isStatusSaved==true) {
            viewModel!!.loadDownloadedStatuses()
            Constants.isStatusSaved=false
        }
    }


    fun showInterstitial(){
        (activity as MainActivity).showAd()
    }

}