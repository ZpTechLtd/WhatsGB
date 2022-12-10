package com.zp.tech.deleted.messages.status.saver.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.ScannerSharedViewModel
import com.zp.tech.deleted.messages.status.saver.database.Scanner
import com.zp.tech.deleted.messages.status.saver.databinding.ItemQrHistoryBinding
import com.zp.tech.deleted.messages.status.saver.ui.scanner.HistoryGeneratedFragment
import com.zp.tech.deleted.messages.status.saver.ui.scanner.QrCreatedDetailsActivity
import com.zp.tech.deleted.messages.status.saver.ui.scanner.ScannerDetailsActivity
import com.zp.tech.deleted.messages.status.saver.utils.convertTime
import com.zp.tech.deleted.messages.status.saver.utils.scannerIcon

class QrCreationAdapter(val viewModel: ScannerSharedViewModel,
                        val context: HistoryGeneratedFragment,
                        private var list: List<Scanner>
) :
RecyclerView.Adapter<QrCreationAdapter.Holder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Scanner>() {
        override fun areItemsTheSame(oldItem: Scanner, newItem: Scanner): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Scanner, newItem: Scanner): Boolean {
            return newItem.id == oldItem.id
        }

    }
    private var differ = AsyncListDiffer(this, diffUtilCallBack)

    init {
        differ.submitList(list)
    }

    inner class Holder(itemView: ItemQrHistoryBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView

        init {
            binding.cardView.setOnClickListener { onCLick() }
            binding.imgDelete.setOnClickListener { onDeleteClick() }

        }

        fun bind(scanner: Scanner) {
            binding.txtTitle.text = scanner.title
            binding.txtTypeDate.text = "${scanner.type}, ${scanner.time.convertTime()}"
            binding.imgIcon.scannerIcon(scanner.type)
        }

        private fun onCLick() {
            binding.root.context.startActivity(
                Intent(
                    binding.root.context,
                    if (list[absoluteAdapterPosition].isCreated) QrCreatedDetailsActivity::class.java else ScannerDetailsActivity::class.java
                ).putExtra("id", list[absoluteAdapterPosition].id)
            )
            context.showInterstitial()
        }

        private fun onDeleteClick() {
            AlertDialog.Builder(context.requireContext()).setMessage(context.getString(R.string.delete_message_qr))
                .setPositiveButton(
                    context.getString(R.string.yes_qr)
                ) { p0, p1 -> viewModel.delete(list[adapterPosition].id) }.setNegativeButton(
                    context.getString(R.string.no_qr)
                ) { p0, p1 -> }.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_qr_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])

    }

    fun setData(list: List<Scanner>) {
        this.list = list
        differ.submitList(this.list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}