package com.example.apiapp01.Model

data class Post(
    val userId: Int,
    val id: Int? = null,
    val title: String,
    val completed: Boolean
)
