package ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class InternalMessageUi(override val uId: Int,
                        val message: String,
                        val reactions: List<MessageReactionUi>,
                        override val viewType: Int = R.layout.item_internal_message): ViewTyped()