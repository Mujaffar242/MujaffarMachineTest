package com.mujaffar.mujaffarminbrowsertest.database


import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey



/*
* for save contact name with number,image and favorite and delete status
* */
@Entity
data class DatabaseContactModel(
        @PrimaryKey
        val id:String,
        val contactName: String,
        val contactNumber: String,
        val photo: String,
        var isFavorite:Boolean,
        var isDeleted:Boolean
        )


