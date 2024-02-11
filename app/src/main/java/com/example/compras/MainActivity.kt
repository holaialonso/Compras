package com.example.compras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.compras.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    //Fichero -> me traigo el layout (activity_main.xml)
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinnerCategory : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="app compras"

        //Spinner
        spinnerCategory = findViewById(R.id.select_categories)

        binding.selectCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var optionSelected = parent!!.adapter.getItem(position).toString()
                Snackbar.make(binding.root, optionSelected, Snackbar.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }




    }

    //MENÚ
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_main_activity, menu)
            return true
        }

        //Método para los eventos de los botones del menú
        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            return when (item.itemId) {
                R.id.view_cart -> {

                    //Pasar de una pantalla a otra
                    val intent : Intent = Intent (this, SecondActivity::class.java)
                    startActivity(intent)

                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }





}
