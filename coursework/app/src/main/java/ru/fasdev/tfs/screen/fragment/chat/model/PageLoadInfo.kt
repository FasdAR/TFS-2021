package ru.fasdev.tfs.screen.fragment.chat.model

class PageLoadInfo(val nameStream: String,
                   val nameTopic: String,
                   val idAnchorMessage: Long?,
                   val afterMessageCount: Int,
                   val beforeMessageCount: Int,
                   val directionScroll: DirectionScroll)