package com.squareit.tripaja.data.model

data class User(
    val idUser: Int,
    val fullName: String,
    val userName: String,
    val follower: Int,
    val following: Int,
    val photo: String,
    val biography: String
)