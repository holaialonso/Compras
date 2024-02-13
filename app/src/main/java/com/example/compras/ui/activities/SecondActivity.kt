package com.example.compras.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivitySecondBinding
import com.example.compras.model.Cart
import com.example.compras.model.Product


class SecondActivity : AppCompatActivity(), ProductAdapter.onRecyclerProductListener {

    //Fichero -> me traigo el layout (activity_second.xml)
    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerProducts : RecyclerView
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var cart : Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="" //sin literal en la navegacion

        //Carrito: recuperar el objeto del intent
        cart = intent.extras!!.getSerializable("cart") as Cart
        makeTotalCart(cart.getProducts())

        //Recycler View
        //Pinto la lista de productos en el RecyclerView
        ProductAdapter = ProductAdapter(cart.getProducts(), this, "second")

        recyclerProducts = findViewById(R.id.resume_cart)
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

        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 1, "iPhone 9", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/2/thumbnail.jpg", 2, "iPhone X", 899.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 3, "iPhone 14", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 4, "iPhone 15", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 5, "iPhone 16", 549.0));

        return aux

    }

    fun makeTotalCart(products: ArrayList<Product>) {

        var total : Double = 0.0

        for(product in products){

            total=total+product.price
        }

        findViewById<TextView>(R.id.amount_total).text=total.toString()+" €"

    }


    override fun onProductSelected(product: Product) {
        TODO("Not yet implemented")
    }

    override fun onProductSelectedRemove(position: Int) {

        //Elimino el elemento del cart
        ProductAdapter.removeElement(position)

    }

    override fun onMakeTotalCart(products: ArrayList<Product>) {
        makeTotalCart(products)
    }



}