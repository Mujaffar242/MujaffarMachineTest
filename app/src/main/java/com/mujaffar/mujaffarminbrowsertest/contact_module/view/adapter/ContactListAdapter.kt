package com.mujaffar.mujaffarminbrowsertest.contact_module.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mujaffar.mujaffarminbrowsertest.R
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.UpdateContactInterface
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import com.mujaffar.mujaffarminbrowsertest.databinding.ContactItemBinding
import com.mujaffar.mujaffarminbrowsertest.util.Utility


/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */

class ContactListAdapter (private val updateContactInterface: UpdateContactInterface,
                          private val pageType: String): ListAdapter<DatabaseContactModel, ContactViewHolder>(ContactDiffCallback())
{

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.from(parent)
    }



    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.contact = getItem(position)
            it.updateContactListner = updateContactInterface
        }

        holder.bind(pageType)


    }


}


/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class ContactViewHolder private constructor(val viewDataBinding: ContactItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.contact_item

        public fun from(parent: ViewGroup): ContactViewHolder {
            val withDataBinding: ContactItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                LAYOUT,
                parent,
                false
            )
            return ContactViewHolder(withDataBinding)
        }


    }


    public fun bind(
        pageType: String
    ) {
        //in case of page type favorite list  hide delete and restore button
        if (pageType.equals(Utility.favoriteList)) {
            viewDataBinding.deleteContact.visibility = View.GONE
            viewDataBinding.addToFavorite.visibility = View.VISIBLE
            viewDataBinding.restoreContact.visibility = View.GONE

        }
        //in case of page type delete list hide both favorite and delete button
        else if (pageType.equals(Utility.deletedList)) {
            viewDataBinding.deleteContact.visibility = View.GONE
            viewDataBinding.addToFavorite.visibility = View.GONE
            viewDataBinding.restoreContact.visibility = View.VISIBLE

        }
        //in case of page type contact list hide restore button
        else {
            viewDataBinding.restoreContact.visibility = View.GONE
            viewDataBinding.addToFavorite.visibility = View.VISIBLE
            viewDataBinding.deleteContact.visibility = View.VISIBLE

        }
    }

}


class ContactDiffCallback :
    DiffUtil.ItemCallback<DatabaseContactModel>() {
    override fun areItemsTheSame(
        oldItem: DatabaseContactModel,
        newItem: DatabaseContactModel
    ): Boolean {
       return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DatabaseContactModel,
        newItem: DatabaseContactModel
    ): Boolean {
        return oldItem==newItem;
    }
}
