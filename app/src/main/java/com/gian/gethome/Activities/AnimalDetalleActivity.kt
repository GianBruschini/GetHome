package com.gian.gethome.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gian.gethome.Adapters.SliderPagerAdapter
import com.gian.gethome.Clases.SlideFirstScreen
import com.gian.gethome.R
import com.google.android.material.tabs.TabLayout
import java.util.*

class AnimalDetalleActivity : AppCompatActivity() {
    private lateinit var sliderPager: ViewPager
    private lateinit var indicator: TabLayout
    private val lstSlides:MutableList<SlideFirstScreen> = mutableListOf()
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private lateinit var transitoUrgente: String
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String

    private val imagesViewPagerList:MutableList<Int> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detalle)
        settingViewPager()

       // getIntentValues()
       // loadValues()
    }

    private fun settingViewPager() {
        sliderPager = findViewById(R.id.imagenAnimalViewPager)
        indicator = findViewById(R.id.indicator)
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        lstSlides.add(SlideFirstScreen(R.drawable.dog, null))
        val adapter = SliderPagerAdapter(applicationContext, lstSlides)
        sliderPager.adapter = adapter
        indicator.setupWithViewPager(sliderPager, true)

    }




   /* private fun getIntentValues() {
        nombreAnimal = intent.getStringExtra("nombre").toString()
        tipoAnimal = intent.getStringExtra("tipoAnimal").toString()
        descripcionAnimal = intent.getStringExtra("descripcion").toString()
        edadAnimal = intent.getStringExtra("edad").toString()
        transitoUrgente = intent.getStringExtra("transitoUrgente").toString()
        userIDownerAnimal = intent.getStringExtra("userIDowner").toString()
        animalKey = intent.getStringExtra("animalKey").toString()
    }



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
    fun backArrowButton(view: View) {}
}