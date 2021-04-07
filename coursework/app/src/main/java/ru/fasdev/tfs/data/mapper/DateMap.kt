package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.DateUi
import java.text.SimpleDateFormat
import java.util.Date

fun Date.toDateUi(dateFormat: SimpleDateFormat) = DateUi(time.toInt(), dateFormat.format(this))
