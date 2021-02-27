package ru.fasdev.homeworkone.data.contacts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val id: String, val displayName: String, val phone: String, val email: String):
    Parcelable