package com.harman.hhelper.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle


class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
    when(position){
        1 -> {

            return LiteratureFragment()
        }
        2 -> return ScheduleFragment()
    }
        return LinksFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }

}