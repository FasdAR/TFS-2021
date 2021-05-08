package ru.fasdev.tfs.data.newPck.source.network.users.model

import kotlinx.serialization.Serializable

@Serializable
class UserPresence(val status: PresenceStatus)