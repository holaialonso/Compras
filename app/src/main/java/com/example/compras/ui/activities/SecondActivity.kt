package com.example.compras.ui.activities

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivitySecondBinding
import com.example.compras.model.Product


class SecondActivity : AppCompatActivity(), ProductAdapter.onRecyclerProductListener {

    //Fichero -> me traigo el layout (activity_second.xml)
    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerProducts : RecyclerView
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var cart : ArrayList<Product>
    private lateinit var totalCart : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menú
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="" //sin literal en la navegacion

        //Carrito: recuperar el objeto del intent
        cart = intent.extras!!.getSerializable("cart") as ArrayList<Product>


        //Recycler View
        //Pinto la lista de productos en el RecyclerView
        ProductAdapter = ProductAdapter(ArrayList(), this, "second")
        makeSetProductsCart(cart);

        recyclerProducts = findViewById(R.id.resume_cart)
        recyclerProducts.adapter = ProductAdapter
        recyclerProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //Total de los productos: lo imprimo en pantalla
        totalCart = findViewById(R.id.amount_total)
        ProductAdapter.makeTotal()

    }


    //MENÚ
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second_activity, menu)
        return true
    }


    //DATOS QUE MANEJA LA ACTIVITY
        //Método que setea los datos del cart en el ProductAdapter
        fun makeSetProductsCart(cart : ArrayList<Product>){

            for(product in cart){
                ProductAdapter.addElement(product)
            }

        }



    //COMUNICACIÓN ADAPTADOR -> ACTIVITY
    // Método que comunica el adaptador con la activity
        override fun onProductSelected(product: Product) {
            TODO("Not yet implemented")
        }

        override fun onProductSelectedRemove(position: Int) {

            //Elimino el elemento del cart
            ProductAdapter.removeElement(position)

        }

        override fun onPrintTotalCart(total: Double) {
            totalCart.text=total.toString()+" €"
        }


}