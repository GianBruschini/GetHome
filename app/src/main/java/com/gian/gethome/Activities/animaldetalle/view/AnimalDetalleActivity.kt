package com.gian.gethome.Activities.animaldetalle.view

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gian.gethome.Activities.animaldetalle.interfaces.AnimalDetalleView
import com.gian.gethome.Activities.animaldetalle.model.AnimalDetalleInteractor
import com.gian.gethome.Activities.animaldetalle.presenter.AnimalDetallePresenter
import com.gian.gethome.Activities.contactinfo.view.ContactInfoActivity
import com.gian.gethome.Adapters.SliderPagerAdapterAnimal
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.SliderAnimalDetailScreen
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animal_detalle.*
import java.util.*


class AnimalDetalleActivity : AppCompatActivity(),AnimalDetalleView {
    private lateinit var sliderPager: ViewPager
    private lateinit var indicator: TabLayout
    private val lstSlides:MutableList<SliderAnimalDetailScreen> = mutableListOf()
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private lateinit var transitoUrgente: String
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var userIDownerAnimal:String
    private lateinit var animalKey:String
    private lateinit var provincia:String
    private lateinit var pais:String
    private lateinit var toggleButtonFav:ToggleButton

    private val presenter = AnimalDetallePresenter(this, AnimalDetalleInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detalle)
        descripcionAnimalTxt.movementMethod = ScrollingMovementMethod()
        getIntentValues()
        setUIvalues()
        presenter.checkFavouritesInDB(animalKey)
        setLogicToToggleButtonFav()
        presenter.setFotoYnombrePerfil(userIDownerAnimal)
        presenter.loadImagesAnimal(animalKey)
        presenter.detectSexoAnimal(sexoAnimal)
        presenter.detectTransitoUrgente(transitoUrgente)
    }

    private fun setLogicToToggleButtonFav() {
        toggleButtonFav.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                presenter.saveLikes(animalKey,userIDownerAnimal)
            } else {
                presenter.deleteLike(animalKey)
            }
        })
    }

    private fun settingViewPager(linkImage: String) {
        sliderPager = findViewById(R.id.imagenAnimalViewPager)
        indicator = findViewById(R.id.indicator)
        lstSlides.add(SliderAnimalDetailScreen(linkImage, null))

    }

   private fun getIntentValues() {
        nombreAnimal = intent.getStringExtra("nombre").toString()
        tipoAnimal = intent.getStringExtra("tipoAnimal").toString()
        descripcionAnimal = intent.getStringExtra("descripcion").toString()
        edadAnimal = intent.getStringExtra("edad").toString()
        transitoUrgente = intent.getStringExtra("transitoUrgente").toString()
        userIDownerAnimal = intent.getStringExtra("userIDowner").toString()
        animalKey = intent.getStringExtra("animalKey").toString()
        sexoAnimal = intent.getStringExtra("sexoAnimal").toString()
        provincia = intent.getStringExtra("Provincia").toString()
        pais = intent.getStringExtra("Pais").toString()
        toggleButtonFav = findViewById(R.id.addToFav)

    }

    private fun setUIvalues() {
        nombreAnimalTxt.text = nombreAnimal
        edadAnimalTxt.text = edadAnimal
        descripcionAnimalTxt.text = descripcionAnimal
        distanceTextCard.text = provincia + pais
    }

    fun backArrowButton(view: View) {
        finish()
    }

    fun goToContactInfo(view: View) {
            val intent = Intent(this, ContactInfoActivity::class.java)
            intent.putExtra("idOwner", userIDownerAnimal)
            intent.putExtra("animalKey", animalKey)
            startActivity(intent)
    }

    override fun databaseError() {
        Toast.makeText(this,
                "Error con el servicio, vuelva a intentarlo más tarde",
                Toast.LENGTH_SHORT).show()
    }

    override fun setFavButton() {
        toggleButtonFav.isChecked = true
    }

    override fun setNombreYfoto(userInfo: UserInfo) {
        Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.progress_animation).into(profileImage)
        nameOwner.text = userInfo.userName
    }

    override fun loadImages(animalImages: MutableList<String>) {
        for (pos in animalImages.indices) {
            if (animalImages[pos] != "null") {
                settingViewPager(animalImages[pos])
            }
        }
        val adapter = SliderPagerAdapterAnimal(applicationContext, lstSlides)
        sliderPager.adapter = adapter
        indicator.setupWithViewPager(sliderPager, true)
    }

    override fun setSexo(sexo:Int) {
        Picasso.get().load(sexo).into(sexoAnimalImage)
    }

    override fun setTransitoUrgente(transito:String) {
        transitoUrgenteTxt.text = transito
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()

    }
}