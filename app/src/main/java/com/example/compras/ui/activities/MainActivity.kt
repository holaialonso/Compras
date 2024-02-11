package com.example.compras.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivityMainBinding
import com.example.compras.model.Product
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    //Fichero -> me traigo el layout (activity_main.xml)
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinnerCategory : Spinner
    private lateinit var recyclerProducts : Recycler
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var productList : ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="" //no quiero que salga ningún literal en la barra de navegacón

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
                if(optionSelected!="Categorías") {
                    Snackbar.make(binding.root, optionSelected, Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Recycler View de los productos
        productList = getProducts() // Devuelve la lista de los productos
        ProductAdapter = ProductAdapter(productList, this, "main")

        val recyclerProducts: RecyclerView = findViewById(R.id.recycler_products)
        recyclerProducts.adapter = ProductAdapter
        recyclerProducts.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)





    }

    fun getProducts() : ArrayList<Product>{

        var aux : ArrayList<Product> = ArrayList()

        aux.add(Product( "prueba1", 2.09))
        aux.add(Product("prueba2", 2.09))
        aux.add(Product( "prueba3", 2.09))
        aux.add(Product("prueba4", 2.09))
        aux.add(Product( "prueba5", 2.09))

        return aux

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
