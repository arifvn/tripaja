package com.squareit.tripaja.ui.profile.trips

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.fragment_inviting.*
import kotlinx.android.synthetic.main.fragment_trip_post.*

class TripPostFragment : Fragment() {

    private lateinit var postListAdapter: PostTripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostProfileAdapter()
    }

    private fun setPostProfileAdapter() {
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

        postListAdapter = PostTripsAdapter()
        postListAdapter.addPost(listPost)
        rvPostTrip.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val spanCount = 3
                val spacing = 12
                val position = parent.getChildAdapterPosition(view)

                if (position >= 0) {
                    val column = position % spanCount
                    outRect.left = column * spacing / spanCount
                    outRect.right = spacing - (column + 1) * spacing / spanCount
                    if (position >= spanCount) {
                        outRect.top = spacing / 2
                    }
                } else {
                    outRect.left = 0
                    outRect.right = 0
                    outRect.top = 0
                    outRect.bottom = 0
                }
            }
        })
        rvPostTrip.setHasFixedSize(true)
        rvPostTrip.adapter = postListAdapter
        rvPostTrip.layoutManager = GridLayoutManager(activity, 3)
    }
}