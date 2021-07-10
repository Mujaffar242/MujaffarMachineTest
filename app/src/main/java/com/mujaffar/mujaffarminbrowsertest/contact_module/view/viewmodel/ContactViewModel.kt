package com.mujaffar.mujaffarminbrowsertest.contact_module.view.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Index
import com.mujaffar.currencyconverter.repository.ContactsRepository
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val contactsRepository: ContactsRepository) : ViewModel()
{

    //corotiune job
    private var currentJob: Job? = null



    //for hold currency list return by repository
    var  contactList=contactsRepository.contacts


    //for hold list of deleted contact
    var deletedContacts=contactsRepository.deletedContacts

    //for hold favorite contact list
    var favoriteContacts=contactsRepository.favoriteContacts


    //for show loading spinner
    var showLoadingProgressBar=MutableLiveData<Boolean>()

    /*
    * get list of contacts and save into room databse when app load first time
    * */
    init {
        viewModelScope.launch {
            contactsRepository.getContactList()
        }

        showLoadingProgressBar.value=false
    }


    /*
    * funcation for update single contact row on room database
    * */
    fun changeDeleteStatus(contactModel: DatabaseContactModel)
    {

        viewModelScope.launch {
            contactsRepository.updateDeleteStatus(contactModel)
        }
    }



    fun changeFavoriteStatus(contactModel: DatabaseContactModel)
    {

        viewModelScope.launch {
            contactsRepository.updateFavoriteStatus(contactModel)
        }
    }

    /*
    * for show loading spinner
    * */
    fun showLoadingSpinner()
    {
        showLoadingProgressBar.value=true;
    }


    /*
   * for hide loading spinner
   * */
    fun hideLoadingSpinner()
    {
        showLoadingProgressBar.value=false;
    }

}