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
import androidx.recyclerview.widget.RecyclerView
import com.mujaffar.mujaffarminbrowsertest.R
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.UpdateContactInterface
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import com.mujaffar.mujaffarminbrowsertest.databinding.ContactItemBinding
import com.mujaffar.mujaffarminbrowsertest.util.Utility


/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class ContactListAdapter(
    private val updateContactInterface: UpdateContactInterface,
    private val pageType: String
) : RecyclerView.Adapter<ContactViewHolder>() {

    /**
     * The contact list that our Adapter will show
     */
    var contacts: List<DatabaseContactModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val withDataBinding: ContactItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ContactViewHolder.LAYOUT,
            parent,
            false
        )
        return ContactViewHolder(withDataBinding)
    }

    override fun getItemCount() = contacts.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.contact = contacts[position]
        }


        /*
        * if page type is not equal deleted list then on click of each row prompted call screen
        * */
        if (!pageType.equals(Utility.deletedList)) {
            holder.itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + contacts[position].contactNumber)
                holder.itemView.context.startActivity(intent)
            }
        }


        /*
        * by click on favorite icon contact added to favorite list or remove from contact list if already on favorite
        * */
        holder.viewDataBinding.addToFavorite.setOnClickListener {

            //invert the favorite status and add to database
            contacts[position].isFavorite = !contacts[position].isFavorite
            updateContactInterface.updateContact(contacts[position])
        }


        /*
        * by click on delete icon contact deleted from all contact list and added to deleted list
        * before deletion confirm from user by showing alerat dialouge
        * */
        holder.viewDataBinding.deleteContact.setOnClickListener {
            showAlertDialog(holder.itemView.context, contacts[position])
        }


        /*
        * for restoring deleted contact from deleted list
        * */
        holder.viewDataBinding.restoreContact.setOnClickListener {
            //set the flag is deleted flase
            contacts[position].isDeleted = false
            updateContactInterface.updateContact(contacts[position])
        }

        //in case of page type favorite list  hide delete and restore button
        if (pageType.equals(Utility.favoriteList)) {
            holder.viewDataBinding.deleteContact.visibility = View.GONE
            holder.viewDataBinding.addToFavorite.visibility = View.VISIBLE
            holder.viewDataBinding.restoreContact.visibility = View.GONE

        }
        //in case of page type delete list hide both favorite and delete button
        else if (pageType.equals(Utility.deletedList)) {
            holder.viewDataBinding.deleteContact.visibility = View.GONE
            holder.viewDataBinding.addToFavorite.visibility = View.GONE
            holder.viewDataBinding.restoreContact.visibility = View.VISIBLE

        }
        //in case of page type contact list hide restore button
        else {
            holder.viewDataBinding.restoreContact.visibility = View.GONE
            holder.viewDataBinding.addToFavorite.visibility = View.VISIBLE
            holder.viewDataBinding.deleteContact.visibility = View.VISIBLE

        }


    }


    /*
    * show alert dialog for confirm deletion
    * */
    private fun showAlertDialog(context: Context, contactModel: DatabaseContactModel) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Delete Contact")
        //set message for alert dialog
        builder.setMessage("Do you want to delete this contact")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            contactModel.isDeleted = true
            contactModel.isFavorite = false
            updateContactInterface.updateContact(contactModel)
            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}


/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class ContactViewHolder(val viewDataBinding: ContactItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.contact_item
    }
}

