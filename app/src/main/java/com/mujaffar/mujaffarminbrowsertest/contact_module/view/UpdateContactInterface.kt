package com.mujaffar.mujaffarminbrowsertest.contact_module.view

import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel

/*
* interface for handle update contact events on contact list activity
* */
interface UpdateContactInterface {
    fun  updateContact(contactModel: DatabaseContactModel)
}