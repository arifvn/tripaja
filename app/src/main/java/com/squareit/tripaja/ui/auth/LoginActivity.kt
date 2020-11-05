package com.squareit.tripaja.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
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

    companion object {
        const val RC_SIGN_IN = 1
    }

    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var gso: GoogleSignInOptions

    override fun onStart() {
        super.onStart()
        authViewModel.authUser?.let {
            startMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeGoogleSignIn()
        initializeViewModel()
        initializeButtonsAction()
    }

    private fun initializeGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
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

    private fun setViewsEnabled(isEnabled: Boolean) {
        edtEmail.isEnabled = isEnabled
        edtPassword.isEnabled = isEnabled
        btnLogin.isEnabled = isEnabled
        btnLoginGoogle.isEnabled = isEnabled
    }

    private fun initializeButtonsAction() {
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty()) {
                edtEmail.error = "Email tidak boleh kosong"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Email tidak valid"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                edtPassword.error = "Password minimal 6 karakter"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            signInWithEmail(email, password)
        }

        btnLoginGoogle.setOnClickListener {
            signInWithGoogleAccount()
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        authViewModel.login(email, password)
    }

    private fun signInWithGoogleAccount() {
        googleSignInClient!!.signInIntent.also { intent ->
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                authViewModel.loginWithGoogle(credential)
            } catch (e: ApiException) {
                Snackbar.make(layoutLogin, "Login Gagal", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}