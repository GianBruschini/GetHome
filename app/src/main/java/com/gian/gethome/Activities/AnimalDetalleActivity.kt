package com.gian.gethome.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
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
    private lateinit var imagenPerfil: String
    private var animalImages:MutableList<String> = mutableListOf()
    private val imagesViewPagerList:MutableList<Int> = mutableListOf()
    private lateinit var provincia:String
    private lateinit var pais:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detalle)
        getIntentValues()
        checkIfIlikePub()
        checkIfPubItsMine()
        setFotoPerfilYNombredePerfil()
        loadImagesOfTheAnimal()
    }

    private fun checkIfPubItsMine() {
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mFirebaseAuth = FirebaseAuth.getInstance()
                val currentUserID = mFirebaseAuth.currentUser!!.uid
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal = snap.getValue(Animal::class.java)
                        if ((nombreAnimal == animal!!.nombre) and (animal.userIDowner == currentUserID)) {
                            //buttonLike.visibility = View.GONE
                            //addToFav.visibility = View.GONE
                            //cardAddToFav.visibility = View.GONE
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setFotoPerfilYNombredePerfil() {
            mFirebaseAuth = FirebaseAuth.getInstance()
            val profilePicture = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(userIDownerAnimal)
            profilePicture.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userInfo: UserInfo = dataSnapshot.getValue(UserInfo::class.java)!!
                    Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.progress_animation).fit().into(profileImage)
                    nameOwner.text = userInfo.userName
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun loadImagesOfTheAnimal() {
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mFirebaseAuth = FirebaseAuth.getInstance()
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal? = snap.getValue(Animal::class.java)
                        if (animal?.animalKey == animalKey) {
                            checkWhatImageIsNotNull(animal)
                            fechaPublicacion.text = animal.fechaDePublicacion
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
                for (pos in animalImages.indices) {
                    if (animalImages[pos] != "null") {
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
        provincia = intent.getStringExtra("Provincia").toString()
        pais = intent.getStringExtra("Pais").toString()
        setUIvalues()
    }

    private fun setUIvalues() {
        nombreAnimalTxt.text = nombreAnimal
        edadAnimalTxt.text = edadAnimal
        descripcionAnimalTxt.text = descripcionAnimal
        distanceTextCard.text = provincia + pais
        when(transitoUrgente){
            "true" -> transitoUrgenteTxt.text = "Con tránsito urgente"
            "false" -> transitoUrgenteTxt.text = "Sin tránsito urgente"
        }
        when(sexoAnimal){
            "Macho" -> Picasso.get().load(R.drawable.male).into(sexoAnimalImage)
            "Hembra" -> Picasso.get().load(R.drawable.female).into(sexoAnimalImage)
        }


    }



    fun likeAnimal(view: View) {
        saveLikes();
    }

    private fun saveLikes() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUserID = mFirebaseAuth.currentUser!!.uid
        val key = FirebaseDatabase.getInstance().getReference("Person").push().key
        val idLikeRef = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(userIDownerAnimal).child("OthersLikes").child(key!!).child("idDioLike")
        val myKey = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(userIDownerAnimal).child("OthersLikes").child(key).child("myKey")
        idLikeRef.setValue(currentUserID)
        myKey.setValue(key.toString())

        val currentUserkey = FirebaseDatabase.getInstance().getReference("Person").push().key
        val myKeyDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey!!).child("myKey")
        val animalKeyDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey).child("animalKey")
        val userOwnerPubDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey).child("idUserOwner")
        animalKeyDB.setValue(animalKey)
        myKeyDB.setValue(currentUserkey.toString())
        userOwnerPubDB.setValue(userIDownerAnimal)
    }

    private fun checkIfIlikePub() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUserID = mFirebaseAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("PubsDiLike")) {
                    println("Entre aca 1")
                    val currentUserID = mFirebaseAuth.currentUser!!.uid
                    val databaseREF = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike")
                    databaseREF.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            println("Entre aca 1.8")
                            for (dataSnapshot in snapshot.children) {
                                val animal = dataSnapshot.getValue(Animal::class.java)
                                if (animal!!.animalKey == animalKey) {
                                    println("Entre aca 2")
                                    buttonLike.setOnClickListener {
                                        Toast.makeText(this@AnimalDetalleActivity,"Ya le has dado me gusta",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                } else {
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    fun backArrowButton(view: View) {
        finish()

    }

    fun goToContactInfo(view: View) {
            val intent = Intent(this,ContactInfoActivity::class.java)
            intent.putExtra("idOwner",userIDownerAnimal)
            intent.putExtra("animalKey",animalKey)
            startActivity(intent)
    }
}