package com.example.branch_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.branch_project.Activity.AllMessagesActivity
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.R

class AllMessagesAdapter(
    private val context: Context,
    private val list: List<AllMessages>,
    private val listen: AllMessagesActivity
) :
    RecyclerView.Adapter<AllMessagesAdapter.AllMessagesViewHolder>() {

    class AllMessagesViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val body: TextView = item.findViewById(R.id.tvBody)!!
        val userId: TextView = item.findViewById(R.id.tvUserId)!!
        val timeStamp: TextView = item.findViewById(R.id.tvTimeStamp)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMessagesViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.all_messages_each_item, parent, false)
        return AllMessagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllMessagesViewHolder, position: Int) {
        val curr = list[position]
        holder.userId.text = "User id - " + curr.user_id
        holder.body.text = "Body - " + curr.body
        holder.timeStamp.text = "Timestamp - " + curr.timestamp
        holder.itemView.setOnClickListener {
            listen.onClickMessage(curr.thread_id.toString(), curr.body)
        }
    }
}

interface OnClick {
    fun onClickMessage(thread_id: String, body: String)
}
