package com.example.apiapp01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapp01.Model.Post
import com.example.apiapp01.Model.PostRequest
import com.example.apiapp01.adapter.PostAdapter
import com.example.apiapp01.network.RetroFitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()
    private val localPosts = mutableMapOf<Int, Post>()  // Guarda los posts creados localmente
    private lateinit var progressBar: ProgressBar
    private lateinit var idInput: EditText
    private lateinit var titleInput: EditText
    private lateinit var completedInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        idInput = findViewById(R.id.idInput)
        titleInput = findViewById(R.id.titleInput)
        completedInput = findViewById(R.id.completedInput)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(posts)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.refreshButton).setOnClickListener {
            val id = idInput.text.toString().toIntOrNull()
            if (id != null) {
                fetchPostById(id)
            } else {
                fetchPosts()
            }
        }

        findViewById<Button>(R.id.createPostButton).setOnClickListener {
            createPost()
        }

        fetchPosts()
    }

    private fun fetchPosts() {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetroFitClient.apiService.getTodos()
                posts.clear()
                posts.addAll(response)
                posts.addAll(localPosts.values)  // Agregar los posts locales

                Log.d("MainActivity", "Datos recibidos: $response")

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun fetchPostById(id: Int) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val post = localPosts[id] ?: RetroFitClient.apiService.getTodoById(id)
                posts.clear()
                post?.let { posts.add(it) }

                Log.d("MainActivity", "Post recibido: $post")

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                    if (post == null) {
                        Toast.makeText(this@MainActivity, "No se encontró el post con ID: $id", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun createPost() {
        val title = titleInput.text.toString()
        val completed = completedInput.text.toString().toBoolean()

        if (title.isBlank()) {
            Toast.makeText(this, "Por favor, ingrese un título", Toast.LENGTH_SHORT).show()
            return
        }

        val newPost = PostRequest(userId = 1, title = title, completed = completed)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetroFitClient.apiService.createPost(newPost)

                // Guardar el nuevo post localmente
                val localId = localPosts.size + 201  // Generar un ID local
                val localPost = Post(userId = 1, id = localId, title = response.title, completed = response.completed)
                localPosts[localId] = localPost

                withContext(Dispatchers.Main) {
                    posts.add(0, localPost)  // Agregar al inicio de la lista
                    adapter.notifyItemInserted(0)
                    recyclerView.scrollToPosition(0)  // Desplazar al inicio
                    Toast.makeText(
                        this@MainActivity,
                        "Post creado exitosamente con ID: $localId",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Limpiar los campos de entrada
                    titleInput.text.clear()
                    completedInput.text.clear()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
