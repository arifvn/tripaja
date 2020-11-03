package com.squareit.tripaja.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareit.tripaja.R
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModel
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModelFactory
import com.squareit.tripaja.utils.Status
import com.squareit.tripaja.utils.startMainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edtEmail
import kotlinx.android.synthetic.main.activity_register.edtPassword
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RegisterActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViewModel()
        initializeButtonsAction()
    }

    private fun initializeViewModel() {
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        authViewModel.result.observe(this@RegisterActivity, {
            when (it.status) {
                Status.LOADING -> {
                    progressBarRegister.visibility = View.VISIBLE
                    setViewsEnabled(false)
                }
                Status.SUCCESS -> {
                    progressBarRegister.visibility = View.GONE
                    startMainActivity()
                }
                Status.ERROR -> {
                    Snackbar.make(layoutLogin, it.message!!, Snackbar.LENGTH_SHORT).show()
                    progressBarRegister.visibility = View.GONE
                    setViewsEnabled(true)
                }
            }
        })
    }

    private fun initializeButtonsAction() {
        btnToLogin.setOnClickListener {
            onBackPressed()
        }

        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

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

            if (password.isEmpty() || password.length < 6) {
                edtPassword.error = "Password must at least 6 character"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                edtConfirmPassword.error = "Password not the same"
                edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            authViewModel.register(email, password)
        }
    }

    private fun setViewsEnabled(isEnabled: Boolean) {
        btnRegister.isEnabled = isEnabled
        edtEmail.isEnabled = isEnabled
        edtPassword.isEnabled = isEnabled
        edtConfirmPassword.isEnabled = isEnabled
        btnToLogin.isEnabled = isEnabled
        btnGoogleRegister.isEnabled = isEnabled
    }
}