package com.squareit.tripaja.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareit.tripaja.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initBtnClickListener()
    }

    private fun initBtnClickListener() {
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnToLogin.setOnClickListener {
            onBackPressed()
        }
    }
}