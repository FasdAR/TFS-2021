package ru.fasdev.tfs.domain.message.repo

import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.domain.user.model.User
import java.util.Date

class TestMessageRepoImpl : MessageRepo {
    // Пока дата захардкорена
    // #region Test Data
    private val users: List<User> = listOf(
        User(1, "", "Andrey Rednikov"),
        User(2, "", "Reagan Emery"),
        User(3, "", "Jayce Hester"),
        User(4, "", "Kady Benitez")
    )

    private val messageList: MutableList<Message> = generateInitialData()
    // #endregion

    override fun getMessageByChat(idChat: Int): List<Message> {
        return if (idChat == 1) messageList else emptyList()
    }

    override fun sendMessage(idChat: Int, idUser: Int, messageText: String) {
        messageList.add(
            Message(
                id = messageList.size + 1,
                sender = users.find { idUser == it.id.toInt() }!!,
                text = messageText
            )
        )
    }

    @ExperimentalStdlibApi
    override fun addReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String) {
        val messageIndex = messageList.indexOfFirst { it.id == idMessage }
        val message = messageList[messageIndex]

        val reactionIndex = message.reactions.indexOfFirst { it.emoji == emoji }

        val newReaction = if (reactionIndex != -1) {
            val reaction = message.reactions[reactionIndex]

            reaction.copy(
                isSelected = true,
                countSelection = reaction.countSelection + 1
            )
        } else {
            Reaction(emoji = emoji, countSelection = 1, isSelected = true)
        }

        val newReactions = buildList<Reaction> {
            addAll(message.reactions)
            if (reactionIndex != -1)
                removeAt(reactionIndex)

            if (reactionIndex != -1) add(reactionIndex, newReaction)
            else add(newReaction)
        }

        val newMessage = message.copy(reactions = newReactions)

        messageList.removeAt(messageIndex)
        messageList.add(messageIndex, newMessage)
    }

    @ExperimentalStdlibApi
    override fun removeReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String) {
        val messageIndex = messageList.indexOfFirst { it.id == idMessage }
        val message = messageList[messageIndex]

        val reactionIndex = message.reactions.indexOfFirst { it.emoji == emoji }

        var newMessage: Message? = null
        if (reactionIndex != -1) {
            val reaction = message.reactions[reactionIndex]
            val newCount = reaction.countSelection - 1

            newMessage = if (newCount <= 0) {
                val newReactions = buildList<Reaction> {
                    addAll(message.reactions)
                    removeAt(reactionIndex)
                }

                message.copy(reactions = newReactions)
            } else {
                val newReaction = reaction.copy(isSelected = false, countSelection = reaction.countSelection - 1)
                val newReactions = buildList<Reaction> {
                    addAll(message.reactions)
                    removeAt(reactionIndex)
                    add(reactionIndex, newReaction)
                }

                message.copy(reactions = newReactions)
            }
        }

        newMessage?.let {
            messageList.removeAt(messageIndex)
            messageList.add(messageIndex, newMessage)
        }
    }

    private fun generateInitialData(): MutableList<Message> = mutableListOf(
        Message(
            id = 1,
            sender = users[0],
            text = "Hello Chat!",
            date = Date(1581952113),
            reactions = listOf(
                Reaction(
                    emoji = "\uD83D\uDE04",
                    countSelection = 4,
                    isSelected = false
                ),
                Reaction(
                    emoji = "\uD83D\uDE0A",
                    countSelection = 10,
                    isSelected = true
                )
            )
        ),
        Message(
            id = 2,
            sender = users[1],
            text = "Hello ${users[0].fullName}!",
            date = Date(1581952114),
            reactions = listOf(
                Reaction(
                    emoji = "\uD83E\uDD19",
                    countSelection = 15,
                    isSelected = false
                )
            )
        ),
        Message(
            id = 3,
            sender = users[2],
            text = "I Happy"
        ),
        Message(
            id = 4,
            sender = users[3],
            text = "And I Fun",
            reactions = listOf(
                Reaction(
                    emoji = "\uD83D\uDC4D",
                    countSelection = 1,
                    isSelected = true
                )
            )
        ),
        Message(
            id = 5,
            sender = users[3],
            text = "Nogotochki",
            reactions = listOf(
                Reaction(
                    emoji = "\uD83D\uDC85",
                    countSelection = 1,
                    isSelected = false
                )
            )
        )
    )
}
