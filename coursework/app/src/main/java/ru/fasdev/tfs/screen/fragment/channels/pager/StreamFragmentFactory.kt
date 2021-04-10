package ru.fasdev.tfs.screen.fragment.channels.pager

import android.content.Context
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.fragment.streamList.StreamListFragment
import ru.fasdev.tfs.pager.ViewPagerFragmentFactory

class StreamFragmentFactory : ViewPagerFragmentFactory {
    companion object {
        private const val SUBSCRIBED_POS = 0
        private const val ALL_POS = 1
        private const val PAGE_SIZE = 2
    }

    override fun createFragment(position: Int): Fragment? {
        return when (position) {
            SUBSCRIBED_POS -> StreamListFragment.newInstance(StreamListFragment.SUBSCRIBED_MODE)
            ALL_POS -> StreamListFragment.newInstance(StreamListFragment.ALL_MODE)
            else -> null
        }
    }

    override fun getTitle(context: Context, position: Int): String {
        return when (position) {
            SUBSCRIBED_POS -> context.resources.getString(R.string.subscribed)
            ALL_POS -> context.resources.getString(R.string.all_streams)
            else -> "NULL $position"
        }
    }

    override fun getSize(): Int = PAGE_SIZE
}
