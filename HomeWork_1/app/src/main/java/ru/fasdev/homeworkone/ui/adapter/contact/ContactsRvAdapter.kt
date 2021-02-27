package ru.fasdev.homeworkone.ui.adapter.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.homeworkone.R
import ru.fasdev.homeworkone.data.contacts.model.Contact

class ContactsRvAdapter : RecyclerView.Adapter<ContactsViewHolder>() {
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
