package ru.fasdev.tfs.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.recycler.Adapter
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.feature.recycler.itemDecoration.VerticalSpaceItemDecoration
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.fragment.adapter.ChatHolderFactory
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.DateUi
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.ExternalMessageUi
import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.InternalMessageUi

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

        adapter.items = listOf(
                DateUi(0, "31 Dec"),
                ExternalMessageUi(0, "Andrey Rednikov",
                        R.drawable.ic_launcher_background.toString(),
                        "Hello My First Message",
                        listOf(
                                MessageReactionUi("\uD83D\uDE30", 1, false),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true)
                        )),
                InternalMessageUi(0, "Hello My Internal Message",
                        listOf(MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true),
                                MessageReactionUi("\uD83E\uDD22", 5, true)))
        ).reversed()
    }
}