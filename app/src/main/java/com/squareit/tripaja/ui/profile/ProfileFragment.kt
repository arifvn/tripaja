package com.squareit.tripaja.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.CategoryTrip
import com.squareit.tripaja.data.model.FavouriteTrip
import com.squareit.tripaja.data.model.ServiceTrip
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_top.*


class ProfileFragment : Fragment() {

    private lateinit var favouriteListAdapter: FavouriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFavouriteTripAdapter()
        setTripInterest()
        setTripServices()
        setPagerAdapter()
    }

    private fun setFavouriteTripAdapter() {
        val listFavourite = listOf<FavouriteTrip>(
            FavouriteTrip(null, "Bali"),
            FavouriteTrip(null, "Dieng"),
        )

        favouriteListAdapter = FavouriteListAdapter()
        favouriteListAdapter.addFavouriteTrip(listFavourite)
        rvPostFavourite.setHasFixedSize(true)
        rvPostFavourite.adapter = favouriteListAdapter
        rvPostFavourite.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setTripInterest() {
        val listCategoryTrip = listOf<CategoryTrip>(
            CategoryTrip(R.drawable.ic_mountain, "Mountain"),
            CategoryTrip(R.drawable.ic_city, "City"),
            CategoryTrip(R.drawable.ic_culture, "Culture"),
            CategoryTrip(R.drawable.ic_food, "Culinary"),
        )

        for (category in listCategoryTrip) {
            val chip = Chip(activity)
            with(chip) {
                chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccentSoft)
                chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
                setTextAppearance(R.style.TextAppearance_ChipTextStyleAppearance)
                chipIcon = ContextCompat.getDrawable(requireContext(), category.icon)
                chipIconSize = 36f
                textEndPadding = 15f
                chipStartPadding = 15f
                setEnsureMinTouchTargetSize(false)
                text = category.name
                cgCategoryInterest.addView(this)
            }
        }
    }

    private fun setTripServices() {
        val listServiceTrip = listOf<ServiceTrip>(
            ServiceTrip(R.drawable.ic_tools, "Tools"),
            ServiceTrip(R.drawable.ic_repair, "Repairing"),
            ServiceTrip(R.drawable.ic_taxi, "Vehicle"),
            ServiceTrip(R.drawable.ic_health, "Medicine")
        )

        for (service in listServiceTrip) {
            val chip = Chip(activity)
            with(chip) {
                chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccentSoft)
                chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
                setTextAppearance(R.style.TextAppearance_ChipTextStyleAppearance)
                chipIcon = ContextCompat.getDrawable(requireContext(), service.icon)
                chipIconSize = 36f
                textEndPadding = 15f
                chipStartPadding = 15f
                setEnsureMinTouchTargetSize(false)
                text = service.name
                cgTripServices.addView(this)
            }
        }
    }

    private fun setPagerAdapter() {
        val postProfilePagerAdapter = PostProfilePagerAdapter(this)
        view_pager.adapter = postProfilePagerAdapter
        TabLayoutMediator(tabs, view_pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Trips"
                1 -> tab.text = "Inviting"
                else -> tab.text = "Discuss"
            }
        }.attach()
    }
}