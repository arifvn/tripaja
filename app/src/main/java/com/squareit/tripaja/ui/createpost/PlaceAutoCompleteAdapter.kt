package com.squareit.tripaja.ui.createpost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareit.tripaja.R
import kotlinx.android.synthetic.main.item_place_autocomplete.view.*

class PlaceAutoCompleteAdapter(private val clickListener: (String) -> Unit) :
    RecyclerView.Adapter<PlaceAutoCompleteAdapter.PlacesViewHolder>() {

    inner class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<PlaceDataModel>() {
        override fun areItemsTheSame(oldItem: PlaceDataModel, newItem: PlaceDataModel): Boolean =
            oldItem.placeName == newItem.placeName

        override fun areContentsTheSame(oldItem: PlaceDataModel, newItem: PlaceDataModel): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun submitList(data: MutableList<PlaceDataModel>) {
        differ.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder =
        PlacesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place_autocomplete, parent, false)
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val placeDataModel = differ.currentList[position]

        holder.itemView.apply {
            tvPlace.text = placeDataModel.placeName
            tvPlaceAddress.text = placeDataModel.placeDetail
            setOnClickListener { clickListener(placeDataModel.placeName) }
        }
    }
}

