package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType

import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model.MessageReactionUi

open class MessageUi : ViewType() {
    open val message: String
        get() = error("Don't Provide Message: $this")
    open val reactions: List<MessageReactionUi>
        get() = error("Don't Provide Reactions: $this")
}
