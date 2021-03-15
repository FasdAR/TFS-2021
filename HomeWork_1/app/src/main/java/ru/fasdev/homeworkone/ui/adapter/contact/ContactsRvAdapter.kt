package ru.fasdev.homeworkone.ui.adapter.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.homeworkone.R
import ru.fasdev.homeworkone.data.contacts.model.Contact
import ru.fasdev.homeworkone.databinding.ItemContactBinding

class ContactsRvAdapter : RecyclerView.Adapter<ContactsRvAdapter.ContactsViewHolder>() {
    class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemContactBinding.bind(view)

        fun bind(contact: Contact) {
            binding.displayName.text = contact.displayName

            stateInfoLayout(contact.phone, binding.phone)
            stateInfoLayout(contact.email, binding.email)
        }

        private fun stateInfoLayout(text: String, textView: TextView) {
            if (text.isNotEmpty()) {
                textView.text = text
                textView.isVisible = true
            } else {
                textView.isVisible = false
            }
        }
    }


    private val localData: MutableList<Contact> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contact, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(localData[position])
    }

    override fun getItemCount(): Int = localData.size

    fun update(data: List<Contact>) {
        val diffUtil = ContactsDiffUtil(localData, data)
        val diffResult = DiffUtil.calculateDiff(diffUtil)

        localData.clear()
        localData.addAll(data)

        diffResult.dispatchUpdatesTo(this)
    }
}
