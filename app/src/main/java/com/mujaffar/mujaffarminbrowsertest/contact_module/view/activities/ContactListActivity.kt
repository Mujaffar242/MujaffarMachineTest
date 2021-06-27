package com.mujaffar.mujaffarminbrowsertest.contact_module.view.activities

import android.app.ProgressDialog
import android.content.Context
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


class ContactListActivity : AppCompatActivity(), UpdateContactInterface {

    //for hold data binding reference
    lateinit var binding: ActivityContactListBinding;


    /**
     * RecyclerView Adapter for converting a list of currency to grid.
     */
    private var viewModelAdapter: ContactListAdapter? = null

    //for hold currency viewmodel
    private lateinit var viewModel: ContactViewModel

    //variable for hold list type
    lateinit var pageType: String


    //variable for hold progress dialog
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
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
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)


        //init viewmodel
        viewModelAdapter = ContactListAdapter(this, pageType)

        //set adapter to recylerview
        binding.contactListRecyclerView.adapter = viewModelAdapter


        //observe contact live data based on different page type and add to adapter
        //if page type is contactlist
        if (pageType.equals(Utility.contactList)) {
            viewModel.contactList?.observe(this, Observer { contact ->
                contact?.apply {
                    viewModelAdapter?.contacts = contact as List<DatabaseContactModel>

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
                    viewModelAdapter?.contacts = contact as List<DatabaseContactModel>
                    hideShowNODataBasedOnList(contact)
                }
            })

        }
        //if page type is deleted list
        else {

            viewModel.deletedContacts?.observe(this, Observer { contact ->
                contact?.apply {
                    viewModelAdapter?.contacts = contact as List<DatabaseContactModel>
                    hideShowNODataBasedOnList(contact)
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


    /*
    * for update clicked item of contact list and take action based of that
    * like delete , favorite, restore events
    * */
    override fun updateContact(contactModel: DatabaseContactModel) {
        viewModel.updateContact(contactModel)
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


}