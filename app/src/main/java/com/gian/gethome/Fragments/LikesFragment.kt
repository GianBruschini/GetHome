package com.gian.gethome.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.animaldetalle.view.AnimalDetalleActivity
import com.gian.gethome.Adapters.HomeAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.Clases.PubsDiLikePOJO
import com.gian.gethome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class LikesFragment: Fragment(), HomeAdapter.OnItemClickListener {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var recyclerLikes:RecyclerView
    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var imagenNotNull:String
    private lateinit var textoEmpty: TextView
    private lateinit var adapter:HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mis_likes, container, false)
        getValues(view)
        setRecyclerView()
        return view

    }

    private fun getValues(view: View) {
        recyclerLikes = view.findViewById(R.id.recyclerview_likes)
        textoEmpty = view.findViewById(R.id.texto_aviso)
    }

    private fun setRecyclerView() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child("Person")
                .child(mFirebaseAuth.currentUser!!.uid)
                .child("PubsDiLike")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        val infoPub: PubsDiLikePOJO = dataSnapshot.getValue(PubsDiLikePOJO::class.java)!!
                        val databaseInner = FirebaseDatabase.getInstance().reference
                                .child("Users")
                                .child("Animales")
                                .child(infoPub.idUserOwner)
                                .child(infoPub.animalKey)
                        databaseInner.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshotInner: DataSnapshot) {
                                    val animal: Animal = snapshotInner.getValue(Animal::class.java)!!
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
                                            animal.provincia))
                                    adapter = HomeAdapter(mlist)
                                    val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                                    recyclerLikes.layoutManager = gridLayoutManager
                                    recyclerLikes.setHasFixedSize(true)
                                    recyclerLikes.adapter = adapter
                                    adapter.setOnItemClickListener(this@LikesFragment)
                            }


                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }


                    recyclerLikes.adapter?.notifyDataSetChanged()
                }else{
                    textoEmpty.visibility = View.VISIBLE
                }

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

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
    override fun onitemClick(position: Int) {
        val intent = Intent(activity, AnimalDetalleActivity::class.java)
        val animalClicked:AnimalAdapterData = mlist[position]
        intent.putExtra("nombre", animalClicked.nombre)
        intent.putExtra("tipoAnimal", animalClicked.tipoAnimal)
        intent.putExtra("descripcion", animalClicked.descripcion)
        intent.putExtra("edad", animalClicked.edad)
        intent.putExtra("transitoUrgente", animalClicked.transitoUrgente)
        intent.putExtra("userIDowner", animalClicked.idOwner)
        intent.putExtra("animalKey", animalClicked.animalKey)
        intent.putExtra("sexoAnimal", animalClicked.sexo)
        intent.putExtra("Provincia", animalClicked.provincia)
        intent.putExtra("Pais", animalClicked.pais)

        startActivity(intent)
    }


}