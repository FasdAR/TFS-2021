package ru.fasdev.homeworkone.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fasdev.homeworkone.R
import ru.fasdev.homeworkone.data.contacts.model.Contact
import ru.fasdev.homeworkone.databinding.ActivityFirstBinding
import ru.fasdev.homeworkone.ui.adapter.contact.ContactsRvAdapter

class FirstActivity : AppCompatActivity() {
    companion object {
        const val KEY_CONTACTS = "contacts"
    }

    private lateinit var binding: ActivityFirstBinding

    private val startResultSecondActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val intent = it.data
                    val array = intent?.getParcelableArrayListExtra<Contact>(SecondActivity.KEY_CONTACTS)

                    array?.let { contactsList = it }

                    checkState()
                }
            }
        }

    private val adapter: ContactsRvAdapter = ContactsRvAdapter()

    private var contactsList: List<Contact> = listOf()
        set(value) {
            field = value
            adapter.update(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getParcelableArrayList<Contact>(KEY_CONTACTS)?.let {
            contactsList = it
        }

        if (contactsList.isEmpty()) toLoadContacts()

        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarLayout.toolbar.apply {
            title = resources.getString(R.string.contacts)
        }

        val layoutManager = LinearLayoutManager(this)

        binding.listContacts.layoutManager = layoutManager
        binding.listContacts.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            this,
            layoutManager.orientation
        )

        binding.listContacts.addItemDecoration(dividerItemDecoration)

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            toLoadContacts()
        }
    }

    override fun onResume() {
        super.onResume()

        checkState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putParcelableArrayList(KEY_CONTACTS, ArrayList(contactsList))
        }
        super.onSaveInstanceState(outState)
    }

    private fun toLoadContacts() {
        startResultSecondActivity.launch(Intent(this, SecondActivity::class.java))
    }

    private fun checkState() {
        if (contactsList.isEmpty()) toEmptyState()
        else toBusyState()
    }

    // #region Ui State
    private fun toEmptyState() {
        binding.emptyContacts.isVisible = true
        binding.listContacts.isVisible = false
    }

    private fun toBusyState() {
        binding.emptyContacts.isVisible = false
        binding.listContacts.isVisible = true
    }
    // #endregion
}
