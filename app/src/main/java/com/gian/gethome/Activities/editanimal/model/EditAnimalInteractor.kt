package com.gian.gethome.Activities.editanimal.model

import com.gian.gethome.Clases.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class EditAnimalInteractor {
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var listener:onEditAnimalInteractor


    interface onEditAnimalInteractor{
        fun onDataBaseError()
        fun passAnimalData(animal: Animal)
        fun onAnimalUpdated()
        fun onNavigateToHome()
    }
    fun retrieveDataFromDB(listener: onEditAnimalInteractor, animalKey: String) {
        this.listener = listener
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child("Animales")
                .child(mFirebaseAuth.currentUser!!.uid)
                .child(animalKey)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animal: Animal = snapshot.getValue(Animal::class.java)!!
                listener.passAnimalData(animal)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onDataBaseError()
            }
        })
    }

    fun editDataAnimal(tipoAnimal: String, nombre: String,
                       cantAnimales: String, transito: String,
                       edad: String, descripcion: String,
                       whatsapp: String, phone: String,
                       mail: String, instagram: String, animalKey: String, sexo: String) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val editAnimal: HashMap<String, Any> = HashMap()
        editAnimal["nombre"] =nombre
        editAnimal["tipoAnimal"] = tipoAnimal
        editAnimal["transitoUrgente"] = transito
        editAnimal["edad"] = edad
        editAnimal["descripcion"] = descripcion
        editAnimal["sexo"] = sexo
        editAnimal["whatsapp"] = whatsapp
        editAnimal["phone"] = phone
        editAnimal["mail"] = mail
        editAnimal["instagram"] = instagram
        editAnimal["cantAnimales"] = cantAnimales
        FirebaseDatabase.getInstance().reference.
        child("Users").
        child("Animales").
        child(mFirebaseAuth.currentUser!!.uid).
        child(animalKey).updateChildren(editAnimal)
        listener.onAnimalUpdated()
    }

    fun deleteAnimalFromDB(animalKey: String, animalUrlImage: String) {
        val mFirebaseStorage = FirebaseStorage.getInstance()
        FirebaseDatabase.getInstance().reference.
        child("Users").
        child("Animales").
        child(mFirebaseAuth.currentUser!!.uid).child(animalKey).removeValue()
        val photoRef: StorageReference = mFirebaseStorage.getReferenceFromUrl(animalUrlImage)
        photoRef.delete()
        listener.onNavigateToHome()
    }
}