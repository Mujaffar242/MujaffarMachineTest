package com.mujaffar.mujaffarminbrowsertest.di

import android.content.Context
import androidx.room.Room
import com.mujaffar.currencyconverter.repository.ContactsRepository
import com.mujaffar.mujaffarminbrowsertest.database.ContactDao
import com.mujaffar.mujaffarminbrowsertest.database.ContactDatabase
import com.mujaffar.mujaffarminbrowsertest.database.getDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val context: Context) {


    @Singleton
    @Provides
    fun provideContactDataBase():ContactDatabase
    {
        return  getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideContactDao(database: ContactDatabase):ContactDao
    {
      return  database.contactDao
    }

    @Singleton
    @Provides
    fun provideContactRepository (database: ContactDatabase,contactDao: ContactDao):ContactsRepository
    {
        return ContactsRepository(database,context,contactDao)
    }







}