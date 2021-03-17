package ru.fasdev.tfs.domain.message.repo

import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.domain.model.User

class TestMessageRepoImpl: MessageRepo
{
    // Пока дата захардкорена
    //#region Test Data
    private val users: List<User> = listOf(
        User(1, "", "Andrey Rednikov"),
        User(2, "", "Reagan Emery"),
        User(3, "", "Jayce Hester"),
        User(4, "", "Kady Benitez")
    )

    private val messageList: MutableList<Message> = generateInitialData()
    //#endregion

    override fun getMessageByChat(idChat: Int): List<Message> =
        if (idChat == 1) messageList else emptyList()

    override fun sendMessage(idChat: Int, idUser: Int, messageText: String) {
        messageList.add(
            Message(
                id = messageList.size + 1,
                sender = users.find { idUser == it.id }!!,
                text = messageText
        ))
    }

    override fun addReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String) {
        val messageIndex = messageList.indexOfFirst { it.id == idMessage }
        val message = messageList[messageIndex]

        val reactionIndex = message.reactions.indexOfFirst { it.emoji == emoji }

        val newMessage = if (reactionIndex != -1) {
            val reaction = message.reactions[reactionIndex]
            val newReactionsUser = arrayListOf(idUser).addAll(reaction.selectedUsersId) as List<Int>
            val newReaction = reaction.copy(selectedUsersId = newReactionsUser)
            val newReactions = arrayListOf(newReaction).addAll(message.reactions) as List<Reaction>

            message.copy(reactions = newReactions)
        } else {
            val newReaction = Reaction(emoji, listOf(idUser))
            val newReactions = mutableListOf(newReaction).addAll(message.reactions) as List<Reaction>
            message.copy(reactions = newReactions)
        }

        messageList.removeAt(messageIndex)
        messageList.add(newMessage)
    }

    override fun removeReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String) {
        val messageIndex = messageList.indexOfFirst { it.id == idMessage }
        val message = messageList[messageIndex]

        val reactionIndex = message.reactions.indexOfFirst { it.emoji == emoji }

        var newMessage: Message? = null
        if (reactionIndex != -1) {
            val reaction = message.reactions[reactionIndex]
            val userIndex = reaction.selectedUsersId.indexOf(idUser)
            val newReactionsUser = mutableListOf(reaction.selectedUsersId).removeAt(userIndex)

            newMessage = if (newReactionsUser.isEmpty()) {
                val newReactions = mutableListOf(message.reactions).removeAt(reactionIndex)
                message.copy(reactions = newReactions)
            } else {
                val newReaction = reaction.copy(selectedUsersId = newReactionsUser)
                val newReactions = mutableListOf(newReaction).addAll(message.reactions) as List<Reaction>
                message.copy(reactions = newReactions)
            }
        }

        newMessage?.let {
            messageList.removeAt(messageIndex)
            messageList.add(newMessage)
        }
    }

    private fun generateInitialData(): MutableList<Message> = mutableListOf(
        Message(
            id = 1,
            sender = users[0],
            text = "Hello Chat!",
            reactions = listOf(
                Reaction(
                    emoji = "\uD83D\uDE04",
                    selectedUsersId = listOf(users[1].id, users[2].id, users[3].id)
                ),
                Reaction(
                    emoji = "\uD83D\uDE0A",
                    selectedUsersId = listOf(users[0].id)
                )
            )
        ),
        Message(
            id = 2,
            sender = users[1],
            text = "Hello ${users[0].fullName}!",
            reactions = listOf(
                Reaction(
                    emoji = "\uD83E\uDD19",
                    selectedUsersId = listOf(users[0].id)
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
                    selectedUsersId = listOf(users[3].id)
                )
            )
        ),
        Message(
            id = 4,
            sender = users[3],
            text = "Nogotochki",
            reactions = listOf(
                Reaction(
                    emoji = "\uD83D\uDC85",
                    selectedUsersId = listOf(users[0].id, users[1].id)
                )
            )
        )
    )
}