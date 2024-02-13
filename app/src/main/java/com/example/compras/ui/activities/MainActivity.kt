package com.example.compras.ui.activities

import android.content.Context
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
import com.example.compras.model.Cart
import com.example.compras.model.Product
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), ProductAdapter.onRecyclerProductListener {

    //Fichero -> me traigo el layout (activity_main.xml)
    private lateinit var binding: ActivityMainBinding

    //Variables globales
    private lateinit var spinnerCategory : Spinner
    private lateinit var recyclerProducts : Recycler
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var productList : ArrayList<Product>
    private var cart : Cart = Cart()

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

        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 1, "iPhone 9", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/2/thumbnail.jpg", 2, "iPhone X", 899.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 3, "iPhone 14", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 4, "iPhone 15", 549.0));
        aux.add(Product( "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", 5, "iPhone 16", 549.0));

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
                    intent.putExtra("cart", cart) //paso el carrito de una pantalla a otra
                    startActivity(intent)

                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    //Método que comunica el adaptador con la activity
    override fun onProductSelected(product: Product) {

        //Añado el producto al carrito
        cart.setProduct(product)
        //Snackbar.make(requireView(), "Añadido al carrito: "+product.name, Snackbar.LENGTH_SHORT).show()
        println("tamaño ->"+cart.getSize())

    }

    override fun onProductSelectedRemove(idProduct: Int) {
        TODO("Not yet implemented")
    }

    override fun onMakeTotalCart(products: ArrayList<Product>) {
        TODO("Not yet implemented")
    }



}
