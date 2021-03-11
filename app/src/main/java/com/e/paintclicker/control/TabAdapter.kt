package com.e.paintclicker.control

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.e.paintclicker.view.ClickFragment
import com.e.paintclicker.view.RankingFragment
import com.e.paintclicker.view.ShopFragment

public class TabAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int):  FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()

                return RankingFragment()
            }
            1 -> {
                return ClickFragment()
            }
            2 -> {
                // val movieFragment = MovieFragment()

                return ShopFragment()
            }
            else -> return ClickFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }




}