package com.vicente.tutorialfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    Bienvenido,
    GOOGLE
}

class homeActivity : AppCompatActivity() {
     private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        boton_dos()

        //setup
        val bundle:Bundle?=intent.extras
        val email:String?=bundle?.getString("email")
        val provider:String?=bundle?.getString("provider")
        setup(email ?:"",provider ?:"")
        //GUARDADO DE DATOS
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
        crud(email ?:"",provider ?:"")
        boton_3()



    }
    fun boton_dos(){
        button2.setOnClickListener {
            val productos: Intent = Intent(this,Ofertas::class.java)
            startActivity(productos)
        }




    }
    fun boton_3(){
        button3.setOnClickListener{
            val cambiar:Intent=Intent(this,adaptador::class.java)
            startActivity(cambiar)
        }


    }
    private fun setup(email:String,provider: String){
        title = "Inicio"
        emailTextView.text=email
        providerTextView.text= provider
        logOutbutton.setOnClickListener{
            //borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
            Toast.makeText(this, "Vuelva pronto", Toast.LENGTH_LONG).show()
        }




    }
    private fun crud(email: String,provider: String){
        title = "Inicio"
        emailTextView.text=email
        providerTextView.text= provider
        guardar.setOnClickListener {
            if (direccion.length()>=5&&telefono.length()>=6){
            db.collection("users").document(email).set(
                hashMapOf(
                    "direccion" to direccion.text.toString(),
                    "telefono" to telefono.text.toString())
            )
            Toast.makeText(this, "Se ha guardado correctamente", Toast.LENGTH_LONG).show()

        }else{
                Toast.makeText(this, "Llena los campos", Toast.LENGTH_LONG).show()
            }
        }

        eliminar.setOnClickListener {
            db.collection("users").document(email).delete()
            Toast.makeText(this, "Se ha eliminado correctamente", Toast.LENGTH_LONG).show()

        }
        recuperar.setOnClickListener {
            db.collection("users").document(email).get().addOnSuccessListener {
                direccion.setText(it.get("direccion")as String?)
                telefono.setText(it.get("telefono") as String?)
            }
            Toast.makeText(this, "Recuperando datos...", Toast.LENGTH_LONG).show()

        }

    }
}