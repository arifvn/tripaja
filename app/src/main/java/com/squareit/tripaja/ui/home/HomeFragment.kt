package com.squareit.tripaja.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.Post
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var popularListAdapter: PopularListAdapter
    private lateinit var featuredListAdapter: FeaturedListAdapter
    private lateinit var listPost: List<Post>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListPost()
        setPopularRecyclerView()
        setFeaturedRecyclerView()
    }

    private fun initListPost() {
        listPost = listOf<Post>(
            Post("Bali", "1552 Post"),
            Post("Lombok", "12 Post"),
            Post("Jepara", "34 Post"),
            Post("Yogyakarta", "22 Post")
        )
    }

    private fun setPopularRecyclerView() {
        popularListAdapter = PopularListAdapter()
        popularListAdapter.addPost(listPost)
        rvPostPopular.setHasFixedSize(true)
        rvPostPopular.adapter = popularListAdapter
        rvPostPopular.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvPostPopular.smoothScrollToPosition(0)
    }

    private fun setFeaturedRecyclerView() {
        featuredListAdapter = FeaturedListAdapter()
        featuredListAdapter.addPost(listPost)
        rvPostFeatured.setHasFixedSize(true)
        rvPostFeatured.adapter = featuredListAdapter
        rvPostFeatured.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPostFeatured.smoothScrollToPosition(0)
    }
}