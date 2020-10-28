package com.squareit.tripaja.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.squareit.tripaja.ui.profile.discuss.DiscussFragment
import com.squareit.tripaja.ui.profile.inviting.InvitingFragment
import com.squareit.tripaja.ui.profile.trips.TripPostFragment

class PostProfilePagerAdapter(hostFragment: ProfileFragment) :
    FragmentStateAdapter(hostFragment) {

    private val pages = listOf(
        TripPostFragment(),
        InvitingFragment(),
        DiscussFragment()
    )

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> pages[0]
            1 -> pages[1]
            else -> pages[2]
        }
    }
}