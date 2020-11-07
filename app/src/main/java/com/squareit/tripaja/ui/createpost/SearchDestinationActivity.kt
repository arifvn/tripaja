package com.squareit.tripaja.ui.createpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareit.tripaja.R
import com.squareit.tripaja.utils.makeSnackbar
import kotlinx.android.synthetic.main.activity_search_destination.*

class SearchDestinationActivity : AppCompatActivity() {
    private var placeName: String = ""
    private var adapter: PlaceAutoCompleteAdapter? = null
    private var listResult = ArrayList<PlaceDataModel>()
    private lateinit var placesClient: PlacesClient
    private var latLng = LatLng(-33.8749937, 151.2041382)

    companion object {
        private const val TAG = "SearchDestinationActivi"
        const val LOCATION_REQUEST_CODE = 32
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_destination)

        initPlaceClient()
        initRecyclerView()
        initSearchFieldChanges()

        btnBack.setOnClickListener {
            val intentResult = Intent()
            intentResult.putExtra(CreatePostFragment.PLACE_RESULT, placeName)
            setResult(RESULT_OK, intentResult)
            finish()
        }
    }

    private fun initPlaceClient() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)
        getCurrentLocation(placesClient)
    }

    private fun getCurrentLocation(placesClient: PlacesClient) {
        val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestForPermissions()
        } else {
            initPlaceClient()
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
                        ?: emptyList()) {
                        latLng = placeLikelihood.place.latLng!!
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Tempat Sekarang Tidak Ditemukan: ${exception.statusCode}")
                    }
                }
            }
        }
    }

    private fun requestForPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish()
                } else {
                    return
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = PlaceAutoCompleteAdapter(this)
        rvListSearched.layoutManager = LinearLayoutManager(this@SearchDestinationActivity)
        rvListSearched.addItemDecoration(
            DividerItemDecoration(
                this@SearchDestinationActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        rvListSearched.adapter = adapter
    }

    private fun initSearchFieldChanges() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    initFilter(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initFilter(inputPlace: String) {
        val token = AutocompleteSessionToken.newInstance()
        val countryFilter = mutableListOf("ID")
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountries(countryFilter)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(inputPlace)
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    val distanceInKm = prediction.distanceMeters.toString()
                    listResult.add(
                        PlaceDataModel(
                            prediction.getPrimaryText(null).toString(),
                            prediction.getSecondaryText(null).toString(),
                            distanceInKm
                        )
                    )
                    adapter!!.updateList(listResult)
                }
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    layoutSearch.makeSnackbar("Destinasi tidak ditemukan")
                }
            }
    }
}