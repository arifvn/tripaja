package com.squareit.tripaja.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareit.tripaja.R
import com.squareit.tripaja.data.model.FavouriteTrip
import kotlinx.android.synthetic.main.item_favourite_trip.view.*

class FavouriteListAdapter : RecyclerView.Adapter<FavouriteListAdapter.FavouriteTripViewHolder>() {

    private var listFavouriteTrip: List<FavouriteTrip>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteTripViewHolder {
        return FavouriteTripViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_trip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouriteTripViewHolder, position: Int) {
        if (listFavouriteTrip != null) {
            val isLastIndex = listFavouriteTrip!!.lastIndex == position
            holder.bindItem(listFavouriteTrip!![position], isLastIndex)
        }
    }

    override fun getItemCount(): Int {
        return if (listFavouriteTrip != null) {
            listFavouriteTrip!!.size
        } else 0
    }

    class FavouriteTripViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        fun bindItem(favouriteTrip: FavouriteTrip, isLastIndex: Boolean) {
            if (isLastIndex) {
                val params = item.layoutParams as RecyclerView.LayoutParams
                params.marginEnd = 0
                item.layoutParams = params
            }

            with(item) {
                tvCapsFavourite.text = favouriteTrip.caption
                Glide.with(this.context)
                    .load(ContextCompat.getDrawable(item.context, R.drawable.img_favourite))
                    .centerCrop()
                    .into(ivFavourite)
            }
        }
    }

    fun addFavouriteTrip(listFavouriteTrip: List<FavouriteTrip>) {
        this.listFavouriteTrip = listFavouriteTrip
        notifyDataSetChanged()
    }
}