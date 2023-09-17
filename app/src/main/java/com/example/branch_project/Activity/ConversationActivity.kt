package com.example.branch_project.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branch_project.Adapter.SeparateThreadAdapter
import com.example.branch_project.Api.RetrofitObject
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.Model.SendMessage
import com.example.branch_project.SharedPref.SharedPref
import com.example.branch_project.databinding.ActivityConversationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationBinding
    private var listMessagesEachThread = mutableListOf<AllMessages>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSendMessage.requestFocus()
        setRecyclerView()
        allMessagesRelatedToThreadId()
        binding.btnSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun setRecyclerView() {
        binding.rvEachMessage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun allMessagesRelatedToThreadId() {
        val authToken = SharedPref(this).getAuthToken("AuthToken")
        val threadId = SharedPref(this).getThreadId("ThreadId")

        val retrofit =
            RetrofitObject.apiInterface.getAllMessages(authToken)

        retrofit.enqueue(object : Callback<List<AllMessages>?> {
            override fun onResponse(
                call: Call<List<AllMessages>?>,
                response: Response<List<AllMessages>?>
            ) {
                val result = response.body()!!

                for (i in result) {
                    if (i.thread_id == threadId.toInt()) {
                        listMessagesEachThread.add(
                            AllMessages(
                                i.id,
                                i.thread_id,
                                i.body,
                                i.agent_id.toString(),
                                i.body,
                                i.timestamp
                            )
                        )
                    }
                }
                listMessagesEachThread.sortByDescending { it.timestamp }
                listMessagesEachThread.reverse()
                val adapter =
                    SeparateThreadAdapter(this@ConversationActivity, listMessagesEachThread)
                binding.rvEachMessage.adapter = adapter
                binding.rvEachMessage.scrollToPosition(listMessagesEachThread.size - 1)
            }

            override fun onFailure(call: Call<List<AllMessages>?>, t: Throwable) {
                Toast.makeText(
                    this@ConversationActivity,
                    "Error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun sendMessage() {
        val authToken = SharedPref(this).getAuthToken("AuthToken")
        val threadId = SharedPref(this).getThreadId("ThreadId")

        val messageSent = binding.tvSendMessage.text.toString()

        if (messageSent.isNotEmpty()) {
            val retrofit =
                RetrofitObject.apiInterface.sendMessage(
                    authToken,
                    SendMessage(threadId.toInt(), messageSent)
                )

            retrofit.enqueue(object : Callback<AllMessages?> {
                override fun onResponse(
                    call: Call<AllMessages?>,
                    response: Response<AllMessages?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!
                        val adapter =
                            SeparateThreadAdapter(this@ConversationActivity, listMessagesEachThread)
                        binding.rvEachMessage.adapter = adapter
                        adapter.updateList(
                            AllMessages(
                                result.id,
                                result.thread_id,
                                result.user_id,
                                result.agent_id,
                                result.body,
                                result.timestamp
                            )
                        )
                        binding.rvEachMessage.scrollToPosition(listMessagesEachThread.size - 1)
                        binding.tvSendMessage.text.clear()
                    }
                }

                override fun onFailure(call: Call<AllMessages?>, t: Throwable) {
                    Toast.makeText(
                        this@ConversationActivity,
                        "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}
