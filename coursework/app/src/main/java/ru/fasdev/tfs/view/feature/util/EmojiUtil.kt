package ru.fasdev.tfs.view.feature.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

class EmojiUtil {
    companion object {
        private const val EMOJI_ASSET_FILE = "emoji.txt"

        fun getListEmoji(context: Context): List<String> {
            val am = context.assets
            val reader = BufferedReader(InputStreamReader(am.open(EMOJI_ASSET_FILE)))
            val mLine: String = reader.readLine()
            reader.close()

            val pattern = Pattern.compile("\\P{M}\\p{M}*+").matcher(mLine)

            val matchList: MutableList<String> = mutableListOf()

            while (pattern.find()) {
                matchList.add(pattern.group())
            }

            return matchList
        }
    }
}
