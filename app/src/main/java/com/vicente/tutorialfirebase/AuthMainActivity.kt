package com.vicente.tutorialfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*

class AuthMainActivity : AppCompatActivity() {
    private  val Google_sing_in =100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","integracion de firebase completa")
        analytics.logEvent("InitScreen", bundle)
        //setup
        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        authlayout.visibility = View.VISIBLE
    }
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email =prefs.getString("email", null)
        val provider =prefs.getString("provider", null)
        if (email != null && provider != null){
            authlayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }
    private fun setup(){
        title = ""
        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotBlank() && passwordEditText.text.isNotEmpty()&&passwordEditText.length()>7){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.Bienvenido)


                    }else{

                        showalert3()
                        Toast.makeText(this, "Ya hay un usario registrado con este correo", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this, "Ingrese un correo válido y una contraseña de almenos 8 digitos", Toast.LENGTH_LONG).show()
            }
        }
        loginButton.setOnClickListener {
            if (emailEditText.text.isNotBlank() && passwordEditText.text.isNotEmpty()&&passwordEditText.length()>7){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.Bienvenido)
                        Toast.makeText(this, "Ingresando", Toast.LENGTH_SHORT).show()



                    }else{
                        showalert2()
                        Toast.makeText(this, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
            }else
            {
                Toast.makeText(this, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
        googlebutton.setOnClickListener {
            //configuración
            val googleConf =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleclient=GoogleSignIn.getClient(this, googleConf)
            googleclient.signOut()
            startActivityForResult(googleclient.signInIntent,Google_sing_in)
        }
    }
    private  fun  showalert(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage("Se ha producido un error, verifique que su correo sea valido como 'example@example.com'")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private  fun  showalert1(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error de google")
        builder.setMessage("Se ha producido un error de google, verifique su conexion")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private  fun  showalert2(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error de autenticación")
        builder.setMessage("Esta cuenta no es tuya, o te equivocaste de contraseña, vuelve a intentar")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private  fun  showalert3(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error de autenticación")
        builder.setMessage("Ya hay alguien registrado con este correo, intenta con uno nuevo :) ")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Google_sing_in){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account =task.getResult(ApiException::class.java)
                if (account != null){
                    val credential=GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            showHome(account.email ?: "",ProviderType.Bienvenido)
                        }else{
                            showalert1()
                        }
                    }
                }
            }catch (e: ApiException){
                showalert1()
            }


        }
    }
}