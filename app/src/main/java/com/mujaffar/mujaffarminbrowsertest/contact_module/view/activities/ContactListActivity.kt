package com.mujaffar.mujaffarminbrowsertest.contact_module.view.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mujaffar.mujaffarminbrowsertest.R
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.UpdateContactInterface
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.adapter.ContactListAdapter
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.viewmodel.ContactViewModel
import com.mujaffar.mujaffarminbrowsertest.database.DatabaseContactModel
import com.mujaffar.mujaffarminbrowsertest.databinding.ActivityContactListBinding
import com.mujaffar.mujaffarminbrowsertest.util.Utility
import dagger.android.AndroidInjection
import javax.inject.Inject


class ContactListActivity : AppCompatActivity(), UpdateContactInterface {

    //for hold data binding reference
    lateinit var binding: ActivityContactListBinding;


    /**
     * RecyclerView Adapter for converting a list of currency to grid.
     */
    private var viewModelAdapter: ContactListAdapter? = null

    //for hold currency viewmodel
    @Inject
    lateinit var viewModel: ContactViewModel

    //variable for hold list type
    lateinit var pageType: String


    //variable for hold progress dialog
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        //get page type from intent previous page
        pageType = intent.getStringExtra(Utility.KEY_PAGE_TYPE)!!


        //for show back button on actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setTitle(pageType)

        //init progress dialog
        progressDialog = ProgressDialog(this)


        //init binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_list)




        //init viewmodel
        viewModelAdapter = ContactListAdapter(this, pageType)

        //set adapter to recylerview
        binding.contactListRecyclerView.adapter = viewModelAdapter


        //observe contact live data based on different page type and add to adapter
        //if page type is contactlist
        if (pageType.equals(Utility.contactList)) {
            viewModel.contactList?.observe(this, Observer { contact ->
                contact?.apply {
                    //viewModelAdapter?.contacts = contact as List<DatabaseContactModel>

                    viewModelAdapter?.submitList(contact as List<DatabaseContactModel>)

                    //if contact list is empty mean fetching contacts show loading spinner
                    if (contact.isEmpty())
                        viewModel.showLoadingSpinner()
                    //otherwise hide the loading spinner
                    else
                        viewModel.hideLoadingSpinner()

                }
            })

        }
        //if page type is favorite list
        else if (pageType.equals(Utility.favoriteList)) {

            viewModel.favoriteContacts?.observe(this, Observer { contact ->
                contact?.apply {
                   // viewModelAdapter?.contacts = contact as List<DatabaseContactModel>
                    viewModelAdapter?.submitList(contact as List<DatabaseContactModel>)
                    hideShowNODataBasedOnList(contact as List<DatabaseContactModel>)
                }
            })

        }
        //if page type is deleted list
        else {

            viewModel.deletedContacts?.observe(this, Observer { contact ->
                contact?.apply {
                    viewModelAdapter?.submitList(contact as List<DatabaseContactModel>)
                    hideShowNODataBasedOnList(contact as List<DatabaseContactModel>)
                }
            })

        }


        /*
        * observer the showLoadingProgressBar and based on this show loading spinner
        * */
        viewModel.showLoadingProgressBar.observe(this, Observer {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

    }


    /*
    * funcation for show no data text centre of screen if list is empaty
    * */
    private fun hideShowNODataBasedOnList(contact: List<DatabaseContactModel>) {
        if (contact.isEmpty()) {
            binding.noDataText.visibility = View.VISIBLE
        } else {
            binding.noDataText.visibility = View.GONE
        }
    }





    // function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun changeFavoriteStatus(contactModel: DatabaseContactModel) {
        //invert the favorite status
        viewModel.changeFavoriteStatus(contactModel)
    }


    override fun chnageDeleteStatus(contactModel: DatabaseContactModel) {
        if(!contactModel.isDeleted)
        {
            showAlertDialog(contactModel)
            return;
        }
        viewModel.changeDeleteStatus(contactModel)
    }

    override fun onContactClick(contactModel: DatabaseContactModel) {
        /*
      * if page type is not equal deleted list then on click of each row prompted call screen
      * */
        if (!pageType.equals(Utility.deletedList)) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + contactModel.contactNumber)
                startActivity(intent)

        }
    }


    /*
   * show alert dialog for confirm deletion
   * */
    private fun showAlertDialog(
        contactModel: DatabaseContactModel,
    ) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Contact")
        //set message for alert dialog
        builder.setMessage("Do you want to delete this contact")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            viewModel.changeDeleteStatus(contactModel)
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