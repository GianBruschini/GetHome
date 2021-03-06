package com.gian.gethome.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.animaldetalle.view.AnimalDetalleActivity
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Activities.openmap.view.OpenMapActivity
import com.gian.gethome.Adapters.HomeAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_maps.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class HomeFragment: Fragment(), HomeAdapter.OnItemClickListener, View.OnClickListener{

    private lateinit var pais: String
    private lateinit var provincia: String
    private lateinit var ordenarPorGenero: String
    private var ordenarPorFechaDePublicacion: Boolean? = false
    private var ordenarPorTransitoUrgente: Boolean? = false
    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var imagenNotNull:String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter:HomeAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var cardView:CardView
    private lateinit var gatoImg:ImageView
    private lateinit var perroImg:ImageView
    private lateinit var loroImg:ImageView
    private lateinit var conejoImg:ImageView
    private lateinit var tortugaImg:ImageView
    private lateinit var todoImg:ImageView
    private lateinit var roedorImg:ImageView
    private lateinit var aveImg:ImageView
    private lateinit var texto_resultado:TextView
    private lateinit var buscador:EditText
    private lateinit var homeActivity: HomeActivity
    private lateinit var latitude:String
    private lateinit var longitude:String
    private var distance by Delegates.notNull<Double>()
    private var size by Delegates.notNull<Int>()
    private var listEmpty:MutableList<AnimalAdapterData> = mutableListOf()
    private lateinit var progressBar:ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getHomeActivityBundle()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        cardView = view.findViewById(R.id.cardViewHome)
        getCountryAndProvinceFromHomeActivity()
        settingValues(view)
        makeActionEditText()
        setAllAnimalsSelected(view)
        return view
    }

    private fun getHomeActivityBundle() {
        //longitude = arguments?.getString("longitude").toString()
        //latitude = arguments?.getString("latitude").toString()

    }

    private fun setAllAnimalsSelected(view: View) {
        val cardAll: CardView = view.findViewById(R.id.cardAll)
        cardAll.setCardBackgroundColor(Color.parseColor("#306060"))
        todoImg.setColorFilter(Color.parseColor("#ffffff"))
        loadRecycler()
    }

    private fun getCountryAndProvinceFromHomeActivity() {
        homeActivity = (activity as HomeActivity?)!!
        provincia = homeActivity.provincia.text.toString()
        pais = homeActivity.pais.text.toString()
        latitude = homeActivity.latitudeTxt.text.toString()
        longitude = homeActivity.longitudeTxt.text.toString()
    }

    private fun loadRecycler() {
        texto_resultado.visibility = View.GONE
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal = snap.getValue(Animal::class.java)!!
                        if (animal.provincia == provincia && animal.pais == pais) {
                            imagenNotNull = checkWhatImageIsNotNull(animal)

                            adapter = HomeAdapter(mlist)
                            distance = adapter.distance(latitude.toDouble(),
                                    longitude.toDouble(), animal.latitude.toDouble(),
                                    animal.longitude.toDouble())
                            mlist.add(AnimalAdapterData(
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
                                    animal.provincia, animal.cantAnimales, distance.toString()))
                            val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                            myRecycler.layoutManager = gridLayoutManager
                            myRecycler.setHasFixedSize(true)
                            myRecycler.adapter = adapter
                            progressBar.visibility = View.GONE
                            adapter.setOnItemClickListener(this@HomeFragment)
                        }

                    }
                }
                myRecycler.adapter?.notifyDataSetChanged()
                if (mlist.size == 0) {
                    texto_resultado.visibility = View.VISIBLE
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

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun settingValues(view: View) {
        val openMap:FloatingActionButton = view.findViewById(R.id.openMap)
        openMap.setOnClickListener {
            val intent = Intent(context, OpenMapActivity::class.java)
            intent.putExtra("currentLatitude", latitude)
            intent.putExtra("currentLongitude", longitude)
            startActivity(intent)
        }
        myRecycler = view.findViewById(R.id.myRecycler)
        texto_resultado = view.findViewById(R.id.texto_resultados)
        buscador = view.findViewById(R.id.buscador)
        cardView = view.findViewById(R.id.cardViewHome)
        cardView.setBackgroundResource(R.drawable.card_rounded_design)
        gatoImg = view.findViewById(R.id.gato)
        perroImg = view.findViewById(R.id.perro)
        conejoImg = view.findViewById(R.id.conejo)
        loroImg = view.findViewById(R.id.loro)
        tortugaImg = view.findViewById(R.id.tortuga)
        todoImg = view.findViewById(R.id.all)
        aveImg = view.findViewById(R.id.bird)
        roedorImg = view.findViewById(R.id.mouse)
        progressBar = view.findViewById(R.id.progressbar)
        gatoImg.setOnClickListener(this)
        perroImg.setOnClickListener(this)
        loroImg.setOnClickListener(this)
        conejoImg.setOnClickListener(this)
        tortugaImg.setOnClickListener(this)
        aveImg.setOnClickListener(this)
        roedorImg.setOnClickListener(this)
        todoImg.setOnClickListener(this)
        progressBar.visibility = View.VISIBLE
        setOnClickImages()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            if(data!=null){
                ordenarPorTransitoUrgente = data.getBooleanExtra("filtrarPorTransito", false)!!
                val genero = data.getStringExtra("filtrarPorGenero");
                if(genero != "G??nero animal"){
                    ordenarPorGenero = genero.toString()
                }
            }
        }
    }

    private fun setOnClickImages() {
        gatoImg.setOnClickListener {
            clearListOfRecycler()
            setGatoImgselected()
            fillScreenWith("Gato")
        }
        perroImg.setOnClickListener {
            clearListOfRecycler()
            setPerroImgselected()
            fillScreenWith("Perro")
        }
        conejoImg.setOnClickListener {
            clearListOfRecycler()
            setConejoImgselected()
            fillScreenWith("Conejo")
        }
        tortugaImg.setOnClickListener {
            clearListOfRecycler()
            setTortugaImgselected()
            fillScreenWith("Tortuga")
        }
        loroImg.setOnClickListener {
            clearListOfRecycler()
            setLoroImgselected()
            fillScreenWith("Loro")
        }
        aveImg.setOnClickListener {
            clearListOfRecycler()
            setAveImgselected()
            fillScreenWith("Ave")
        }
        roedorImg.setOnClickListener {
            clearListOfRecycler()
            setRoedorImgselected()
            fillScreenWith("Roedor")
        }
        todoImg.setOnClickListener {
            clearListOfRecycler()
            setTodoImgselected()
            loadRecycler()
        }
    }



    private fun clearListOfRecycler() {
        size= mlist.size
        if (size > 0) {
            for (i in 0 until size) {
                mlist.removeAt(0)
            }
            myRecycler.adapter!!.notifyItemRangeRemoved(0, size)
        }
        //adapter.clearList()
        //myRecycler.adapter!!.notifyDataSetChanged()
    }

    private fun fillScreenWith(theTypeOfAnimal: String) {
        //adapter.clearList()
        texto_resultado.visibility = View.GONE
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val tipoAnimal: String = snap.child("tipoAnimal").value.toString();
                        if (tipoAnimal == theTypeOfAnimal) {
                            val animal: Animal = snap.getValue(Animal::class.java)!!
                            if (animal.provincia == provincia && animal.pais == pais) {

                                imagenNotNull = checkWhatImageIsNotNull(animal)
                                if (imagenNotNull != "") {
                                    adapter = HomeAdapter(mlist)
                                    distance = adapter.distance(latitude.toDouble(),
                                            longitude.toDouble(), animal.latitude.toDouble(),
                                            animal.longitude.toDouble())
                                    mlist.add(AnimalAdapterData(animal.nombre, animal.tipoAnimal,
                                            imagenNotNull,
                                            animal.edad, animal.fechaDePublicacion, animal.descripcion,
                                            animal.transitoUrgente,
                                            animal.userIDowner, animal.animalKey, animal.sexo,
                                            animal.pais, animal.provincia, animal.cantAnimales,
                                            distance.toString()))

                                    val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                                    myRecycler.layoutManager = gridLayoutManager
                                    myRecycler.setHasFixedSize(true)
                                    myRecycler.adapter = adapter

                                    adapter.setOnItemClickListener(this@HomeFragment)
                                }

                            }


                        }

                    }
                }

                myRecycler.adapter?.notifyDataSetChanged()
                if (mlist.size == 0) {
                    texto_resultado.visibility = View.VISIBLE
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

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setGatoImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#306060"))
        gatoImg.setColorFilter(Color.parseColor("#ffffff"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setPerroImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#306060"))
        perroImg.setColorFilter(Color.parseColor("#ffffff"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setConejoImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#306060"))
        conejoImg.setColorFilter(Color.parseColor("#ffffff"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setLoroImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#306060"))
        loroImg.setColorFilter(Color.parseColor("#ffffff"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setTortugaImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#306060"))
        tortugaImg.setColorFilter(Color.parseColor("#ffffff"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }

    private fun setAveImgselected() {
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#306060"))
        aveImg.setColorFilter(Color.parseColor("#ffffff"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
    }

    private fun setRoedorImgselected() {
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#306060"))
        roedorImg.setColorFilter(Color.parseColor("#ffffff"))
    }

    private fun setTodoImgselected() {
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#306060"))
        todoImg.setColorFilter(Color.parseColor("#ffffff"))
        cardBird.setCardBackgroundColor(Color.parseColor("#ffffff"))
        aveImg.setColorFilter(Color.parseColor("#000000"))
        cardMouse.setCardBackgroundColor(Color.parseColor("#ffffff"))
        roedorImg.setColorFilter(Color.parseColor("#000000"))
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

    override fun onClick(v: View?) {

    }

    private fun makeActionEditText() {
        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().isNotEmpty()) {
                    filter(editable.toString())
                }
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<AnimalAdapterData> = ArrayList<AnimalAdapterData>()
        for (item in mlist) {
            if (item.nombre.toLowerCase().contains(text.toLowerCase()) ||
                    item.sexo.toLowerCase().contains(text.toLowerCase()) ||
                    item.provincia.toLowerCase().contains(text.toLowerCase()) ||
                    item.pais.toLowerCase().contains(text.toLowerCase()) ||
                    item.edad.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if(this::adapter.isInitialized){
            adapter.filterList(filteredList)
        }
    }





}