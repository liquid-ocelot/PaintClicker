package com.e.paintclicker.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.e.paintclicker.control.GameDataSingleton
import com.e.paintclicker.control.currencyEnum
import com.e.paintclicker.databinding.ActivityMainBinding
import java.io.*

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

            var directory:File
            /*if(false){
                directory= File( applicationContext.getExternalFilesDir(null),"saves")

            }
            else{
                directory= File(applicationContext.filesDir,"saves")
            }
            if(!directory.exists()){
                directory.createNewFile()
            }*/

            val file= File(applicationContext.filesDir, GameDataSingleton.playerName+".txt")

            if(!file.exists()){
                file.createNewFile()
            }
            else{
                val fis: FileInputStream = FileInputStream(file)
                val `is` = ObjectInputStream(fis)
                GameDataSingleton.SetDataFromSave(`is`.readObject() as List<Byte>)
                if(GameDataSingleton.sponsorLevel>0||GameDataSingleton.apprenticeLevel>0||GameDataSingleton.currencies[currencyEnum.ArtBucks.index].amount>0){
                    GameDataSingleton.canSellPaintings=true
                }
                `is`.close()
                fis.close()
            }

            startActivity(intent)
        }

        //PERMISSIONS
        ActivityCompat.requestPermissions(this, Array<String>(1){ Manifest.permission.READ_EXTERNAL_STORAGE}, 0)
        ActivityCompat.requestPermissions(this, Array<String>(1){ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0)
    }
}