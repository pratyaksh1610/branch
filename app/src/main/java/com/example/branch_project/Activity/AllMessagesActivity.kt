package com.example.branch_project.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branch_project.Adapter.AllMessagesAdapter
import com.example.branch_project.Adapter.OnClick
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.SharedPref.SharedPref
import com.example.branch_project.ViewModel.MessageViewModel
import com.example.branch_project.databinding.ActivityAllMessagesBinding

class AllMessagesActivity : AppCompatActivity(), OnClick {

    private lateinit var binding: ActivityAllMessagesBinding
    private var listMessages = mutableListOf<AllMessages>()
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMessagesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setRecyclerView()
        getAllMessages()
    }

    private fun setRecyclerView() {
        binding.rvAllMessages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getAllMessages() {
        val authToken = SharedPref(this@AllMessagesActivity).getAuthToken("AuthToken")

        viewModel.getAllMessages(authToken)

        viewModel.getListOfAllMessages().observe(
            this,
            Observer { itt ->

                listMessages = itt?.toMutableList()!!
                listMessages.sortByDescending { it.timestamp }

                val groupItems = listMessages.groupBy { it.user_id }

                // Find the item with the latest timestamp in each group
                val distinctItemsWithLatestTimestamp = groupItems.values.mapNotNull { group ->
                    group.maxByOrNull { it.timestamp }
                }

                val adapter =
                    AllMessagesAdapter(
                        this@AllMessagesActivity,
                        distinctItemsWithLatestTimestamp,
                        this@AllMessagesActivity
                    )
                binding.rvAllMessages.adapter = adapter
                binding.rvAllMessages.scrollToPosition(0)
            }
        )
    }

    override fun onClickMessage(thread_id: String, body: String) {
        SharedPref(this@AllMessagesActivity).setThreadId("ThreadId", thread_id)
        goToConversationActivity()
    }

    private fun goToConversationActivity() {
        val i = Intent(this@AllMessagesActivity, ConversationActivity::class.java)
        startActivity(i)
    }

    private fun logOutAndUpdateSharedPref() {
        SharedPref(this@AllMessagesActivity).setAuthToken("AuthToken", "")
        SharedPref(this@AllMessagesActivity).setIsLoggedInKey("LoggedIn", "")
    }

    private fun openLogoutDialog() {
        AlertDialog.Builder(this@AllMessagesActivity)
            .setTitle("Do you want to log out ?")
            .setPositiveButton("Yes") { _, _ ->
                logOutAndUpdateSharedPref()
                finishAffinity()
                startActivity(Intent(this@AllMessagesActivity, MainActivity::class.java))
            }
            .setNegativeButton("No") { _, _ ->
                SharedPref(this@AllMessagesActivity).setIsLoggedInKey("LoggedIn", "true")
                finishAffinity()
                startActivity(Intent(this@AllMessagesActivity, MainActivity::class.java))
            }
            .show()
    }

    override fun onRestart() {
        super.onRestart()
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (SharedPref(this@AllMessagesActivity).getIsLoggedInKey("LoggedIn") != "true") {
            openLogoutDialog()
        } else {
            finishAffinity()
            startActivity(Intent(this@AllMessagesActivity, MainActivity::class.java))
            super.onBackPressed()
        }
    }
}
