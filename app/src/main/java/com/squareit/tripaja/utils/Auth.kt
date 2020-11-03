package com.squareit.tripaja.utils

import android.content.Context
import android.content.Intent
import com.squareit.tripaja.ui.MainActivity

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).also { intent ->
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }