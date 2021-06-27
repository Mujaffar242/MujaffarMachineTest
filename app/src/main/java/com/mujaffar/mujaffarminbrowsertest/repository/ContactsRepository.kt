package com.mujaffar.currencyconverter.repository

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.mujaffar.mujaffarminbrowsertest.database.ContactDatabase
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


/*
* data source for provide data to our ui layer
* */
class ContactsRepository(val database: ContactDatabase, val applicatin: Application) {
    suspend fun getContactList() {
        withContext(Dispatchers.IO) {
            //save list of contact with favorite and delete status in local database

            if((database.contactDao.getAllContacts().value== null))
            database.contactDao.insertAll(getContacts(applicatin))
        }
    }




    /*
    * get contact list from device when app lanch first time for save it on local databse
    * */
    fun getContacts(ctx: Context): List<DatabaseContactModel>? {
        val list: MutableList<DatabaseContactModel> = ArrayList()
        val contentResolver: ContentResolver = ctx.getContentResolver()
        val cursor: Cursor? =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor?.getCount()!! > 0) {
            while (cursor?.moveToNext()!!) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    val inputStream: InputStream? =
                        ContactsContract.Contacts.openContactPhotoInputStream(
                            ctx.getContentResolver(),
                            ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI,
                                id.toLong()
                            )
                        )
                    val person: Uri =
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )
                    var pURI: Uri = Uri.withAppendedPath(
                        person,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )
                    var photo: Bitmap;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream)
                    } else {
                        pURI = Uri.parse("");
                    }
                    while (cursorInfo?.moveToNext()!!) {
                        val contactName: String =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val contactNumber: String =
                            cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))


                        val info = DatabaseContactModel(
                            id,
                            contactName,
                            contactNumber,
                            pURI.toString(),
                            false,
                            false
                        )
                        list.add(info)
                    }
                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        return list
    }


    /*
    * get contact name with number and photos from room databse
    *  */
    val contacts: LiveData<List<DatabaseContactModel?>?>? = database.contactDao.getDeletedOrNonDeletedList(false)


    /*
    * get list of deleted contact
    * */
        val deletedContacts: LiveData<List<DatabaseContactModel?>?>? = database.contactDao.getDeletedOrNonDeletedList(true)


    /*
    * get list of favorite contacts
    * */
    val favoriteContacts:LiveData<List<DatabaseContactModel?>?>? = database.contactDao.getfavoriteContactList(true)


            /*
            * update contact
            * */
   suspend fun updateContact(contactModel: DatabaseContactModel) {
        withContext(Dispatchers.IO){
            database.contactDao.update(contactModel)
        }
    }

    /*
   * delete contact
   * */
    suspend fun deleteContact(contactModel: DatabaseContactModel) {
        withContext(Dispatchers.IO){
            database.contactDao.delete(contactModel)
        }
    }



}