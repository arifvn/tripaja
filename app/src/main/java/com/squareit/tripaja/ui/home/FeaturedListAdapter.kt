package com.squareit.tripaja.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.item_post_popular.view.*

class FeaturedListAdapter : RecyclerView.Adapter<FeaturedListAdapter.PostViewHolder>() {

    private var listPost: List<Post>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_featured, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (listPost != null) {
            val isLastIndex = listPost!!.lastIndex == position
            holder.bindItem(listPost!![position], isLastIndex)
        }
    }

    override fun getItemCount(): Int {
        return if (listPost != null) {
            listPost!!.size
        } else 0
    }

    class PostViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        fun bindItem(post: Post, isLastIndex: Boolean) {
            with(item) {
                tvLocation.text = post.location
                tvTotalPost.text = post.totalPost
                Glide.with(this.context)
                    .load("https://picsum.photos/200/300/?blur")
                    .centerCrop()
                    .into(ivPostPhoto)
                ivGradient.visibility = View.VISIBLE
            }
        }
    }

    fun addPost(listPost: List<Post>) {
        this.listPost = listPost
        notifyDataSetChanged()
    }
}