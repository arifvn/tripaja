package com.squareit.tripaja.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.squareit.tripaja.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btnResetPassword.setOnClickListener {
            val email = edtEmailSendTo.text.toString().trim()
            if (email.isEmpty()) {
                edtEmail.error = "Email can not be blank"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Email is not valid"
                edtEmail.requestFocus()
                return@setOnClickListener
            }
        }
    }
}