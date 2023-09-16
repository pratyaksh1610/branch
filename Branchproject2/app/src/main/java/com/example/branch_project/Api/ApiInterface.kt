package com.example.branchdemo.Api

import com.example.branch_project.Model.AllMessages
import com.example.branch_project.Model.AuthToken
import com.example.branch_project.Model.LoginCredentials

import com.example.branch_project.Model.SendMessage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    // to hit at end point

    @POST("/api/login")
    fun postLogin(@Body login: LoginCredentials): Call<AuthToken>

    @GET("/api/messages")
    fun getAllMessages(@Header("X-Branch-Auth-Token") header: String): Call<List<AllMessages>>

    @POST("/api/messages")
    fun sendMessage(
        @Header("X-Branch-Auth-Token") header: String,
        @Body sendMessage: SendMessage
    ): Call<AllMessages>
}
