package com.squareit.tripaja.di

import android.app.Application
import com.squareit.tripaja.data.repository.AuthFirebase
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModel
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))
        bind<AuthFirebase>() with singleton { AuthFirebase() }
        bind() from singleton { AuthViewModelFactory(instance()) }
    }
}