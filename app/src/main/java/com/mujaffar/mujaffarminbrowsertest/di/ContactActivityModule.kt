package com.mujaffar.mujaffarminbrowsertest.di

import com.mujaffar.mujaffarminbrowsertest.contact_module.view.activities.ContactListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ContactActivityModule {


    @ContributesAndroidInjector
    abstract fun contriButeContactActivityInjector():ContactListActivity



}