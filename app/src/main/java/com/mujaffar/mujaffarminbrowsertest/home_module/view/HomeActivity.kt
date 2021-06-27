package com.mujaffar.mujaffarminbrowsertest.home_module.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mujaffar.mujaffarminbrowsertest.R
import com.mujaffar.mujaffarminbrowsertest.contact_module.view.activities.ContactListActivity
import com.mujaffar.mujaffarminbrowsertest.databinding.ActivityHomeBinding
import com.mujaffar.mujaffarminbrowsertest.home_module.viewmodel.HomeViewModel
import com.mujaffar.mujaffarminbrowsertest.util.Utility


class HomeActivity : AppCompatActivity() {

    //for hold data binding reference
    lateinit var binding: ActivityHomeBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ask contact read permission
        askContactPermissioon()


        //hide the action bar
        supportActionBar?.hide()


        //init binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        //init homeview model
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        //set viewmodel to bining
        binding.homeViewModel = homeViewModel


        /*
        * observer contact list button click
        * */
        homeViewModel.navigateToContactList.observe(this, Observer {
            if (it) {
                //if permisson is not granted then ask for permssion again
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                    ) !==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    askContactPermissioon()
                }
                //otherwise redirect to contact list page
                else {
                    val intent = Intent(this, ContactListActivity::class.java)
                    intent.putExtra(Utility.KEY_PAGE_TYPE, Utility.contactList)
                    startActivity(intent)
                    homeViewModel.onContactListNavigated()
                }


            }
        })


        /*
       * observer favorite list button click
       * */
        homeViewModel.navigateToContactFavorite.observe(this, Observer {
            if (it) {
                //if permisson is not granted then ask for permssion again
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                    ) !==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    askContactPermissioon()
                }
                //othereise reidrect to favorite contact list page
                else {
                    val intent = Intent(this, ContactListActivity::class.java)
                    intent.putExtra(Utility.KEY_PAGE_TYPE, Utility.favoriteList)
                    startActivity(intent)
                    homeViewModel.onFavoriteListNavigated()
                }
            }
        })


        /*
        * observe delete conact list button click
        * */
        homeViewModel.navigateToContactDeleted.observe(this, Observer {
            if (it) {
                //if permisson is not granted then ask for permssion again
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                    ) !==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    askContactPermissioon()
                }
                //otherwise redirect to favorite contact list
                else {
                    val intent = Intent(this, ContactListActivity::class.java)
                    intent.putExtra(Utility.KEY_PAGE_TYPE, Utility.deletedList)
                    startActivity(intent)
                    homeViewModel.onDeletedListNavigated()
                }
            }
        })


    }

    /*
    * funcion for ask contact permission
    **/
    fun askContactPermissioon() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS), 1
                )
            }
        }
    }


    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_CONTACTS
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                    }
                } else {
                }
                return
            }
        }
    }


}