package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.recycler.item.date.DateItem
import java.text.SimpleDateFormat
import java.util.Date

fun Date.toDateUi(dateFormat: SimpleDateFormat) = DateItem(time.toInt(), dateFormat.format(this))
