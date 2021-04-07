package ru.fasdev.tfs.screen.fragment.chat.recycler.viewType

import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

open class MessageUi : ViewType() {
    open val message: String
        get() = error("Don't Provide Message: $this")
    open val reactions: List<MessageReactionUi>
        get() = error("Don't Provide Reactions: $this")
}
