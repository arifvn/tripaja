package com.squareit.tripaja.ui.profile.inviting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.fragment_inviting.*

class InvitingFragment : Fragment() {

    private lateinit var postInvitingAdapter: PostInvitingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inviting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostInvitingAdapter()
    }

    private fun setPostInvitingAdapter() {
        val listPost = listOf<Post>(
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
            Post("", ""),
        )

        postInvitingAdapter = PostInvitingAdapter()
        postInvitingAdapter.addPost(listPost)
        rvPostInviting.setHasFixedSize(true)

        rvPostInviting.adapter = postInvitingAdapter
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPostInviting.layoutManager = linearLayoutManager
        rvPostInviting.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}