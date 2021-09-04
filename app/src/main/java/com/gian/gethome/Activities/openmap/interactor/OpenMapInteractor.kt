package com.gian.gethome.Activities.openmap.interactor

import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.openmap.Model.Coordinates
import com.gian.gethome.Adapters.LocationInfoAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OpenMapInteractor {
    private lateinit var listener:onOpenMapInteractorListener
    private lateinit var mFirebaseAuth:FirebaseAuth
    private var listOfCoordinates: MutableList<Coordinates> = mutableListOf()
    private lateinit var animal:Animal
    private var mlistOfAnimals: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var imagenNotNull:String
    private lateinit var distance:String

    interface onOpenMapInteractorListener{
        fun onDataBaseError()
        fun onPassListOfCoordinates(listOfCoordinates: MutableList<Coordinates>)
        fun onPassListOfAnimals(mlistOfAnimals: ArrayList<AnimalAdapterData>)
    }


    fun retrieveDataOfLocations(listener: onOpenMapInteractorListener) {
        this.listener = listener
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    var cantIteraciones: Int = 0
                    for (snap in dataSnapshot.children) {
                        cantIteraciones ++
                        if(cantIteraciones == 1){
                            animal= snap.getValue(Animal::class.java)!!
                            listOfCoordinates.add(Coordinates(animal.latitude,animal.longitude,animal.userIDowner))
                        }
                    }
                }
                listener.onPassListOfCoordinates(listOfCoordinates)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onDataBaseError()
            }
        })

    }

    fun retrieveDataFromDB(idUserOwner: String?, myCurrentLatitude: String, myCurrentLongitude: String) {
        mlistOfAnimals.clear()
        val database = FirebaseDatabase.getInstance().reference.child("Users").
        child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        animal= snap.getValue(Animal::class.java)!!
                        if(animal.userIDowner == idUserOwner){
                            imagenNotNull = checkWhatImageIsNotNull(animal)
                            distance = calculateDistance(
                                    myCurrentLatitude.toDouble(),
                                    myCurrentLongitude.toDouble()
                                    ,animal.latitude.toDouble(),
                                    animal.longitude.toDouble()).toString()

                            mlistOfAnimals.add(AnimalAdapterData(
                                    animal.nombre,
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
                                    animal.provincia, animal.cantAnimales, distance))
                        }
                    }
                }
                listener.onPassListOfAnimals(mlistOfAnimals)
            }

            fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
                val theta = lon1 - lon2
                var dist = (Math.sin(deg2rad(lat1))
                        * Math.sin(deg2rad(lat2))
                        + (Math.cos(deg2rad(lat1))
                        * Math.cos(deg2rad(lat2))
                        * Math.cos(deg2rad(theta))))
                dist = Math.acos(dist)
                dist = rad2deg(dist)
                dist = dist * 60 * 1.1515
                return dist
            }

            private fun deg2rad(deg: Double): Double {
                return deg * Math.PI / 180.0
            }

            private fun rad2deg(rad: Double): Double {
                return rad * 180.0 / Math.PI
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

            override fun onCancelled(error: DatabaseError) {
                listener.onDataBaseError()
            }
        })

    }
}