package com.squareit.tripaja.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareit.tripaja.data.repository.UserRepository
import com.squareit.tripaja.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _result = MutableLiveData<Resource<String>>()
    val result: LiveData<Resource<String>> = _result

    val authUser by lazy {
        repository.currentUser()
    }

    fun login(email: String, password: String) {
        _result.value = Resource.loading(null)

        val disposable = repository.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _result.value = Resource.success(null)
            }, {
                _result.value = Resource.error(it.message!!, null)
            })
        disposables.add(disposable)
    }

    fun register(email: String, password: String) {
        _result.value = Resource.loading(null)

        val disposable = repository.register(email, password)
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