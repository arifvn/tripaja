package com.squareit.tripaja.ui.profile.discuss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.fragment_discuss.*

class DiscussFragment : Fragment() {

    private lateinit var postDiscussAdapter: PostDiscussAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discuss, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostDiscussAdapter()
    }

    private fun setPostDiscussAdapter() {
        val listPost = listOf<Post>(
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", "")
        )

        postDiscussAdapter = PostDiscussAdapter()
        postDiscussAdapter.addPost(listPost)
        rvPostDiscuss.setHasFixedSize(true)

        rvPostDiscuss.adapter = postDiscussAdapter
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPostDiscuss.layoutManager = linearLayoutManager
        rvPostDiscuss.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}