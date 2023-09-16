package com.example.branch_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.R

class SeparateThreadAdapter(
    private val context: Context,
    private val list: MutableList<AllMessages>
) : RecyclerView.Adapter<SeparateThreadAdapter.SeparateThreadViewHolder>() {

    class SeparateThreadViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val body: TextView = item.findViewById(R.id.tvBody1)!!

        val agentId: TextView = item.findViewById(R.id.tvAgentId)!!
        val timeStamp: TextView = item.findViewById(R.id.tvTimeStamp1)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeparateThreadViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.separate_thread_each_item, parent, false)
        return SeparateThreadViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newItems: AllMessages) {
        list.add(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SeparateThreadViewHolder, position: Int) {
        val curr = list[position]
        holder.agentId.text = "Agent id - " + curr.agent_id.toString()
        holder.body.text = "Body - " + curr.body
        holder.timeStamp.text = "Timestamp - " + curr.timestamp
    }
}
