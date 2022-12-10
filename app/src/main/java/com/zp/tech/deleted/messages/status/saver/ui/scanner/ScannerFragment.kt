package com.zp.tech.deleted.messages.status.saver.ui.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.embeded.BarcodeCallback
import com.example.embeded.BarcodeResult
import com.example.embeded.DecoratedBarcodeView
import com.example.embeded.DefaultDecoderFactory
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ads.AdsManager
import com.zp.tech.deleted.messages.status.saver.databinding.FragmentScannerBinding
import com.zp.tech.deleted.messages.status.saver.ui.BaseScannerFragment
import com.zp.tech.deleted.messages.status.saver.ui.ScannerActivity
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity
import com.zp.tech.deleted.messages.status.saver.utils.PermissionUtils
import com.zp.tech.deleted.messages.status.saver.utils.getFormats
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.math.roundToInt


class ScannerFragment : BaseScannerFragment<FragmentScannerBinding>(),
    DecoratedBarcodeView.TorchListener {

    private val DEFAULT_FRAME_THICKNESS_DP = 4f
    private val DEFAULT_FRAME_ASPECT_RATIO_WIDTH = 1f
    private val DEFAULT_FRAME_ASPECT_RATIO_HEIGHT = 1f
    private val DEFAULT_FRAME_CORNER_SIZE_DP = 40f
    private val DEFAULT_FRAME_CORNERS_RADIUS_DP = 20f
    private val DEFAULT_FRAME_SIZE = 0.68f
    private var permissionUtils: PermissionUtils? = null
    private var oldID = 0
    private var adManager: AdsManager? = null

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (!result.text.isNullOrEmpty()) {
                viewModel!!.saveCode(result.result, false, "")
                vibrate()
            } else {
                showEmptyCodeDialog()
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setLayoutResource(R.layout.fragment_scanner, inflater, container!!)

        permissionUtils = PermissionUtils(requireActivity())
        adManager= AdsManager(requireActivity())

        binding!!.barcodeScanner.decoderFactory = DefaultDecoderFactory(getFormats())
        binding!!.barcodeScanner.initializeFromIntent(requireActivity().intent)
        binding!!.barcodeScanner.decodeSingle(callback)
        binding!!.barcodeScanner.setTorchListener(this)

        val density = resources.displayMetrics.density
        binding!!.viewFinderView.setFrameAspectRatio(
            DEFAULT_FRAME_ASPECT_RATIO_WIDTH,
            DEFAULT_FRAME_ASPECT_RATIO_HEIGHT
        )
        binding!!.viewFinderView.maskColor =
            ContextCompat.getColor(requireActivity(), R.color.maskColor)
        binding!!.viewFinderView.frameColor =
            ContextCompat.getColor(requireActivity(), R.color.frameColor)
        binding!!.viewFinderView.frameThickness = Math.round(DEFAULT_FRAME_THICKNESS_DP * density)
            .toInt()
        binding!!.viewFinderView.frameCornersSize =
            (DEFAULT_FRAME_CORNER_SIZE_DP * density).roundToInt()
        binding!!.viewFinderView.frameCornersRadius =
            (DEFAULT_FRAME_CORNERS_RADIUS_DP * density).roundToInt()
        binding!!.viewFinderView.frameSize = DEFAULT_FRAME_SIZE
        permissionUtils!!.requestPermission()
        binding!!.btnBack.setOnClickListener { onClick(it) }
        binding!!.imgFlashOn.setOnClickListener { onClick(it) }
        binding!!.imgFlashOff.setOnClickListener { onClick(it) }
        binding!!.imgImage.setOnClickListener { onClick(it) }
        viewModel!!.idLiveData.observe(requireActivity()) {
            if (oldID != it) {
                oldID = it
                startActivity(
                    Intent(
                        requireActivity(),
                        ScannerDetailsActivity::class.java
                    ).putExtra("id", it)
                )
                showInterstitial()
            }
        }
        adManager!!.loadNativeBannerMaxTransparent(binding!!.relAds)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        decodeSingle()
        binding!!.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding!!.barcodeScanner.pause()
    }

    override fun onTorchOn() {
        binding!!.imgFlashOn.visibility = View.GONE
        binding!!.imgFlashOff.visibility = View.VISIBLE
    }

    override fun onTorchOff() {
        binding!!.imgFlashOn.visibility = View.VISIBLE
        binding!!.imgFlashOff.visibility = View.GONE
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                requireActivity().finish()
            }
            R.id.imgFlashOn -> {
                binding!!.barcodeScanner.setTorchOn()
            }
            R.id.imgFlashOff -> {
                binding!!.barcodeScanner.setTorchOff()
            }

            R.id.imgImage -> {
                val pickIntent = Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startForResult.launch(pickIntent)
            }
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data == null || data.data == null) {
                    decodeSingle()
                    Log.e(
                        "TAG",
                        "The uri is null, probably the user cancelled the image selection process using the back button."
                    )

                } else {
                    val uri: Uri? = data?.data
                    try {
                        val inputStream: InputStream? =
                            uri?.let { requireActivity().contentResolver.openInputStream(it) }
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        if (bitmap == null) {
                            Log.e("TAG", "uri is not a bitmap," + uri.toString())

                        } else {
                            val width = bitmap.width
                            val height = bitmap.height
                            val pixels = IntArray(width * height)
                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                            val source = RGBLuminanceSource(width, height, pixels)
                            val bBitmap = BinaryBitmap(HybridBinarizer(source))
                            val reader = MultiFormatReader()
                            try {
                                val result: Result = reader.decode(bBitmap)
                                if (!result.text.isNullOrEmpty()) {
                                    viewModel!!.saveCode(result, false, "")
                                    vibrate()
                                } else {
                                    showEmptyCodeDialog()
                                }
                            } catch (e: NotFoundException) {
                                showEmptyCodeDialog()
                                Log.e("TAG", "decode exception", e)
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        showToast(e.message!!)
                        decodeSingle()
                    }

                }
            }
        }

    private fun decodeSingle() {
        binding!!.barcodeScanner.decodeSingle(callback)
    }

    fun showEmptyCodeDialog() {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.empty_dialog, null)
        val close = view.findViewById<TextView>(R.id.btnClose)
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(false)
        close.setOnClickListener {
            dialog?.dismiss()
            decodeSingle()
        }
        builder.setView(view)
        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.30).toInt()
        dialog.window!!.setLayout(width, height)
    }


    fun showInterstitial(){
        (activity as ScannerActivity).showAd()
    }
}