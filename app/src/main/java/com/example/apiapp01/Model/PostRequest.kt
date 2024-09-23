package com.example.apiapp01.Model

data class PostRequest(
    val userId: Int,
    val title: String,
    val completed: Boolean
)
