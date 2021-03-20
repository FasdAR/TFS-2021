package ru.fasdev.tfs.view.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main)
{
    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentMainBinding.bind(it) }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}