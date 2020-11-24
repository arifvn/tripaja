package com.squareit.tripaja.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.squareit.tripaja.data.repository.AuthFirebase
import com.squareit.tripaja.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(private val authFirebase: AuthFirebase) : ViewModel() {

    private val disposables = CompositeDisposable()
    private var _result = MutableLiveData<Resource<String>>()
    val result: LiveData<Resource<String>> = _result

    val authUser by lazy {
        authFirebase.currentUser()
    }

    fun login(email: String, password: String) {
        _result.value = Resource.loading(null)

        val disposable = authFirebase.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _result.value = Resource.success(null)
            }, {
                _result.value = Resource.error(it.message!!, null)
            })
        disposables.add(disposable)
    }

    fun loginWithGoogle(credential: AuthCredential) {
        _result.value = Resource.loading(null)

        val disposable = authFirebase.loginWithGoogle(credential)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _result.value = Resource.success(null)
            }, {
                _result.value = Resource.error(it.message!!, null)
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}