@file:Suppress("DEPRECATION")

package ru.fasdev.homeworkone.background.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.fasdev.homeworkone.background.broadcast.SyncContactsBroadcastReceiver
import ru.fasdev.homeworkone.data.contacts.ContactsRepo
import ru.fasdev.homeworkone.data.contacts.ContactsRepoImpl

class SyncContactsService(name: String = "SyncContactsService") :
    IntentService(name) {
    private lateinit var contactsRepo: ContactsRepo

    override fun onHandleIntent(intent: Intent?) {
        contactsRepo = ContactsRepoImpl(applicationContext)
        val contacts = ArrayList(contactsRepo.getContacts())

        // Симуляция долгой загрузки
        Thread.sleep(1000L)

        val resultIntent = Intent(SyncContactsBroadcastReceiver.ACTION_CONTACTS)
        resultIntent.putParcelableArrayListExtra(SyncContactsBroadcastReceiver.KEY_CONTACTS, contacts)

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
    }
}
