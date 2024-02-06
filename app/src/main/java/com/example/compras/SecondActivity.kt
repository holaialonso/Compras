package com.example.compras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.compras.databinding.ActivityMainBinding

class SecondActivity : AppCompatActivity() {

    //Fichero -> me traigo el layout (activity_second.xml)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="title"
    }


    //MENÚ
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second_activity, menu)
        return true
    }
}