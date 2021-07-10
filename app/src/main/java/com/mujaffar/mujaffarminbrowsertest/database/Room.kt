/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mujaffar.mujaffarminbrowsertest.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/*
* room query for communicate with sqlite database
* */
@Dao
interface ContactDao {

    //for get all contacts
    @Query("select * from DatabaseContactModel")
    fun getAllContacts(): LiveData<List<DatabaseContactModel>>


    //for insert all contact list to room databse
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<DatabaseContactModel>?)


    /*
    *for update database model single row
    * use for update favorite and delete status
     */
    @Update
    fun update(contact: DatabaseContactModel)


    //for permanent delete this row from database
    @Delete
    fun delete(model: DatabaseContactModel)


    //for get deleted list
    @Transaction
    @Query("SELECT * FROM DatabaseContactModel WHERE isDeleted=:isDeleted")
    fun getDeletedOrNonDeletedList(isDeleted: Boolean?): LiveData<List<DatabaseContactModel?>?>?

    //for get favorite list
    @Transaction
    @Query("SELECT * FROM DatabaseContactModel WHERE isFavorite=:isFavorite")
    fun getfavoriteContactList(isFavorite: Boolean?): LiveData<List<DatabaseContactModel?>?>?

}


/*
* for get database object
* */
@Database(entities = [DatabaseContactModel::class], version = 1,exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}

private lateinit var INSTANCE: ContactDatabase

fun getDatabase(context: Context): ContactDatabase {
    synchronized(ContactDatabase::class.java)
    {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ContactDatabase::class.java,
                "videos"
            ).build()
        }
    }

    return INSTANCE
}