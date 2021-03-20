package ru.fasdev.tfs.view.ui.fragment.people

import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R

class PeopleFragment : Fragment(R.layout.fragment_people)
{
    companion object {
        val TAG: String = PeopleFragment::class.java.simpleName
        fun newInstance(): PeopleFragment = PeopleFragment()
    }
}