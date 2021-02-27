package ru.fasdev.homeworkone.data.contacts

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import ru.fasdev.homeworkone.data.contacts.model.Contact
import ru.fasdev.homeworkone.feature.wrapUse

class ContactsRepoImpl(context: Context): ContactsRepo
{
    companion object {
        const val DISPLAY_NAME_COLUMN = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        const val LOOK_UP_COLUMN = ContactsContract.Contacts.LOOKUP_KEY

        const val PHONE_COLUMN = ContactsContract.CommonDataKinds.Phone.NUMBER
        const val EMAIL_COLUMN = ContactsContract.CommonDataKinds.Email.ADDRESS
    }

    private val contactsProjection = arrayOf(DISPLAY_NAME_COLUMN, LOOK_UP_COLUMN)

    private val contentResolver = context.contentResolver

    override fun getContacts(): List<Contact>
    {
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, contactsProjection,
            null, null, null)

        val arrayContacts: MutableList<Contact> = mutableListOf()

        cursor.wrapUse { cursor ->
            while (cursor.moveToNext()) {
                val displayNameIndex = cursor.getColumnIndex(DISPLAY_NAME_COLUMN)
                val lookUpIndex = cursor.getColumnIndex(LOOK_UP_COLUMN)

                val nameContact = cursor.getString(displayNameIndex)
                val lookUpContact = cursor.getString(lookUpIndex)

                arrayContacts.add(Contact(lookUpContact, nameContact,
                    getPhone(lookUpContact), getEmail(lookUpContact)))
            }
        }

        return arrayContacts
    }

    private fun getPhone(lookupKey: String): String {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(PHONE_COLUMN), "${ContactsContract.Data.LOOKUP_KEY} = ?",
            arrayOf(lookupKey), null)

        var phoneContact = ""

        cursor.wrapUse {
            while (it.moveToNext()) {
                val phoneIndex = it.getColumnIndex(PHONE_COLUMN)

                phoneContact = it.getStringOrNull(phoneIndex) ?: ""
            }
        }

        return phoneContact
    }

    private fun getEmail(lookupKey: String): String {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(EMAIL_COLUMN), "${ContactsContract.Data.LOOKUP_KEY} = ?",
            arrayOf(lookupKey), null)

        var emailContact = ""

        cursor.wrapUse {
            while (it.moveToNext()) {
                val emailIndex = it.getColumnIndex(EMAIL_COLUMN)

                emailContact = it.getStringOrNull(emailIndex) ?: ""
            }
        }

        return emailContact
    }
}