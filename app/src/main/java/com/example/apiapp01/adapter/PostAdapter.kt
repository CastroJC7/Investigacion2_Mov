package com.example.apiapp01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapp01.Model.Post
import com.example.apiapp01.R

// Adaptador para mostrar una lista de objetos Post en un RecyclerView

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.postId)
        val title: TextView = view.findViewById(R.id.postTitle)
        val completed: TextView = view.findViewById(R.id.postCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.id.text = "ID: ${post.id}"
        holder.title.text = post.title
        holder.completed.text = if (post.completed) "Completado" else "No completado"
    }

    override fun getItemCount() = posts.size
}