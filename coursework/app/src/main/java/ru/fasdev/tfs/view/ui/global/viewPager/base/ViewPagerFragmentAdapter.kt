package ru.fasdev.tfs.view.ui.global.viewPager.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentAdapter : FragmentStateAdapter {
    constructor(fragment: Fragment, fragmentFactory: ViewPagerFragmentFactory) : super(fragment) {
        this.fragmentFactory = fragmentFactory
    }

    constructor(fragmentActivity: FragmentActivity, fragmentFactory: ViewPagerFragmentFactory) :
        super(fragmentActivity) {
            this.fragmentFactory = fragmentFactory
        }

    private var fragmentFactory: ViewPagerFragmentFactory

    override fun getItemCount(): Int = fragmentFactory.getSize()

    override fun createFragment(position: Int): Fragment {
        val newFragment = fragmentFactory.createFragment(position)

        return newFragment ?: Fragment()
    }
}
