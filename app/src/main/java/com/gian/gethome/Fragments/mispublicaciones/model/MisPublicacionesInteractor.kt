package com.gian.gethome.Fragments.mispublicaciones.model

import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class MisPublicacionesInteractor {
    private lateinit var imagenNotNull:String
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var listener:onMisPublicacionesListener
    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()

    interface onMisPublicacionesListener {
        fun onPassAnimalData(animal: Animal, imagenNotNull: String)
        fun onNotifyDataChanged()
        fun onSetTextoEmptyVisible()
        fun onDataBaseError()
        fun onNotifyPubDeleted(position: Int)
        fun passAnimalAt(animalAdapterData: AnimalAdapterData)




    }


    fun getPubData(listener: onMisPublicacionesListener) {
        this.listener = listener
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child("Animales")
                .child(mFirebaseAuth.currentUser!!.uid)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val animal: Animal = dataSnapshot.getValue(Animal::class.java)!!
                        println("El nombre del animal es " + " " + animal.nombre)
                        imagenNotNull = checkWhatImageIsNotNull(animal)
                        mlist.add(AnimalAdapterData(animal.nombre,
                                animal.tipoAnimal,
                                imagenNotNull,
                                animal.edad,
                                animal.fechaDePublicacion,
                                animal.descripcion,
                                animal.transitoUrgente,
                                animal.userIDowner,
                                animal.animalKey,
                                animal.sexo,
                                animal.pais,
                                animal.provincia,animal.cantAnimales))
                        listener.onPassAnimalData(animal, imagenNotNull)

                    }
                    listener.onNotifyDataChanged()
                } else {
                    listener.onSetTextoEmptyVisible()
                }

            }


            override fun onCancelled(error: DatabaseError) {
                listener.onDataBaseError()
            }
        })
    }

    private fun checkWhatImageIsNotNull(animal: Animal): String {
        if (animal.imagen1 != "null") {
            return animal.imagen1
        }
        if (animal.imagen2 != "null") {
            return animal.imagen2
        }
        if (animal.imagen3 != "null") {
            return animal.imagen3
        }
        if (animal.imagen4 != "null") {
            return animal.imagen4
        }
        return ""
    }

    fun deleteAnimalFromDB(position: Int) {
        FirebaseDatabase.getInstance().reference.
        child("Users").
        child("Animales").
        child(mFirebaseAuth.currentUser!!.uid).child(mlist[position].animalKey).removeValue()
        listener.onNotifyPubDeleted(position)

    }

    fun searchAnimalAt(position: Int) {
        listener.passAnimalAt(mlist[position])
    }

}