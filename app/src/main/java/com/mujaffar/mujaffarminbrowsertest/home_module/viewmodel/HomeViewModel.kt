package com.mujaffar.mujaffarminbrowsertest.home_module.viewmodel

import androidx.lifecycle.*


class HomeViewModel() : ViewModel() {

    //for naviagte to contact list page
    private val _navigateToContactList = MutableLiveData<Boolean>()
    val navigateToContactList
        get() = _navigateToContactList

    //for navigate to favorite list page
    private val _navigateToContactFavorite = MutableLiveData<Boolean>()
    val navigateToContactFavorite
        get() = _navigateToContactFavorite


    //for navigate to conact list page
    private val _navigateToContactDeleted = MutableLiveData<Boolean>()
    val navigateToContactDeleted
        get() = _navigateToContactDeleted


    init {
        //for initial set all live data to false

        _navigateToContactList.value=false
        _navigateToContactFavorite.value=false
        navigateToContactDeleted.value=false
    }


    /*
    * call when conact button cliked
    * */
    fun onContactButtonClicked(){
        navigateToContactList.value = true
    }


    /*
    * call when favorite button click
    * */
    fun onFavoriteButtonClicked(){
        navigateToContactFavorite.value = true
    }


    /*
    * call when delete button click
    * */
    fun onDeleteButtonClick(){
        navigateToContactDeleted.value = true
    }


    /*
    * call after navigate to contact list for reset
    * */
    fun onContactListNavigated() {
        navigateToContactList.value = false
    }


    /*
    * call after navigate to favorite list for reset
    * */
    fun onFavoriteListNavigated() {
        navigateToContactFavorite.value = false
    }


    /*
    * call after navigate to delete list for reset
    * */
    fun onDeletedListNavigated() {
        navigateToContactDeleted.value = false
    }

}