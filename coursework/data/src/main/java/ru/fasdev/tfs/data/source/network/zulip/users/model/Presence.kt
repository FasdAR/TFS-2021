package ru.fasdev.tfs.data.source.network.zulip.users.model

import kotlinx.serialization.Serializable

@Serializable
class Presence (val aggregated: UserPresence)