package ru.fasdev.tfs.view.ui.fragment.channels

import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R

class ChannelsFragment : Fragment(R.layout.fragment_channels)
{
    companion object {
        val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
    }
}