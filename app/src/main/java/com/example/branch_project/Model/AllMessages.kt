package com.example.branch_project.Model

data class AllMessages(
    val id: Int,
    val thread_id: Int,
    val user_id: String,
    val agent_id: String?,
    val body: String,
    val timestamp: String
)

data class SendMessage(
    var thread_id: Int,
    var body: String
)
