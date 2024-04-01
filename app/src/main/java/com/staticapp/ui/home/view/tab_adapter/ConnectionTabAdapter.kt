package com.staticapp.ui.home.view.tab_fragment.tab_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.staticapp.ui.home.view.tab_fragment.ConnectionFragment
import com.staticapp.ui.home.view.tab_fragment.CustomMessageFragment

class ConnectionTabAdapter(fm: FragmentManager?,loc:Lifecycle): FragmentStateAdapter(fm!!,loc) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ConnectionFragment()
            }

            1 -> {
                CustomMessageFragment()
            }

            else->{
                return  ConnectionFragment()
            }
    }


}}