package ru.fasdev.tfs.screen.fragment.chat.mvi.model

import ru.fasdev.tfs.screen.fragment.chat.model.DirectionScroll

class PageLoadInfo(
    val idStream: Long,
    val nameStream: String,
    val nameTopic: String,
    val idAnchorMessage: Long?,
    val afterMessageCount: Int,
    val beforeMessageCount: Int,
    val directionScroll: DirectionScroll
)