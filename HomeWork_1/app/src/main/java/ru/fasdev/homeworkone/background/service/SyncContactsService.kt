@file:Suppress("DEPRECATION")

package ru.fasdev.homeworkone.background.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.fasdev.homeworkone.data.contacts.ContactsRepo
import ru.fasdev.homeworkone.data.contacts.ContactsRepoImpl
import ru.fasdev.homeworkone.ui.activity.SecondActivity

class SyncContactsService(name: String =  SyncContactsService::class.java.simpleName) :
    IntentService(name) {
    companion object {
        const val EMULATE_TIMEOUT = 1000L
    }

    private lateinit var contactsRepo: ContactsRepo

    override fun onHandleIntent(intent: Intent?) {
        contactsRepo = ContactsRepoImpl(applicationContext)
        val contacts = ArrayList(contactsRepo.getContacts())

        // Симуляция долгой загрузки
        Thread.sleep(EMULATE_TIMEOUT)

        val resultIntent = Intent(SecondActivity.BROADCAST_ACTION_CONTACTS)
        resultIntent.putParcelableArrayListExtra(SecondActivity.BROADCAST_KEY_CONTACTS, contacts)

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
    }
}
