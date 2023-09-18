package com.example.branch_project.Repository

import com.example.branch_project.Api.RetrofitObject
import com.example.branch_project.Model.AllMessages
import com.example.branch_project.Model.AuthToken
import com.example.branch_project.Model.LoginCredentials
import retrofit2.Call

class Repository {

    fun postLogin(login: LoginCredentials): Call<AuthToken> {
        return RetrofitObject.apiInterface.postLogin(login)
    }

    fun getAllMessages(authToken: String): Call<List<AllMessages>> {
        return RetrofitObject.apiInterface.getAllMessages(authToken)
    }
}
