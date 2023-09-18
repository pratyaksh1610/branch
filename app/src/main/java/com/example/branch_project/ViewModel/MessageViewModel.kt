package com.example.branch_project.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.Model.AuthToken
import com.example.branch_project.Model.LoginCredentials
import com.example.branch_project.Repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()
    private val authToken: MutableLiveData<String> = MutableLiveData()
    private val listAllMessages: MutableLiveData<List<AllMessages>?> = MutableLiveData()

    fun postLogin(login: LoginCredentials) {
        repository.postLogin(login).enqueue(object : Callback<AuthToken?> {
            override fun onResponse(call: Call<AuthToken?>, response: Response<AuthToken?>) {
                if (response.isSuccessful && response.body() != null) {
                    authToken.postValue(response.body()!!.auth_token)
                } else {
                    authToken.postValue("Invalid")
                }
            }

            override fun onFailure(call: Call<AuthToken?>, t: Throwable) {
            }
        })
    }

    fun getAuthToken(): LiveData<String> {
        return authToken
    }

    fun getAllMessages(authToken: String) {
        repository.getAllMessages(authToken).enqueue(object : Callback<List<AllMessages>?> {
            override fun onResponse(
                call: Call<List<AllMessages>?>,
                response: Response<List<AllMessages>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    listAllMessages.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<AllMessages>?>, t: Throwable) {
            }
        })
    }

    fun getListOfAllMessages(): MutableLiveData<List<AllMessages>?> {
        return listAllMessages
    }
}
