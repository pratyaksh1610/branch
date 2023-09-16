package com.example.branch_project.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branch_project.Adapter.AllMessagesAdapter
import com.example.branch_project.Adapter.OnClick
import com.example.branch_project.Api.RetrofitObject
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.SharedPref.SharedPref
import com.example.branch_project.databinding.ActivityAllMessagesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMessagesActivity : AppCompatActivity(), OnClick {

    private lateinit var binding: ActivityAllMessagesBinding
    private var listMessages = mutableListOf<AllMessages>()

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
        val authToken = SharedPref(this).getAuthToken("AuthToken")
        val retrofit = RetrofitObject.apiInterface.getAllMessages(authToken)

        retrofit.enqueue(object : Callback<List<AllMessages>?> {
            override fun onResponse(
                call: Call<List<AllMessages>?>,
                response: Response<List<AllMessages>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val listAllMessages = response.body()!!

                    // if it works, don't touch it
                    listMessages = listAllMessages.toMutableList()
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
            }

            override fun onFailure(call: Call<List<AllMessages>?>, t: Throwable) {
                Toast.makeText(
                    this@AllMessagesActivity,
                    "Error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
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
                finishAndRemoveTask()
            }
            .setNegativeButton("No") { _, _ ->
                SharedPref(this@AllMessagesActivity).setIsLoggedInKey("LoggedIn", "true")
                finishAffinity()
            }
            .show()
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    override fun onBackPressed() {
        openLogoutDialog()
    }
}
