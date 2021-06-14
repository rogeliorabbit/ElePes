package com.hitglynorthz.elepes.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hitglynorthz.elepes.ui.home.AllFragment
import com.hitglynorthz.elepes.ui.home.ArtistsFragment
import com.hitglynorthz.elepes.ui.home.FavsFragment

class TabsHomeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment()
            1 -> ArtistsFragment()
            2 -> FavsFragment()
            else -> AllFragment()
        }
    }

    override fun getItemCount(): Int {
        return NUM_FRAG
    }

    companion object {
        private const val NUM_FRAG = 3
    }
}