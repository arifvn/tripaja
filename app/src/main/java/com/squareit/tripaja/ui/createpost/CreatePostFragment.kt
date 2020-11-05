package com.squareit.tripaja.ui.createpost

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.squareit.tripaja.R
import com.squareit.tripaja.utils.makeSnackbar
import com.synnapps.carouselview.ImageListener
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kotlinx.android.synthetic.main.fragment_create_post.*

class CreatePostFragment : Fragment() {
    private var selectedUriCarousel: List<Uri>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlaceForMaps()

        btnAddImage.setOnClickListener {
            initImagePicker()
        }
    }

    private fun initPlaceForMaps() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }
        Places.createClient(requireContext())

        searchFragment.visibility = View.VISIBLE

        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        val countryFilter = mutableListOf("ID")
        autocompleteFragment
            .setCountries(countryFilter)
            .setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                searchFragment.visibility = View.GONE
                btnSearchDestination.text = place.name
            }

            override fun onError(status: Status) {
                searchFragment.visibility = View.GONE
                layoutCreatePost.makeSnackbar("Terjadi kesalahan : $status")
            }
        })
    }

    private fun initImagePicker() {
        TedImagePicker.with(requireContext())
            .mediaType(MediaType.IMAGE)
            .title("Pilih Gambar")
            .buttonBackground(R.color.colorPrimary)
            .buttonText("Selesai")
            .selectedUri(selectedUriCarousel)
            .drawerAlbum()
            .startMultiImage { uriList ->
                if (uriList.isNotEmpty()) {
                    setImagePostCarousel(uriList.size, uriList)
                    selectedUriCarousel = uriList
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun setImagePostCarousel(size: Int, uriList: List<Uri>) {
        val requestOptions = RequestOptions()
        requestOptions.transform(CenterCrop(), RoundedCorners(10))
        val imageListener = ImageListener { position, imageView ->
            Glide.with(requireContext())
                .load(uriList[position])
                .centerCrop()
                .apply(requestOptions)
                .into(imageView)
        }
        imgPostCarousel.setImageListener(imageListener)
        imgPostCarousel.pageCount = size
    }
}