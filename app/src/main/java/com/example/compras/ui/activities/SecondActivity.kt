package com.example.compras.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivitySecondBinding
import com.example.compras.model.Product
import com.google.android.material.snackbar.Snackbar


class SecondActivity : AppCompatActivity(), ProductAdapter.onRecyclerProductListener {

    //Fichero -> me traigo el layout (activity_second.xml)
    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerProducts : RecyclerView
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var cart : ArrayList<Product>
    private lateinit var totalCart : TextView
    private lateinit var emptyCart : View

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
        ProductAdapter.showTotal("textview")

        //Mostrar el mensaje de "carrito vacío" si no hay productos
        emptyCart = findViewById(R.id.empty_cart)
        showEmptyCart()


    }


    //MENÚ
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.confirm_cart -> { //confirmar el carrito

                if(ProductAdapter.getItemCount()>0){ //confirmo el carrito si tengo elementos dentro del carrito
                    ProductAdapter.showTotal("snackbar")
                    makeEmptyCart()
                }
                else{
                    Snackbar.make(binding.root, "El carrito esta vacío, no puedes realizar una compra.", Snackbar.LENGTH_LONG).show()
                }

                true
            }
            R.id.empty_cart -> { //vaciar el carrito

                if(ProductAdapter.getItemCount()>0){ //si tengo productos dentro del carrito
                    makeEmptyCart()
                    Snackbar.make(binding.root, "El carrito se ha vaciado.", Snackbar.LENGTH_LONG).show()
                }
                else{
                    Snackbar.make(binding.root, "El carrito ya está vacío.", Snackbar.LENGTH_LONG).show()
                }

                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //DATOS QUE MANEJA LA ACTIVITY
        //Método que setea los datos del cart en el ProductAdapter
        fun makeSetProductsCart(cart : ArrayList<Product>){

            for(product in cart){
                ProductAdapter.addElement(product)
            }

        }


        //Método que muestra (o no) el mensaje de "no hay productos en el carrito" cuando es necesario
        fun showEmptyCart(){

            if(ProductAdapter.getItemCount()==0) {
                emptyCart.visibility = View.VISIBLE
            }
            else{
                emptyCart.visibility = View.GONE
            }
        }


        //Método para vaciar el carrito
        fun makeEmptyCart(){

            cart = ArrayList() //vacío el carrito
            ProductAdapter.removeAllElements() //vacío los elementos de la lista
            ProductAdapter.showTotal("textview") //actualizo el total en el textview
            showEmptyCart() //muestro el mensaje de "no hay productos en el carrito
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

        override fun onPrintTotalCart(total: String, type : String) {

            when(type){
                "textview" ->{
                    totalCart.text=total.toString()+" €"
                }

                "snackbar" ->{
                    Snackbar.make(findViewById(android.R.id.content), "Enhorabuena, compra por valor de "+total.toString()+" € realizada", Snackbar.LENGTH_LONG).show()
                }
            }


        }

        override fun onShowEmptyCart() {
            showEmptyCart()
        }

    //CART -> PASARLO A LA MAINACTIVITY
    override fun onBackPressed() {

        intent.putExtra("cart", cart) //paso la variable a la mainActivity
        super.onBackPressed()

    }

}