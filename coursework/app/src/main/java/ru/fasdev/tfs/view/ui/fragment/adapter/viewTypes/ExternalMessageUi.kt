package ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class ExternalMessageUi(val nameSender: String, val avatarSrc: String,
                        val message: String, val reactions: List<MessageReactionUi>,
                        override val viewType: Int = R.layout.layout_external_message,
                        override val uId: Int): ViewTyped()