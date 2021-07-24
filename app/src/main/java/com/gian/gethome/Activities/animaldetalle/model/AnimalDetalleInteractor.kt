package com.gian.gethome.Activities.animaldetalle.model

import android.graphics.drawable.Drawable
import com.gian.gethome.Activities.animaldetalle.presenter.AnimalDetallePresenter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animal_detalle.*

class AnimalDetalleInteractor {
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUserkey:String
    private var animalImages:MutableList<String> = mutableListOf()

    interface onAnimalDetalleListener{
        fun onDatabaseError()
        fun onSetLikeButton()
        fun onSetFotoYnombre(userInfo: UserInfo)
        fun onLoadImages(animalImages:MutableList<String>)
        fun onTransitoUrgente(transitoUrgente:String)
        fun onDetectSexo(sexo:Int)

    }


    fun checkFavouritesInDB(animalKey: String, listener: onAnimalDetalleListener) {
        val reference = FirebaseDatabase.getInstance().reference
        val applesQuery = reference.child("Users").child("Person").
        child(mFirebaseAuth.currentUser!!.uid).
        child("PubsDiLike").
        orderByChild("animalKey").
        equalTo(animalKey)
        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    if (appleSnapshot.exists()) {
                        listener.onSetLikeButton()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


    fun saveLikes(animalKey: String, userIDownerAnimal: String) {
        val currentUserID = mFirebaseAuth.currentUser!!.uid
        val currentUserkey = FirebaseDatabase.getInstance().getReference("Person").push().key.toString()
        val myKeyDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey).child("myKey")
        val animalKeyDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey).child("animalKey")
        val userOwnerPubDB = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(currentUserID).child("PubsDiLike").child(currentUserkey).child("idUserOwner")
        animalKeyDB.setValue(animalKey)
        myKeyDB.setValue(currentUserkey.toString())
        userOwnerPubDB.setValue(userIDownerAnimal)
    }

    fun deleteLike(animalKey: String) {
        val ref = FirebaseDatabase.getInstance().reference
        val applesQuery: Query = ref.child("Users").
        child("Person").
        child(mFirebaseAuth.currentUser!!.uid).
        child("PubsDiLike").
        orderByChild("animalKey").
        equalTo(animalKey)
        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    fun setFotoPerfilYNombredePerfil(listener: onAnimalDetalleListener,userIDownerAnimal: String) {

        val profilePicture = FirebaseDatabase.getInstance().reference.child("Users").
        child("Person").
        child(userIDownerAnimal)
        profilePicture.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userInfo: UserInfo = dataSnapshot.getValue(UserInfo::class.java)!!
                listener.onSetFotoYnombre(userInfo)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onDatabaseError()
            }
        })
    }


    fun loadImagesOfTheAnimal(animalKey: String,listener: onAnimalDetalleListener) {
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal? = snap.getValue(Animal::class.java)
                        if (animal?.animalKey == animalKey) {
                            checkWhatImageIsNotNull(animal,listener)
                        }
                    }
                }
                //Por ahi crashee cuando no haya ningun animal publicado... despues testear esto.
            }

            private fun checkWhatImageIsNotNull(animal: Animal, listener: onAnimalDetalleListener) {
                animalImages.add(animal.imagen1)
                animalImages.add(animal.imagen2)
                animalImages.add(animal.imagen3)
                animalImages.add(animal.imagen4)
                listener.onLoadImages(animalImages)

            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }


    fun detectTransitoUrgente(transitoUrgente: String,listener: onAnimalDetalleListener){
        when(transitoUrgente){
            "true" ->  listener.onTransitoUrgente("Con tránsito urgente")
            "false" -> listener.onTransitoUrgente("Sin tránsito urgente")
        }

    }
    fun detectSexoAnimal(sexo:String,listener: AnimalDetallePresenter){
        when(sexo){
            "Macho" -> listener.onDetectSexo(R.drawable.male)
            "Hembra" -> listener.onDetectSexo(R.drawable.female)
        }
    }


}