package com.example.compras.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ButtonBarLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.compras.R
import com.example.compras.model.Product
import com.google.android.material.snackbar.Snackbar


class ProductAdapter (private var products : ArrayList <Product>, private val contexto : Context, private val activity : String): RecyclerView.Adapter<ProductAdapter.MyHolder>() {

    //Listener de la clase
    private lateinit var listener: onRecyclerProductListener
    private lateinit var allProducts : ArrayList<Product>

    //Clase que pinta los valores de la lista
    class MyHolder(item: View) : ViewHolder(item) {



        // Elementos del producto
        var nombre: TextView
        var precio: TextView
        var image: ImageView
        var btnComprar: Button ?
        var btnEliminar : Button ?


        init {
            // imagen = item.findViewById(R.id.product_image)
            nombre = item.findViewById(R.id.product_name)
            precio = item.findViewById(R.id.product_price)
            image = item.findViewById(R.id.imageView)
            btnComprar = item.findViewById(R.id.buttonAddCart)
            btnEliminar = item.findViewById(R.id.buttonRemove)


        }

    }


    //Init
    init {
        listener = contexto as onRecyclerProductListener
        allProducts = products
    }

    //GRÁFICA
        //Método que pinta para cada elemento de la lista la parte gráfica
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

            //Inicializo la vista por defecto (la del main)
            var vista: View = LayoutInflater.from(contexto)
                .inflate(R.layout.item_product, parent, false)

            //La vista del second activity
            if(activity=="second"){
                vista = LayoutInflater.from(contexto)
                        .inflate(R.layout.resume_product, parent, false)
            }

            return MyHolder(vista)
        }

        //Método que retorna el número de elementos en la lista
        override fun getItemCount(): Int {
            return products.size
        }

        //Método que representa cada dato en una posición
        override fun onBindViewHolder(holder: MyHolder, position: Int) {

            var product = products[position]
            holder.nombre.text = product.name
            holder.precio.text = clearNumber(product.price.toString())+" €"
            Glide.with(contexto).load(product.image).into(holder.image)

            //Botón comprar
            holder.btnComprar?.setOnClickListener() {
                listener.onProductSelected(product)

            }

            //Botón eliminar
            holder.btnEliminar?.setOnClickListener() {
                listener.onProductSelectedRemove(position) //elimino el elemento
                showTotal("textview") //actualizo el total
                listener.onShowEmptyCart() //compruebo si tengo que mostrar el cartel de "no hay productos en el carrito"
            }
        }

    //DATOS
        //Método para añadir un elemento a la lista
        fun addElement(element : Product){

            products.add(element)
            allProducts = products
            notifyItemInserted(products.size-1)

        }


        //Método para eliminar elementos de la lista
        fun removeElement(position : Int){

            //Compruebo antes de eliminar el elemento que la lista no la tengo vacía y que no estoy intentando acceder a un elemento de ésta que no exista
            if ((products.isNotEmpty())&&(position in 0 until products.size)) {
                products.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, products.size)
            }

        }

        //Método para eliminar todos los elementos de la lista
        fun removeAllElements(){

            products = ArrayList()
            notifyDataSetChanged()

            listener.onUpdateCart(products) //actualizo la lista de productos en el cart
        }

        //Método para filtrar la lista completa
        fun makeFilter(option : String){

            if(option.equals("Categorías", true)){ //default
                this.products = ArrayList(allProducts)
            }
            else{
                this.products = ArrayList(allProducts.filter{it.category.equals(option, true)})
            }

            notifyDataSetChanged()
        }


        //Método para calcular el total de los elementos del carrito
        fun showTotal(type : String){

            listener.onPrintTotalCart(clearNumber(makeTotal().toString()), type)

        }

        fun makeTotal() : Double{

            var total=0.0

            for(product in products){

                total=total+product.price.toDouble()
            }

            return total
        }


    //COMUNICACIÓN ADAPTER -> ACTIVITY
        //Interfaz para la comunicación entre el adaptador y la activity
        interface onRecyclerProductListener {

            fun onProductSelected(product: Product)

            fun onProductSelectedRemove(idProduct : Int)

            fun onPrintTotalCart(total : String, type : String) //imprimir el total del carrito

            fun onShowEmptyCart() //compruebo la vista que tengo que mostrar del carrito (en caso de que esté vacío -> "no hay productos en el carrito")

            fun onUpdateCart(products : ArrayList <Product>)

        }


    //-------
    //FUNCIONES PARA TRATAR LOS NÚMEROS
        //Función para limpiar los números (y que no salga el .0 a la derecha)
        private fun clearNumber(number : String) : String{
            var aux=" "

            if(isInt(number)){ //Si el número es entero
                //Le quito el ".0" del final
                aux=number.replace(".0", "")
            }
            else{ //Si el número es decimal -> lo retorno tal cuál

                aux=number
            }

            return aux
        }

        //Método para comprobar si un número es entero
        private fun isInt (number : String) : Boolean{

            var aux=number.split(".")
            var isInt=true

            if(aux.size>1){

                if(aux[1]!= "0"){
                    isInt=false
                }
            }

            return isInt
        }



}