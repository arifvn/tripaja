package com.squareit.tripaja.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.squareit.tripaja.R
import com.squareit.tripaja.databinding.ActivityLoginBinding
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModel
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModelFactory
import com.squareit.tripaja.utils.Status
import com.squareit.tripaja.utils.makeSnackbar
import com.squareit.tripaja.utils.startMainActivity
import com.squareit.viewbinder.viewBinder
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

    private val viewBinding: ActivityLoginBinding by viewBinder()

    override fun onStart() {
        super.onStart()
        authViewModel.authUser?.let {
            startMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initBtnClickListener()
    }

    private fun initViewModel() {
        authViewModel =
            ViewModelProvider(this@LoginActivity, factory).get(AuthViewModel::class.java)
        authViewModel.result.observe(this@LoginActivity) {
            when (it.status) {
                Status.LOADING -> {
                    viewBinding.progressBarRegister.visibility = View.VISIBLE
                    setViewsEnabled(false)
                }
                Status.SUCCESS -> {
                    viewBinding.progressBarRegister.visibility = View.GONE
                    startMainActivity()
                }
                Status.ERROR -> {
                    viewBinding.root.makeSnackbar(it.message!!)
                    viewBinding.progressBarRegister.visibility = View.GONE
                    setViewsEnabled(true)
                }
            }
        }
    }

    private fun setViewsEnabled(isEnabled: Boolean) {
        with(viewBinding) {
            edtEmail.isEnabled = isEnabled
            edtPassword.isEnabled = isEnabled
            btnForgotPassword.isEnabled = isEnabled
            btnLogin.isEnabled = isEnabled
            btnRegisterGoogle.isEnabled = isEnabled
            btnToRegister.isEnabled = isEnabled
        }
    }

    private fun initBtnClickListener() {
        with(viewBinding) {
            btnLogin.setOnClickListener {
                val email = edtEmail.editText?.text.toString().trim()
                val password = edtPassword.editText?.text.toString().trim()

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

                loginWithEmail(email, password)
            }

            btnRegisterGoogle.setOnClickListener {
                loginWithGoogleAccount()
            }
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        authViewModel.login(email, password)
    }

    private fun loginWithGoogleAccount() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

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
                Log.d("TAG", "GAGAL: $e, ${e.message}")
                viewBinding.root.makeSnackbar("Login Gagal")
            }
        }
    }
}
