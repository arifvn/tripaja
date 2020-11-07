package com.squareit.tripaja.ui.createpost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareit.tripaja.R
import kotlinx.android.synthetic.main.item_place_autocomplete.view.*

class PlaceAutoCompleteAdapter(
    private val context: Context,
) : RecyclerView.Adapter<PlaceAutoCompleteAdapter.PlaceViewHolder>() {

    private var mResultList: ArrayList<PlaceDataModel>? = null

    fun updateList(newList: ArrayList<PlaceDataModel>) {
        mResultList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): PlaceViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_place_autocomplete, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        if (mResultList != null) {
            holder.bindItem(mResultList!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (mResultList != null) {
            mResultList!!.size
        } else 0
    }

    class PlaceViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(placeDataModel: PlaceDataModel) {
            view.tvPlace.text = placeDataModel.placeName
            view.tvPlaceAddress.text = placeDataModel.placeDetail
            view.tvPlaceDistance.text = placeDataModel.placeDistance
        }
    }
}
