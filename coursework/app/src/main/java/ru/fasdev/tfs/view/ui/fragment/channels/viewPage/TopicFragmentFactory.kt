package ru.fasdev.tfs.view.ui.fragment.channels.viewPage

import android.content.Context
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.topicList.TopicListFragment
import ru.fasdev.tfs.view.ui.global.viewPager.base.ViewPagerFragmentFactory

class TopicFragmentFactory : ViewPagerFragmentFactory
{
    companion object {
        private const val SUBSCRIBED_POS = 0
        private const val ALL_POS = 1
        private const val MAX_SIZE = 2
    }

    override fun createFragment(position: Int): Fragment? {
        return when (position) {
            SUBSCRIBED_POS -> TopicListFragment.newInstance(TopicListFragment.SUBSCRIBED_MODE)
            ALL_POS -> TopicListFragment.newInstance(TopicListFragment.ALL_MODE)
            else -> null
        }
    }

    override fun getTitle(context: Context, position: Int): String {
        return when(position) {
            SUBSCRIBED_POS -> context.resources.getString(R.string.subscribed)
            ALL_POS -> context.resources.getString(R.string.all_streams)
            else -> "NULL $position"
        }
    }

    override fun getSize(): Int = MAX_SIZE
}