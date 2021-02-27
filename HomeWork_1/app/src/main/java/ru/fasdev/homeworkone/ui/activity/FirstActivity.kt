package ru.fasdev.homeworkone.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fasdev.homeworkone.R
import ru.fasdev.homeworkone.data.contacts.model.Contact
import ru.fasdev.homeworkone.databinding.ActivityFirstBinding
import ru.fasdev.homeworkone.ui.adapter.contact.ContactsRvAdapter

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    private val startResultSecondActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    val intent = it.data
                    val array = intent?.getParcelableArrayListExtra<Contact>(SecondActivity.KEY_CONTACTS)

                    array?.let {
                        adapter.update(it)
                    }
                }
            }
    }

    private val adapter: ContactsRvAdapter = ContactsRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            startResultSecondActivity.launch(Intent(this, SecondActivity::class.java))
        }

        binding.toolbarLayout.toolbar.apply {
            title = resources.getString(R.string.contacts)
        }

        val layoutManager = LinearLayoutManager(this)

        binding.listContacts.layoutManager = layoutManager
        binding.listContacts.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this,
            layoutManager.orientation)

        binding.listContacts.addItemDecoration(dividerItemDecoration)
    }
}