package com.uka.findmyphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_my_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MyTrackers : AppCompatActivity() {

    var contacts = ArrayList<UserContact>()

    var adapter: MyContactsAdapter? = null

    var userData:UserData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)

        userData = UserData(this)

        adapter = MyContactsAdapter(this, contacts)

        mListTrackers.adapter = adapter

        mListTrackers.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val userInfo = contacts.get(position)
            UserData.myTrackers.remove(userInfo.phoneNumber)
            refreshData()

            userData!!.saveData()
        }

        userData!!.loadData()
        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tracker_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.finishActivity -> {
                finish()
            }

            R.id.addContact -> {
                checkPermission()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val REQUEST_PERMISSION = 123
    val REQUEST_CONTACT = 124

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
                return
            }
        }

        pickContact()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CONTACT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val contactData = data!!.data
                    val c = contentResolver.query(contactData, null, null, null, null)

                    if (c.moveToFirst()) {
                        val id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val has_phone = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if (has_phone.equals("1")) {
                            val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null)

                            phones.moveToFirst()
                            val phoneNumber = phones.getString(phones.getColumnIndex("data1"))
                            val name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                            UserData.myTrackers.put(phoneNumber, name)

                            refreshData()

                            userData!!.saveData()
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun refreshData() {
        contacts.clear()

        for ((key, value) in UserData.myTrackers) {
            contacts.add(UserContact(value, key))
        }

        adapter!!.notifyDataSetChanged()
    }

    inner class MyContactsAdapter : BaseAdapter {

        var context: Context? = null

        var layoutInflater: LayoutInflater? = null

        var contacts = ArrayList<UserContact>()

        constructor(context: Context, contacts: ArrayList<UserContact>) {
            this.context = context
            this.contacts = contacts

            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var contact = contacts[position]
            var myView = layoutInflater!!.inflate(R.layout.contact_ticket, null)

            myView.mTextName.text = contact.name
            myView.mTextPhone.text = contact.phoneNumber

            return myView
        }

        override fun getItem(position: Int): Any {
            return contacts[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return contacts.size
        }
    }
}
