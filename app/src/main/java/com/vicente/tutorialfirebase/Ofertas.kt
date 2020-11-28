package com.vicente.tutorialfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_productos.*

class Ofertas : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        productos()
    }
    private fun productos(){

        Guardarp.setOnClickListener {
            if (categoria.length()>=1&&nombre.length()>=1&&descripcion.length()>=1&&cantidad.length()>=1&&precio.length()>=1){
            db.collection("Productos").document().set(
                hashMapOf("Categoria" to categoria.text.toString(),
                    "Nombre" to nombre.text.toString(),
                    "Descripcion" to descripcion.text.toString(),
                    "Cantidad" to cantidad.text.toString(),
                    "Precio" to precio.text.toString())
            )
            Agregado()
            //Toast.makeText(this, "su producto se agregado", Toast.LENGTH_LONG).show()
            //val cambiar:Intent=Intent(this,adaptador::class.java)
            //startActivity(cambiar)

        }
            else
            {
                noAgregado()
            }

        }

    }
    private  fun  Agregado(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Felicidades")
        builder.setMessage("Se ha agregado correctamente tu articulo")
        builder.setPositiveButton("aceptar",null)
        //val cambiar1:Intent= Intent(this,adaptador::class.java)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    private fun noAgregado(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Llena todos los campos")
        builder.setMessage("No se ha agregado correctamente tu articulo")
        builder.setPositiveButton("aceptar",null)
        //val cambiar1:Intent= Intent(this,adaptador::class.java)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
}