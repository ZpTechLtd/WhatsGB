package com.zp.tech.deleted.messages.status.saver.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.databinding.ItemMessageBinding
import com.zp.tech.deleted.messages.status.saver.databinding.ItemMessageHeaderBinding
import com.zp.tech.deleted.messages.status.saver.models.Messages
import com.zp.tech.deleted.messages.status.saver.ui.dialogs.MessageViewDialog
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


const val VIEW_TYPE_HEADER = 1
const val VIEW_TYPE_Footer = 2

//dd-MMMM-yyyy
class MessagesAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: List<Messages> = ArrayList()

    inner class MessageHolder(itemView: ItemMessageBinding) :
        RecyclerView.ViewHolder(itemView.root), View.OnClickListener {
        val binding = itemView

        init {
            binding.rel.setOnClickListener(this)
            binding.txtMessage.setOnClickListener(this)
        }

        fun bind(whatMessages: com.zp.tech.deleted.messages.status.saver.database.Messages) {
            binding.txtMessage.text = whatMessages.text
            binding.txtTime.text = whatMessages.time
            binding.carview.setBackgroundResource(
                if (whatMessages.isDeleted) R.drawable.messagebox_deleted else R.drawable.messagebox

            )
        }

        override fun onClick(v: View?) {
            MessageViewDialog(context, list[absoluteAdapterPosition].message.text).showDialog()
        }
    }

    inner class HeaderHolder(itemView: ItemMessageHeaderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        fun bind(messages: com.zp.tech.deleted.messages.status.saver.database.Messages) {
            try {
                val format: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val date: Date = format.parse(messages.date)
                binding.txtDate.text = formatter.format(date)
            } catch (exp: ParseException) {
                binding.txtDate.text = messages.date
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_HEADER) {
            return HeaderHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_message_header,
                    parent,
                    false
                )
            )
        } else {
            return MessageHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_message,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        when (message.viewType) {
            VIEW_TYPE_HEADER -> {
                (holder as HeaderHolder).bind(message.message)

            }
            VIEW_TYPE_Footer -> {
                (holder as MessageHolder).bind(message.message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    fun emptyList() {
        asyncListDiffer.submitList(list)
    }

    fun submitList(it: List<Messages>?) {
        list = it!!
        asyncListDiffer.submitList(it)

    }

    private val itemCallback = object : DiffUtil.ItemCallback<Messages>() {
        override fun areItemsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem == newItem
        }
    }
    var asyncListDiffer: AsyncListDiffer<Messages> =
        AsyncListDiffer(this, itemCallback)
}
