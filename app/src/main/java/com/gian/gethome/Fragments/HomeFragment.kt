package com.gian.gethome.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.AnimalDetalleActivity
import com.gian.gethome.Adapters.HomeAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class HomeFragment: Fragment(), HomeAdapter.OnItemClickListener {

    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var imagenNotNull:String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter:HomeAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var cardView:CardView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        settingValues(view)
        loadRecycler()
        return view

    }

    private fun loadRecycler() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal = snap.getValue(Animal::class.java)!!
                        imagenNotNull = checkWhatImageIsNotNull(animal)
                        mlist.add(AnimalAdapterData(animal.nombre, animal.tipoAnimal,
                                imagenNotNull,
                                animal.edad, animal.descripcion,
                                animal.transitoUrgente,
                                animal.userIDowner, animal.animalKey))
                    }
                }
                myRecycler.adapter?.notifyDataSetChanged()

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

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HomeAdapter(mlist)
        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        myRecycler.layoutManager = gridLayoutManager
        myRecycler.setHasFixedSize(true)
        myRecycler.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    private fun settingValues(view: View) {
        myRecycler = view.findViewById(R.id.myRecycler)
        cardView = view.findViewById<CardView>(R.id.cardViewHome)
        cardView.setBackgroundResource(R.drawable.card_rounded_design)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Cargando publicaciones")
        progressDialog.setMessage("Por favor, espere")
        progressDialog.setCancelable(false)
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
        startActivity(intent)
    }

}