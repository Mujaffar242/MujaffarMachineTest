package com.mujaffar.mujaffarminbrowsertest

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel



/*
* binding adapter for set image on imageview
* */
@BindingAdapter("setContactImage")
fun ImageView.setImageOrShowFirstLetter(data:DatabaseContactModel)
{
    //if no profile image with contact then set first letter of contact as a profile image
    if(data.photo==null||data.photo.equals(""))
    {
       var drawable: TextDrawable = TextDrawable.builder()
            .buildRect(data.contactName.get(0).toString(), Color.RED);
        setImageDrawable(drawable);
    }
    //otherwise set profile image by uri
    else{
        setImageURI(Uri.parse(data.photo))
    }
}


/*
* binding adapter for set favorite status
* */
@BindingAdapter("setFavoriteImage")
fun ImageView.setImageFavorite(data:DatabaseContactModel)
{
    if(data.isFavorite)
    {
      setImageResource(R.drawable.ic_filled_favorite)
    }
    else{
        setImageResource(R.drawable.ic_border_favorite)

    }
}




