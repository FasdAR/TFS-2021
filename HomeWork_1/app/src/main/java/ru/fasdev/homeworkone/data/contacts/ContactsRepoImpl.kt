package ru.fasdev.homeworkone.data.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import ru.fasdev.homeworkone.data.contacts.model.Contact
import ru.fasdev.homeworkone.feature.getValue
import ru.fasdev.homeworkone.feature.getValueOrNull
import ru.fasdev.homeworkone.feature.wrapUse
import java.util.*

class ContactsRepoImpl(context: Context): ContactsRepo
{
    companion object {
        const val DISPLAY_NAME_COLUMN = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        const val LOOK_UP_COLUMN = ContactsContract.Contacts.LOOKUP_KEY

        const val PHONE_COLUMN = ContactsContract.CommonDataKinds.Phone.NUMBER
        const val EMAIL_COLUMN = ContactsContract.CommonDataKinds.Email.ADDRESS
    }

    private val contentResolver = context.contentResolver

    override fun getContacts(): List<Contact>
    {
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                arrayOf(DISPLAY_NAME_COLUMN, LOOK_UP_COLUMN),
            null, null, null)

        val arrayContacts: MutableList<Contact> = mutableListOf()

        cursor.wrapUse { cursor ->
            while (cursor.moveToNext()) {
                val nameContact = cursor.getValue(DISPLAY_NAME_COLUMN)
                val lookUpContact = cursor.getValue(LOOK_UP_COLUMN)

                arrayContacts.add(Contact(lookUpContact, nameContact,
                    getPhone(lookUpContact), getEmail(lookUpContact)))
            }
        }

        return arrayContacts
    }

    private fun getPhone(lookupKey: String): String {
        val phone = queryOneValue(lookupKey,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONE_COLUMN)

        return PhoneNumberUtils.formatNumber(queryOneValue(lookupKey,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONE_COLUMN),
            Locale.getDefault().country) ?: phone
    }

    private fun getEmail(lookupKey: String): String =
            queryOneValue(lookupKey,
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, EMAIL_COLUMN)

    private fun queryOneValue(lookupKey: String, uri: Uri, projection: String): String {
        val cursor = contentResolver.query(uri, arrayOf(projection), "${ContactsContract.Data.LOOKUP_KEY} = ?",
        arrayOf(lookupKey), null)

        var value = ""

        cursor.wrapUse {
            while (it.moveToNext()) {
                value = it.getValueOrNull(projection) ?: ""
            }
        }

        return value
    }
}