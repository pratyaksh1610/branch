package com.example.branch_project.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.branch_project.Api.RetrofitObject
import com.example.branch_project.Model.AuthToken
import com.example.branch_project.Model.LoginCredentials
import com.example.branch_project.SharedPref.SharedPref
import com.example.branch_project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLoggedIn = SharedPref(this).getIsLoggedInKey("LoggedIn")
        if (isLoggedIn == "true") {
            goToAllMessagesActivity()
        }

        binding.etUsername.requestFocus()
//        binding.etUsername.setText("pratyakshkhuranaofficial@gmail.com").toString().trim()
//        binding.etPassword.setText("moc.liamg@laiciffoanaruhkhskaytarp").toString().trim()

        binding.btnSubmit.setOnClickListener {
            loginUsingUsernamePassword()
        }
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

        val retrofit = RetrofitObject.apiInterface.postLogin(loginCredentials)

        retrofit.enqueue(object : Callback<AuthToken?> {
            override fun onResponse(call: Call<AuthToken?>, response: Response<AuthToken?>) {
                if (response.isSuccessful && response.body() != null) {
                    authToken = response.body()?.auth_token.toString()

                    saveDataInSharedPreferences()
                    goToAllMessagesActivity()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Invalid credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.etUsername.text.clear()
                    binding.etPassword.text.clear()
                    binding.etUsername.requestFocus()
                    binding.etUsername.setSelection(0)
                }
            }

            override fun onFailure(call: Call<AuthToken?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onRestart() {
        binding.etUsername.requestFocus()
        binding.etUsername.text.clear()
        binding.etPassword.text.clear()
        super.onRestart()
    }
}
