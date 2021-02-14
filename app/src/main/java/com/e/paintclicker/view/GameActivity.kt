package com.e.paintclicker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.paintclicker.R
import com.e.paintclicker.control.TabAdapter
import com.e.paintclicker.databinding.ActivityGameBinding
import com.google.android.material.tabs.TabLayout

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Rank"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Click"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Shop"))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabAdapter(this, supportFragmentManager, binding.tabLayout.tabCount)
        binding.pager.adapter = adapter

        binding.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.pager.currentItem = tab!!.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })


    }
}