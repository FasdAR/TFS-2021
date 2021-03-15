package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes.DateUi
import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateUi(dateFormat: SimpleDateFormat) = DateUi(time.toInt(), dateFormat.format(this))