package ru.fasdev.tfs.view.ui.fragment.profile

import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R

class ProfileFragment : Fragment(R.layout.fragment_profile)
{
    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}