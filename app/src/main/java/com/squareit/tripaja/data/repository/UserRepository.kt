package com.squareit.tripaja.data.repository

import com.google.firebase.auth.AuthCredential
import com.squareit.tripaja.data.firebase.FirebaseService

class UserRepository(private val firebase: FirebaseService) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun loginWithGoogle(credential: AuthCredential) = firebase.loginWithGoogle(credential)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}