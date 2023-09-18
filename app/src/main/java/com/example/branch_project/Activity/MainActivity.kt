package com.example.branch_project.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.branch_project.Model.LoginCredentials
import com.example.branch_project.SharedPref.SharedPref
import com.example.branch_project.ViewModel.MessageViewModel
import com.example.branch_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authToken: String
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateLoginScreen()

        binding.btnViewAllMessages.setOnClickListener {
            goToAllMessagesActivity()
        }

        binding.btnLogout.setOnClickListener {
            logOutAndUpdateSharedPref()
            binding.llLogIn.visibility = View.VISIBLE
            binding.llLogoutViewAll.visibility = View.GONE
        }

        binding.etUsername.requestFocus()
//        binding.etUsername.setText("pratyakshkhuranaofficial@gmail.com").toString().trim()
//        binding.etPassword.setText("moc.liamg@laiciffoanaruhkhskaytarp").toString().trim()

        binding.btnSubmit.setOnClickListener {
            loginUsingUsernamePassword()
        }
    }

    private fun updateLoginScreen() {
        val isLoggedIn = SharedPref(this).getIsLoggedInKey("LoggedIn")
        if (isLoggedIn == "true") {
            binding.llLogIn.visibility = View.GONE
            binding.llLogoutViewAll.visibility = View.VISIBLE
        } else {
            binding.llLogIn.visibility = View.VISIBLE
            binding.llLogoutViewAll.visibility = View.GONE
        }
    }

    private fun logOutAndUpdateSharedPref() {
        SharedPref(this@MainActivity).setAuthToken("AuthToken", "")
        SharedPref(this@MainActivity).setIsLoggedInKey("LoggedIn", "")
    }

    private fun saveDataInSharedPreferences() {
        SharedPref(this).setAuthToken("AuthToken", authToken)
    }

    private fun goToAllMessagesActivity() {
        val i = Intent(this@MainActivity, AllMessagesActivity::class.java)
        startActivity(i)
    }

    private fun loginUsingUsernamePassword() {
        val loginCredentials =
            LoginCredentials(
                binding.etUsername.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )

        viewModel.postLogin(loginCredentials)
        viewModel.getAuthToken().observe(
            this,
            Observer {
                authToken = it
                if (authToken != "Invalid") {
                    goToAllMessagesActivity()
                    saveDataInSharedPreferences()
                } else {
                    binding.etUsername.requestFocus()
                    binding.etUsername.text.clear()
                    binding.etPassword.text.clear()
                    Toast.makeText(this, authToken, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
