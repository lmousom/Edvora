package com.mousom.edvora.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mousom.edvora.ui.NearestRide
import com.mousom.edvora.ui.PastRide
import com.mousom.edvora.ui.UpcomingRide
import com.mousom.edvora.utils.Constants.Companion.NUM_TABS


class RideStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return NearestRide()
            1 -> return UpcomingRide()
        }
        return PastRide()
    }
}