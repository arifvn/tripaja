package com.squareit.tripaja.ui.createpost

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareit.tripaja.R
import com.squareit.tripaja.utils.makeSnackbar
import kotlinx.android.synthetic.main.activity_search_destination.*

class SearchDestinationActivity : AppCompatActivity() {
    private var placeAdapter: PlaceAutoCompleteAdapter? = null
    private val placesClient: PlacesClient by lazy {
        Places.createClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_destination)

        initRecyclerView()
        initSearchFieldChanges()

        btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun initRecyclerView() {
        placeAdapter = PlaceAutoCompleteAdapter { placeName ->
            val intentResult = Intent()
            intentResult.putExtra(CreatePostFragment.INTENT_SEARCH_RESULT, placeName)
            setResult(RESULT_OK, intentResult)
            finish()
        }

        rvListSearched.apply {
            layoutManager = LinearLayoutManager(this@SearchDestinationActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchDestinationActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            adapter = placeAdapter
        }

        val placeListInitial = arrayListOf<PlaceDataModel>()
        placeAdapter!!.differ.submitList(placeListInitial)
    }

    private fun initSearchFieldChanges() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                showNotFound(false)

                if (s.toString() != "") {
                    initFilter(s.toString())
                }
            }
        })
    }

    private fun initFilter(inputPlace: String) {
        progressBarSearch.visibility = View.VISIBLE

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }

        val token = AutocompleteSessionToken.newInstance()
        val countryFilter = mutableListOf("ID")
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountries(countryFilter)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(inputPlace)
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val result = arrayListOf<PlaceDataModel>()
                for (prediction in response.autocompletePredictions) {
                    result.add(
                        PlaceDataModel(
                            prediction.getPrimaryText(null).toString(),
                            prediction.getSecondaryText(null).toString(),
                        )
                    )
                }
                if (result.size == 0) {
                    showNotFound(true)
                }
                placeAdapter!!.submitList(result)
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    layoutSearch.makeSnackbar("Terjadi Kesalahan")
                }
            }.addOnCompleteListener {
                progressBarSearch.visibility = View.GONE
            }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    private fun showNotFound(isVisible: Boolean) {
        if (isVisible) {
            imgNotFound.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
        } else {
            imgNotFound.visibility = View.GONE
            tvNotFound.visibility = View.GONE
        }
    }
}