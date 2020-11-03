package com.squareit.tripaja

import android.app.Application
import com.squareit.tripaja.data.firebase.FirebaseSource
import com.squareit.tripaja.data.repository.UserRepository
import com.squareit.tripaja.ui.auth.viewmodel.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))
        bind<FirebaseSource>() with singleton { FirebaseSource() }
        bind<UserRepository>() with singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
    }
}