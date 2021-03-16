package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes.DateUi
import java.text.SimpleDateFormat
import java.util.Date

fun Date.toDateUi(dateFormat: SimpleDateFormat) = DateUi(time.toInt(), dateFormat.format(this))
