package com.vicente.tutorialfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","integracion de firebase completa")
        analytics.logEvent("InitScreen", bundle)
        //setup
        setup()
    }
    private fun setup(){
        title = "Autenticación"
        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotBlank() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.Bienvenido)


                    }else{
                        showalert()
                    }
                }
            }
        }
        loginButton.setOnClickListener {
            if (emailEditText.text.isNotBlank() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.Bienvenido)


                    }else{
                        showalert()
                    }
                }
            }
        }
    }
    private  fun  showalert(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private fun showHome (email:String,provider:ProviderType){
        val homeIntent: Intent=Intent(this,homeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider",provider.name)

        }
        startActivity(homeIntent)
    }
}