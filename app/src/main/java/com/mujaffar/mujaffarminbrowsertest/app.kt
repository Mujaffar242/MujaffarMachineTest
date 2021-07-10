package com.mujaffar.mujaffarminbrowsertest

import android.app.Application
import com.mujaffar.mujaffarminbrowsertest.di.AppModule
import com.mujaffar.mujaffarminbrowsertest.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class app:Application() ,HasAndroidInjector{

    @Inject lateinit var mInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return mInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)
    }

}