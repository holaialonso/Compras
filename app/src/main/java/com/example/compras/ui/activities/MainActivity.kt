package com.example.compras.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.R
import com.example.compras.adapter.ProductAdapter
import com.example.compras.databinding.ActivityMainBinding
import com.example.compras.model.Product
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject


/*

            spinnerCategory.setSelection(selectedIndex, true)
 */

class MainActivity : AppCompatActivity(), OnItemSelectedListener, ProductAdapter.onRecyclerProductListener{

    //Fichero -> me traigo el layout (activity_main.xml)
    private lateinit var binding: ActivityMainBinding

    //Variables globales
    private lateinit var spinnerCategory : Spinner
    private lateinit var recyclerProducts : RecyclerView
    private lateinit var ProductAdapter : ProductAdapter
    private lateinit var SpinnerCategoriesAdapter : ArrayAdapter<String>
    private lateinit var cart : ArrayList<Product>
    private var selectedIndex : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Parte gráfica + lógica del activity
            var binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

        //Cart
            cart = ArrayList()

            //1. Si tiene el estado guardado -> lo recupero
            if (savedInstanceState != null) {
                val savedArrayList = savedInstanceState.getSerializable("cart")
                if (savedArrayList is ArrayList<*>) {
                    cart = savedArrayList as ArrayList<Product>
                }
            }


            //2. Compruebo si viene de la secondActivity -> si lo tengo, lo inicializo con ese valor
            if (intent.hasExtra("cart")) {
               cart = intent.extras!!.getSerializable("cart") as ArrayList<Product>
            }


        //Menú
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title="Compras" //no quiero que salga ningún literal en la barra de navegacón

        //Spinner
            spinnerCategory = findViewById(R.id.select_categories) //hago el binding
            selectedIndex = savedInstanceState?.getInt("selectedIndex") ?: selectedIndex //recojo el valor de la opción seleccionada

            SpinnerCategoriesAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item) //adaptador del spinner
            SpinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            makeCategoryProducts() //después de declarar el adaptador -> relleno los items del spinner

            spinnerCategory.adapter = SpinnerCategoriesAdapter


            //Eventos
            binding.selectCategories.onItemSelectedListener = object : OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val optionSelected = parent!!.adapter.getItem(position).toString()
                    ProductAdapter.makeFilter(optionSelected)

                    //Textview que muestro cuando no hay productos
                    val emptyProducts: View = findViewById(R.id.empty_products)
                    if(optionSelected.equals("Categorías", true)){
                        emptyProducts.visibility = View.INVISIBLE
                    }
                    else{ //Resto de categorías
                        if(ProductAdapter.getItemCount()==0) { //no tengo elementos para mostrar
                            emptyProducts.visibility = View.VISIBLE
                        }
                        else{
                            emptyProducts.visibility = View.INVISIBLE
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }


        //Recycler View de los productos
            ProductAdapter = ProductAdapter(ArrayList(), this, "main")
            makeListProducts()

            recyclerProducts = findViewById(R.id.recycler_products)
            recyclerProducts.adapter = ProductAdapter
            recyclerProducts.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

    }

    //ESTADO -> PORTRAIT <-> LANDSCAPE
        //Método para guardar el estado
        override fun onSaveInstanceState(outState: Bundle) {

            super.onSaveInstanceState(outState)
            outState.putSerializable("cart", cart)
            outState.putInt("selectedIndex", spinnerCategory.selectedItemPosition)

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


    //COMUNICACIÓN ADAPTADOR -> ACTIVITY
    // Método que comunica el adaptador con la activity
        override fun onProductSelected(product: Product) {

            //Añado el producto al carrito
            cart.add(product)

            //Muestro en pantalla un aviso
            Snackbar.make(findViewById(android.R.id.content), "Añadido al carrito: "+product.name, Snackbar.LENGTH_LONG).show()

        }

        override fun onProductSelectedRemove(idProduct: Int) {
            TODO("Not yet implemented")
        }

        override fun onPrintTotalCart(total: String, type: String) {
            TODO("Not yet implemented")
        }

        override fun onShowEmptyCart() {
            TODO("Not yet implemented")
        }



    //JSON
        //Método que trae la información de los productos
        fun makeListProducts(){

            //Monto la petición
            val request : JsonObjectRequest = JsonObjectRequest(
                "https://dummyjson.com/products",
                {
                    //RESPUESTA DE DATOS
                    //Lo que me devuelve la API
                    val result : JSONArray = it.getJSONArray("products")

                    //Recorro la información que tengo
                    for(i in 0 until result.length()){

                        val element = result[i] as JSONObject
                        val product : Product = Product(element.getString("id").toInt(), element.getString("thumbnail"), element.getString("title"), formatLiteral(element.getString("category")) ,element.getString("price").toDouble())
                        ProductAdapter.addElement(product)

                    }

                },
                {
                    //ERROR
                    Snackbar.make(binding.root, "Error en la conexion", Snackbar.LENGTH_SHORT).show();

                })

            //Hago la petición
            Volley.newRequestQueue(applicationContext).add(request)
        }

        //Método para traer la información de las categorías existentes para los productos
        fun makeCategoryProducts() {

            SpinnerCategoriesAdapter.add("Categorías")

            //Monto la petición
            val request = JsonArrayRequest(Request.Method.GET, "https://dummyjson.com/products/categories", null,
               { response ->
                    // Manejar la respuesta JSON
                    for (i in 0 until response.length()) {
                        val category = response.getString(i)
                        SpinnerCategoriesAdapter.add(formatLiteral(category))
                    }

                    //Selecciono en la lista el elemento
                    spinnerCategory.setSelection(selectedIndex)

                },
               { error ->
                    // Manejar errores de la solicitud
                    Snackbar.make(binding.root, "Error en la conexion", Snackbar.LENGTH_SHORT).show();
                })


            //Hago la petición
            Volley.newRequestQueue(applicationContext).add(request)


        }

        //Método para limpiar la categoría del producto y ponerla bonita (que no salgan cosas como womens-watches)
        fun formatLiteral (literal : String) : String{

            val aux=literal.replace("-", " ")

            return aux.substring(0, 1).toUpperCase() + aux.substring(1)
        }



    //Métodos de la interfaz: OnItemSelectedListener
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            TODO("Not yet implemented")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }




}


