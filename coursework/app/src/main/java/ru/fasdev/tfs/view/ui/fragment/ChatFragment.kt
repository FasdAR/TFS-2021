package ru.fasdev.tfs.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.mapper.mapToUiList
import ru.fasdev.tfs.view.feature.recycler.Adapter
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.feature.recycler.itemDecoration.VerticalSpaceItemDecoration
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.fragment.adapter.ChatHolderFactory
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.DateUi
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.ExternalMessageUi
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.InternalMessageUi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment: Fragment(R.layout.fragment_chat)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val holderFactory = ChatHolderFactory()
        val adapter = Adapter<ViewTyped>(holderFactory)

        val rvList: RecyclerView = view.findViewById(R.id.rv_list)
        rvList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        rvList.adapter = adapter
        rvList.addItemDecoration(VerticalSpaceItemDecoration(19.toDp))

        adapter.items = getGenerateListMsg().mapToUiList(1)
    }

    private fun getGenerateListMsg(): List<Message> =
        listOf(
            Message(1,
                User(1, "", "Test Test1"),
                "Hello Chat!", Date(1584278973),
                listOf(
                    Reaction("\uD83E\uDD24", 10, false)
                )
            ),
            Message(2,
                User(2, "", "Test Test2"),
                "Hello Test2", Date(),
                listOf(
                    Reaction("\uD83E\uDD74", 5, true)
                )
            ),
            Message(3,
                User(1, "", "Test Test2"),
                "Hello Test1", Date(),
                listOf()
            )
        )
}