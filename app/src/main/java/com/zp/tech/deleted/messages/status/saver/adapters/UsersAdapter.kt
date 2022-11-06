package com.zp.tech.deleted.messages.status.saver.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zp.tech.deleted.messages.status.saver.R
import com.zp.tech.deleted.messages.status.saver.database.Users
import com.zp.tech.deleted.messages.status.saver.databinding.ItemUsersBinding
import com.zp.tech.deleted.messages.status.saver.ui.activities.MessagesActivity
import com.zp.tech.deleted.messages.status.saver.ui.fragments.MessagesFragment
import java.text.SimpleDateFormat

class UsersAdapter(private val context: MessagesFragment, private var list: List<Users>) :
    RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    inner class UserHolder(itemView: ItemUsersBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        private var currentDate: String? = null

        init {
            currentDate = getDate(System.currentTimeMillis())
            binding.root.setOnClickListener(this::onClick)
        }

        fun bind(whatUsers: Users) {
            binding.txtTitle.text = whatUsers.title
            binding.txtMessage.text = whatUsers.text
            binding.txtDate.text =
                if (whatUsers.date == currentDate) whatUsers.time else whatUsers.date
            if (whatUsers.isGroupUser) {
                binding.icon.setImageResource(R.drawable.ic_user_groups)
            } else {
                binding.icon.setImageResource(R.drawable.ic_user)
            }
        }

        private fun getDate(time: Long): String? {
            @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return simpleDateFormat.format(time)
        }

        private fun onClick(view: View) {
            val userModel: Users = list[absoluteAdapterPosition]
            context.activity?.startActivity(
                Intent(context.activity, MessagesActivity::class.java).putExtra(
                    "title",
                    userModel.title
                )
                    .putExtra("package", userModel.packageName)
                    .putExtra("isGroup", userModel.isGroupUser)
            )
            context.showInterstitial()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_users,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) 0 else list.size
    }

    fun setData(users: List<Users>) {
        this.list = users
        notifyDataSetChanged()
    }
}