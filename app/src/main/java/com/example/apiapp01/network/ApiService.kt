package com.example.apiapp01.network
import com.example.apiapp01.Model.Post
import com.example.apiapp01.Model.PostRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<Post>

    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Post

    @POST("todos")
    suspend fun createPost(@Body post: PostRequest): Post
}