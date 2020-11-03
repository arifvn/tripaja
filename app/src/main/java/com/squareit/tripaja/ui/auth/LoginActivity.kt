package com.squareit.tripaja.ui.auth

import android.content.Intent
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
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var authViewModel: AuthViewModel

    override fun onStart() {
        super.onStart()
        authViewModel.authUser?.let {
            startMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViewModel()
        initializeButtonsAction()
    }

    private fun initializeViewModel() {
        authViewModel =
            ViewModelProvider(this@LoginActivity, factory).get(AuthViewModel::class.java)
        authViewModel.result.observe(this@LoginActivity, {
            when (it.status) {
                Status.LOADING -> {
                    progressBarLogin.visibility = View.VISIBLE
                    setViewsEnabled(false)
                }
                Status.SUCCESS -> {
                    progressBarLogin.visibility = View.GONE
                    startMainActivity()
                }
                Status.ERROR -> {
                    Snackbar.make(layoutLogin, it.message!!, Snackbar.LENGTH_SHORT).show()
                    progressBarLogin.visibility = View.GONE
                    setViewsEnabled(true)
                }
            }
        })
    }

    private fun initializeButtonsAction() {
        btnToRegister.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

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

            authViewModel.login(email, password)
        }

        btnForgotPassword.setOnClickListener {
            Intent(this@LoginActivity, ResetPasswordActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setViewsEnabled(isEnabled: Boolean) {
        btnLogin.isEnabled = isEnabled
        edtEmail.isEnabled = isEnabled
        edtPassword.isEnabled = isEnabled
        btnGoogleLogin.isEnabled = isEnabled
        btnToRegister.isEnabled = isEnabled
        btnForgotPassword.isEnabled = isEnabled
    }
}