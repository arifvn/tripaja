package com.squareit.tripaja.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareit.tripaja.data.repository.AuthFirebase

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val authFirebase: AuthFirebase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authFirebase) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}