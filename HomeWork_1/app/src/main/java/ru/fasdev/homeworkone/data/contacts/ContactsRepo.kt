package ru.fasdev.homeworkone.data.contacts

import ru.fasdev.homeworkone.data.contacts.model.Contact

interface ContactsRepo
{
    fun getContacts(): List<Contact>
}