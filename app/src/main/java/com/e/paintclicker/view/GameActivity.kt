package com.e.paintclicker.view

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e.paintclicker.control.GameDataSingleton
import com.e.paintclicker.control.TabAdapter
import com.e.paintclicker.databinding.ActivityGameBinding
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

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

    override fun onPause() {
        super.onPause()

        var directory:File
        //check permissions for external storage
        /*if(false){
            directory=File( applicationContext.getExternalFilesDir(null),"saves")
        }
        else{
            directory=File(applicationContext.filesDir,"saves")
        }*/
        val file=File(applicationContext.filesDir,GameDataSingleton.playerName+".txt")
        if(!file.exists()){
            file.createNewFile()
        }
        val fos: FileOutputStream = FileOutputStream(file) //applicationContext.openFileOutput(file.path, Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)

        os.writeObject(GameDataSingleton.GetDataToSave())
        os.close()
        fos.close()

    }
}