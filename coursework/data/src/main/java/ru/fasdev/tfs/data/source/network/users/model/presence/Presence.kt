package ru.fasdev.tfs.data.source.network.users.model.presence

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.users.model.user.UserPresence

@Serializable
class Presence (val aggregated: UserPresence)