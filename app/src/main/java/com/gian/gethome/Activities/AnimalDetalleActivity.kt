package com.gian.gethome.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gian.gethome.Adapters.SliderPagerAdapter
import com.gian.gethome.Adapters.SliderPagerAdapterAnimal
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.SlideFirstScreen
import com.gian.gethome.Clases.SliderAnimalDetailScreen
import com.gian.gethome.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animal_detalle.*
import java.text.SimpleDateFormat
import java.util.*

class AnimalDetalleActivity : AppCompatActivity() {
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
    private lateinit var mFirebaseAuth:FirebaseAuth
    private var animalImages:MutableList<String> = mutableListOf()
    private val imagesViewPagerList:MutableList<Int> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detalle)
        getIntentValues()
        loadImagesOfTheAnimal()
    }

    private fun loadImagesOfTheAnimal() {
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mFirebaseAuth = FirebaseAuth.getInstance()
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal? = snap.getValue(Animal::class.java)
                        if(animal?.animalKey == animalKey){
                            checkWhatImageIsNotNull(animal)
                        }
                    }
                }
                //Por ahi crashee cuando no haya ningun animal publicado... despues testear esto.
            }

            private fun checkWhatImageIsNotNull(animal: Animal) {
                animalImages.add(animal.imagen1)
                animalImages.add(animal.imagen2)
                animalImages.add(animal.imagen3)
                animalImages.add(animal.imagen4)
                for(pos in animalImages.indices){
                    if(animalImages[pos] != "null"){
                            settingViewPager(animalImages[pos])
                    }
                }
                val adapter = SliderPagerAdapterAnimal(applicationContext, lstSlides)
                sliderPager.adapter = adapter
                indicator.setupWithViewPager(sliderPager, true)

            }

            override fun onCancelled(error: DatabaseError) {}
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
        setUIvalues()
    }

    private fun setUIvalues() {
        nombreAnimalTxt.text = nombreAnimal
        edadAnimalTxt.text = edadAnimal
        descripcionAnimalTxt.text = descripcionAnimal
        when(transitoUrgente){
            "true" -> transitoUrgenteTxt.text = "Con tránsito urgente"
            "false" -> transitoUrgenteTxt.text = "Sin tránsito urgente"
        }
        when(sexoAnimal){
            "Macho" -> Picasso.get().load(R.drawable.male).into(sexoAnimalImage)
            "Hembra" -> Picasso.get().load(R.drawable.female).into(sexoAnimalImage)
        }
        setLocalDate() //Este ultimo en realidad iria en PublicarAnimalFragment

    }

    @SuppressLint("SimpleDateFormat")
    private fun setLocalDate() {
        val df = SimpleDateFormat("dd-MM-yyyy")
        val formatted = df.format(Date())
        fechaPublicacion.text=formatted
    }


    /*
      private fun loadValues() {
          val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
          database.addValueEventListener(object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                  mFirebaseAuth = FirebaseAuth.getInstance()

                  for (dataSnapshot in snapshot.children) {
                      for (snap in dataSnapshot.children) {
                          val animal: Animal? = snap.getValue(Animal::class.java)
                          if(animal?.animalKey == animalKey){
                              checkWhatImageIsNotNull(animal)
                          }

                      }
                  }
                  //Por ahi crashee cuando no haya ningun animal publicado... despues testear esto.
              }

              private fun checkWhatImageIsNotNull(animal: Animal) {
                  animalImages.add(animal.imagen1)
                  animalImages.add(animal.imagen2)
                  animalImages.add(animal.imagen3)
                  animalImages.add(animal.imagen4)
                  for(pos in animalImages.indices){
                      if(animalImages[pos] != "null"){
                          println("Es distinto de null")
                      }else{
                          println("Es null")
                      }
                  }


              }

              override fun onCancelled(error: DatabaseError) {}
          })
      }

       */



    fun likeAnimal(view: View) {}
    fun backArrowButton(view: View) {
        finish()
    }
}