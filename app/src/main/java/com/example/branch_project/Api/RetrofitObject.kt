package com.example.branch_project.Api

import com.example.branchdemo.Api.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val baseUrl = "https://android-messaging.branch.co/"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInterface = retrofit.create(ApiInterface::class.java)
}
