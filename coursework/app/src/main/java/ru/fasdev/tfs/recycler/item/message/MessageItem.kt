package ru.fasdev.tfs.recycler.item.message

import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

open class MessageItem : ViewType() {
    open val message: String
        get() = error("Don't Provide Message: $this")
    open val reactions: List<MessageReactionUi>
        get() = error("Don't Provide Reactions: $this")
}
