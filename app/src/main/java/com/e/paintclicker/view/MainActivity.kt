package com.e.paintclicker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.e.paintclicker.R
import com.e.paintclicker.control.GameDataSingleton
import com.e.paintclicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(GameDataSingleton.playerName!=null&&GameDataSingleton.playerName!=""){
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }
        binding.editUsername.addTextChangedListener( object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.button.isEnabled = binding.editUsername.text.length != 0
            }

        })

        binding.button.setOnClickListener { v ->
            val intent = Intent(applicationContext, GameActivity::class.java)

            GameDataSingleton.playerName= binding.editUsername.text.toString()
            startActivity(intent)
        }


    }
}