package ru.fasdev.tfs.domain.message.interactor

import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.domain.model.User
import java.util.*

class MessageInteractorImpl : MessageInteractor
{
    //Пока дата захардкорена, далее скорее всего уйдет в отдельный data модуль
    //#region Test Data
    private val currentUser: User = User(id = 1, "", "Self User")

    private val messageList: MutableList<Message> = mutableListOf(
            Message(1,
                    currentUser,
                    "Hello Chat!", Date(1584278973),
                    mutableListOf(
                            Reaction("\uD83E\uDD24", 10, false)
                    )
            ),
            Message(2,
                    User(2, "", "Test Test2"),
                    "Hello Test2", Date(),
                    mutableListOf(
                            Reaction("\uD83E\uDD74", 5, true)
                    )
            ),
            Message(3,
                    currentUser,
                    "Hello Test1", Date(),
                    mutableListOf()
            )
    )
    //#endregion

    override fun getMessageByChat(idChat: Int): List<Message>
    {
        return if (idChat == 1) messageList
        else emptyList()
    }

    override fun setSelectedReaction(idMessage: Int, emoji: String, isSelected: Boolean) {
        val indexMessage = messageList.indexOfFirst { it.id == idMessage }
        val indexReaction = messageList[indexMessage].reactions.indexOfFirst { it.emoji == emoji }

        if (indexReaction == -1 && isSelected) {
            messageList[indexMessage].reactions.add(Reaction(emoji, 1, true))
        }
        else {
            with(messageList[indexMessage].reactions[indexReaction]) {
                val newCount = if (isSelected) this.countSelected - 1 else this.countSelected + 1
                val newReaction = this.copy(countSelected = newCount, isSelected = isSelected)
                messageList[indexMessage].reactions[indexReaction] = newReaction
            }
        }
    }

    override fun sendMessage(text: String) {
        messageList.add(Message(messageList.last().id + 1, currentUser, text, Date(), mutableListOf()))
    }
}