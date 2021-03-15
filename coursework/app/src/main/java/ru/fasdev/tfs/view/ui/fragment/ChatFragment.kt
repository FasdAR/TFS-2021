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

        val list: List<MessageDomain> = listOf(
            MessageDomain(0, 102,
            "Andrey Rednikov","Hello", Date(), listOf())
        )

        adapter.items = flatMapMessageList(105, list).reversed()
    }

    private fun flatMapMessageList(internalUserId: Int = 0, messages: List<MessageDomain>): List<ViewTyped> {
        val dateFormatKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val dateFormatUi = SimpleDateFormat("dd MMM", Locale.getDefault())

        val mapMessages = messages.groupBy { dateFormatKey.format(it.date) }
        val resultList: ArrayList<ViewTyped> = arrayListOf()
        mapMessages.keys
                .sorted()
                .forEach { key ->
                    val date = dateFormatKey.parse(key)
                    val items = mapMessages[key]

                    date?.let { date ->
                        resultList.add(DateUi(date.time.toInt(), date = dateFormatUi.format(date)))
                    }

                    items?.forEach {
                        if (it.idSender == internalUserId) {
                            resultList.add(
                                InternalMessageUi(
                                    it.id, it.message,
                                    it.listReaction.map {
                                        MessageReactionUi(it.emoji, it.countReaction, it.isSelected)
                                    }))
                        }
                        else {
                            resultList.add(ExternalMessageUi(it.id,
                                it.nameSender, R.drawable.ic_launcher_background.toString(), it.message,
                                it.listReaction.map { MessageReactionUi(it.emoji, it.countReaction, it.isSelected) }))
                        }
                    }
                }

        return resultList
    }

    data class MessageDomain(val id: Int, val idSender: Int,
                             val nameSender: String, val message: String, val date: Date,
                             val listReaction: List<ReactionDomain>)
    data class ReactionDomain(val emoji: String, val countReaction: Int, val isSelected: Boolean)
}