package com.squareit.tripaja.ui.createpost

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareit.tripaja.R
import com.squareit.tripaja.utils.makeSnackbar
import com.synnapps.carouselview.ImageListener
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kotlinx.android.synthetic.main.fragment_create_post.*

class CreatePostFragment : Fragment() {
    private var selectedUriCarousel = listOf<Uri>()
    private val viewModel: CreatePostViewModel by navGraphViewModels(R.id.main_nav_graph)

    companion object {
        private const val SEARCH_RESULT_CODE = 1
        const val INTENT_SEARCH_RESULT = "place_result"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    private fun initViewModel() {
        with(viewModel) {
            listUri.observe(viewLifecycleOwner, {
                setImagePostCarousel(it.size, it)
                selectedUriCarousel = it
            })
            destination.observe(viewLifecycleOwner, {
                btnSearchDestination.text = it
            })
            description.observe(viewLifecycleOwner, {
                edtDescription.setText(it)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddImage.setOnClickListener {
            initImagePicker()
        }

        btnSearchDestination.setOnClickListener {
            Intent(requireContext(), SearchDestinationActivity::class.java).also {
                startActivityForResult(it, SEARCH_RESULT_CODE)
            }
        }
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
                    viewModel.setListUri(uriList)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SEARCH_RESULT_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    data?.let {
                        val place = data.getStringExtra(INTENT_SEARCH_RESULT)
                        if (place != null) {
                            viewModel.setDestination(place)
                        }
                    }
                }
                RESULT_CANCELED -> {
                    layoutCreatePost.makeSnackbar("Tidak ada destinasi terpilih")
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        viewModel.setDescription(edtDescription.text.toString())
        super.onStop()
    }
}