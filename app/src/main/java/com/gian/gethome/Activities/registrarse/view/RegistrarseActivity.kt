package com.gian.gethome.Activities.registrarse.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.elegirfotodeperfil.view.ElegirFotoDePerfilActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityRegistrarseBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registrarse.*

class RegistrarseActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegistrarseBinding
    private var mFirebaseAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSomeValues()
        cardView.setBackgroundResource(R.drawable.card_rounded_design)
        botonContinuar.setOnClickListener {
            pasarDatosEiniciarActivity()
        }
    }

    private fun initializeSomeValues() {
        mFirebaseAuth = FirebaseAuth.getInstance()

    }

    private fun pasarDatosEiniciarActivity() {
        val intent = Intent(this, ElegirFotoDePerfilActivity::class.java)
        intent.putExtra("facebook", binding.facebook.editText?.text.toString())
        intent.putExtra("twitter", binding.twitter.editText?.text.toString())
        intent.putExtra("instagram", binding.instagram.editText?.text.toString())
        intent.putExtra("sitioWeb", binding.paginaWeb.editText?.text.toString())
        startActivity(intent)
    }



}