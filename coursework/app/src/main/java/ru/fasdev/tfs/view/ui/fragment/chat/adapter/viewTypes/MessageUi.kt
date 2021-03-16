package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes

import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

open class MessageUi(): ViewTyped() {
    open val message: String
        get() = error("Don't Provide Message: $this")
    open val reactions: List<MessageReactionUi>
        get() = error("Don't Provide Reactions: $this")
}