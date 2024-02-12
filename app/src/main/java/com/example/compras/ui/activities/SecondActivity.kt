package com.example.compras.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivityMainBinding
import com.example.compras.databinding.ActivitySecondBinding
import com.example.compras.model.Product

class SecondActivity : AppCompatActivity() {

    //Fichero -> me traigo el layout (activity_second.xml)
    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerProducts : RecyclerView.Recycler
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var productList : ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="" //sin literal en la navegacion


        //Recycler View de los productos
        productList = getProducts() // Devuelve la lista de los productos
        ProductAdapter = ProductAdapter(productList, this, "second")

        val recyclerProducts: RecyclerView = findViewById(R.id.resume_cart)
        recyclerProducts.adapter = ProductAdapter
        recyclerProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    //MENÚ
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second_activity, menu)
        return true
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

}