package com.mujaffar.mujaffarminbrowsertest.di

import com.mujaffar.mujaffarminbrowsertest.app
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AppModule::class, ContactActivityModule::class
    ]
)
interface AppComponent {
    fun inject(app: app)
}