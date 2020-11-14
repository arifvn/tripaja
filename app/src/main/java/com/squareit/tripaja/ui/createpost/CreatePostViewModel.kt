package com.squareit.tripaja.ui.createpost

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {
    private var _listUri = MutableLiveData<List<Uri>>()
    val listUri: LiveData<List<Uri>> = _listUri

    private var _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    fun setListUri(newList: List<Uri>) {
        _listUri.value = newList
    }

    fun setDestination(newDestination: String) {
        _destination.value = newDestination
    }

    fun setDescription(newDescription: String) {
        _description.value = newDescription
    }
}