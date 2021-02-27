package ru.fasdev.homeworkone.ui.adapter.contact

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.homeworkone.R
import ru.fasdev.homeworkone.data.contacts.model.Contact

class ContactsViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    private val displayNameText: TextView = view.findViewById(R.id.display_name)
    private val phoneText: TextView = view.findViewById(R.id.phone_text)
    private val emailText: TextView = view.findViewById(R.id.email_text)

    private val phoneLayout: ViewGroup = view.findViewById(R.id.phone)
    private val emailLayout: ViewGroup = view.findViewById(R.id.email)

    fun bind(contact: Contact) {
        displayNameText.text = contact.displayName

        stateInfoLayout(contact.phone, phoneText, phoneLayout)
        stateInfoLayout(contact.email, emailText, emailLayout)
    }

    private fun stateInfoLayout(text: String, textView: TextView, layout: ViewGroup) {
        if (text.isNotEmpty()) {
            textView.text = text
            layout.isVisible = true
        }
        else {
            layout.isVisible = false
        }
    }
}