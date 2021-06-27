package com.mujaffar.mujaffarminbrowsertest.contact_module.view.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mujaffar.currencyconverter.repository.ContactsRepository
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import com.mujaffar.mujaffarminbrowsertest.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.IOException

class ContactViewModel(application: Application) : AndroidViewModel(application)
 {

     //corotiune job
    private var currentJob: Job? = null

     //get local database
    private val database=getDatabase(application)

     //data source currency repository
    private val contactsRepository = ContactsRepository(database,application)


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
     fun updateContact(contactModel: DatabaseContactModel)
     {

         viewModelScope.launch {
             contactsRepository.updateContact(contactModel)
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