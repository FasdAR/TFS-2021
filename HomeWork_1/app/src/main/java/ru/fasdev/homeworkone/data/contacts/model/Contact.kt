package ru.fasdev.homeworkone.data.contacts.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Contact(val id: String, val displayName: String, val phone: String, val email: String) :
    Parcelable
