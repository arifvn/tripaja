package com.squareit.tripaja.ui.profile.inviting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.item_post_inviting.view.*

class PostInvitingAdapter : RecyclerView.Adapter<PostInvitingAdapter.PostViewHolder>() {

    private var listPost: List<Post>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_inviting, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (listPost != null) {
            holder.bindItem(listPost!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (listPost != null) {
            listPost!!.size
        } else 0
    }

    class PostViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        fun bindItem(post: Post) {
            Glide.with(item.context)
                .load(ContextCompat.getDrawable(item.context, R.drawable.ic_user_placeholder))
                .centerCrop()
                .into(item.ivIcon)
        }
    }

    fun addPost(listPost: List<Post>) {
        this.listPost = listPost
        notifyDataSetChanged()
    }
}