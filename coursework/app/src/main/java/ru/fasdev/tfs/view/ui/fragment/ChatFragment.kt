package ru.fasdev.tfs.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.recycler.Adapter
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.ui.fragment.adapter.ChatHolderFactory

class ChatFragment: Fragment(R.layout.fragment_chat)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = Adapter<ViewTyped>(ChatHolderFactory())
    }
}