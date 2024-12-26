package com.hear_word_and_type.app

import android.app.Application
import com.hear_word_and_type.app.data.preferences.PreferenceProvider
import com.hear_word_and_type.app.ui.custom_views.circle_progress.CustomProgress
import com.hear_word_and_type.app.util.FieldsValidator
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import org.kodein.di.generic.instance

class MainApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override val kodein = Kodein.lazy {

        import(androidXModule(this@MainApplication))

        bind() from singleton { FieldsValidator() }
        bind() from singleton { CustomProgress() }
        bind() from singleton { PreferenceProvider(instance()) }
    }
}